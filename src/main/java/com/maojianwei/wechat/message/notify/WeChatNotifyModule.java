package com.maojianwei.wechat.message.notify;

import com.maojianwei.wechat.message.notify.api.WeChatMessage;
import com.maojianwei.wechat.message.notify.api.WeChatNotifyService;
import com.maojianwei.wechat.message.notify.lib.WeChatUser;
import com.maojianwei.wechat.message.notify.rest.WeChatPushReply;
import com.maojianwei.wechat.message.notify.rest.WeChatPushRequest;
import com.maojianwei.wechat.message.notify.rest.WeChatTokenReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.maojianwei.wechat.message.notify.rest.WeChatTokenRequest.getAccessTokenUrl;
import static java.lang.String.format;

public class WeChatNotifyModule implements WeChatNotifyService {

    // Demo
    public static void main(String[] args) {

        WeChatNotifyService service = new WeChatNotifyModule(
                "<your corpId>", -1, "<your corpSecret>",
                new WeChatUser("<your userId>", -1, -1));
        service.start();
        service.pushMessage(WeChatMessage.builder().setAppId("Mao-Demo").setMessage("Bigmao Radio Station").build());
        service.stop();
    }


    private static final Logger logger = LoggerFactory.getLogger(WeChatNotifyModule.class);

    private ExecutorService pool;
    private BlockingQueue<WeChatMessage> queue = new LinkedBlockingQueue<>();

    private String corpId; // corp id
    private int agentid; // app id
    private String corpSecret; // app secret key
    private WeChatUser defaultReceiver;
    private String dynamicAccessToken;


    public WeChatNotifyModule(String corpId, int agentid, String corpSecret) {
        this(corpId, agentid, corpSecret, null);
    }

    public WeChatNotifyModule(String corpId, int agentid, String corpSecret, WeChatUser defaultReceiver) {
        this.corpId = corpId;
        this.agentid = agentid;
        this.corpSecret = corpSecret;
        this.defaultReceiver = defaultReceiver;
        this.dynamicAccessToken = "";
    }

    @Override
    public void updateAuthentication(String corpId, int agentid, String corpSecret) {
        if(corpId.equals(this.corpId) && agentid == this.agentid && corpSecret.equals(this.corpSecret)) {
            return;
        }
        this.corpId = corpId;
        this.agentid = agentid;
        this.corpSecret = corpSecret;
        this.dynamicAccessToken = "";
    }

    @Override
    public void setDefaultReceiver(WeChatUser defaultReceiver) {
        this.defaultReceiver = defaultReceiver;
    }

    @Override
    public void start() {
        pool = Executors.newCachedThreadPool();
        pool.submit(new SendTask());
    }

    @Override
    public void stop() {
        pool.shutdownNow();
        pool = null;
    }

    @Override
    public boolean pushMessage(WeChatMessage message) {
        return queue.offer(message);
    }

    @Override
    public boolean pushMessage(WeChatMessage message, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(message, timeout, unit);
    }

    private class SendTask implements Runnable {

        @Override
        public void run() {
            int errcode;
            WeChatMessage msg;
            while (true) {
                try {
                    if ((msg = queue.poll(500, TimeUnit.MILLISECONDS)) != null) {
                        while (true) {
                            errcode = pushNotification(msg);
                            if (errcode == 0) {
                                break;
                            } else if (errcode == 40014 || errcode == 41001 || errcode == 42001) {
                                while (!updateAccessToken()) {
                                    Thread.sleep(1000);
                                }
                            } else if (errcode == Integer.MAX_VALUE) {
                                logger.warn("Fail to pushNotification, no receiver, {}", msg.toString());
                                break;
                            } else {
                                logger.warn("Fail to pushNotification, {}, {}", errcode, msg.toString());
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        private int pushNotification(WeChatMessage msg) {
            if (msg.getReceiver() == null && defaultReceiver == null) {
                return Integer.MAX_VALUE; // avoid conflict with wechat definition.
            }

            WeChatPushRequest request = new WeChatPushRequest(msg.toString(),
                    msg.getReceiver() == null ? defaultReceiver : msg.getReceiver(), agentid);
            WeChatPushReply reply = new RestTemplate().postForObject(
                    format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s", dynamicAccessToken),
                    request, WeChatPushReply.class);
            return reply.getErrcode();
        }

        private boolean updateAccessToken() {
            WeChatTokenReply reply = new RestTemplate()
                    .getForObject(getAccessTokenUrl(corpId, corpSecret), WeChatTokenReply.class);
            if (reply.getErrcode() == 0) {
                dynamicAccessToken = reply.getAccess_token();
                logger.info("Update token {}", dynamicAccessToken);
                return true;
            }
            return false;
        }
    }
}
