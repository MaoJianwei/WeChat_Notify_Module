package com.maojianwei.wechat.message.notify.api;

import com.maojianwei.wechat.message.notify.lib.WeChatUser;

import java.util.concurrent.TimeUnit;

public interface WeChatNotifyService {

    /**
     * Start to process WeChat messages.
     */
    void start();

    /**
     * Stop to process WeChat messages.
     */
    void stop();

    /**
     * Change default receiver.
     *
     * If no receiver is specified in {@link WeChatMessage}, send message to this receiver.
     *
     * @param defaultReceiver default message receiver
     */
    void setDefaultReceiver(WeChatUser defaultReceiver);

    /**
     * Set new authentication information
     * @param corpId corp id
     * @param agentid app id
     * @param corpSecret app secret
     */
    void updateAuthentication(String corpId, int agentid, String corpSecret);

    /**
     * Submit message to queue.
     *
     * Message will be sent to the specific/default receiver in the future as soon as possible.
     *
     * Will block until completing to push message into the queue.
     *
     * @param message notification message to be sent
     * @return {@code true} if the element was added to this queue, else {@code false}
     */
    boolean pushMessage(WeChatMessage message);

    /**
     * Submit message to queue.
     *
     * Message will be sent to the specific/default receiver in the future as soon as possible.
     *
     * Will block until completing to push message into the queue, or out of time.
     *
     * @param message notification message to be sent
     * @param timeout how long to wait before giving up, in units of {@code unit} parameter
     * @param unit a {@code TimeUnit} determining how to interpret the {@code timeout} parameter
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before space is available
     * @throws InterruptedException if interrupted while waiting
     */
    boolean pushMessage(WeChatMessage message, long timeout, TimeUnit unit) throws InterruptedException;
}
