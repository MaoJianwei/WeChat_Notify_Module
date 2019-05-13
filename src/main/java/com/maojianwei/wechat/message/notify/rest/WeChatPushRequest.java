package com.maojianwei.wechat.message.notify.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maojianwei.wechat.message.notify.lib.WeChatUser;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatPushRequest {

    private String msgtype = "text";
    private Map<String, String> text = new HashMap<>();

    private String touser;
    private int toparty;
    private int totag;

    private int agentid;

    private int safe; // 0 - unencrypted, 1 - encrypted

    public WeChatPushRequest(String msg, WeChatUser receiver, int agentId) {
        this(msg, receiver, agentId, false);
    }

    public WeChatPushRequest(String msg, WeChatUser receiver, int agentId, boolean safeMode) {
        this.touser = receiver.getUserId();
        this.toparty = receiver.getPartyId();
        this.totag = receiver.getTagId();
        this.agentid = agentId;
        text.put("content", msg);
        this.safe = safeMode ? 1 : 0;
    }

    public String getTouser() {
        return touser;
    }

    public int getToparty() {
        return toparty;
    }

    public int getTotag() {
        return totag;
    }

    public int getAgentid() {
        return agentid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public Map<String, String> getText() {
        return text;
    }

    public int getSafe() {
        return safe;
    }
}
