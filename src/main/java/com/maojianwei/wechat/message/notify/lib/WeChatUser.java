package com.maojianwei.wechat.message.notify.lib;

public class WeChatUser {

    private String userId;
    private int partyId;
    private int tagId;

    public WeChatUser(String userId, int partyId, int tagId) {
        this.userId = userId;
        this.partyId = partyId;
        this.tagId = tagId;
    }

    public String getUserId() {
        return userId;
    }

    public int getPartyId() {
        return partyId;
    }

    public int getTagId() {
        return tagId;
    }
}
