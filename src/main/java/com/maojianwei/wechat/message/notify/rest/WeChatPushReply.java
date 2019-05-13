package com.maojianwei.wechat.message.notify.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatPushReply {

    private int errcode;
    private String errmsg;
    private String invaliduser;
    private String invalidparty;
    private String invalidtag;

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getInvaliduser() {
        return invaliduser;
    }

    public String getInvalidparty() {
        return invalidparty;
    }

    public String getInvalidtag() {
        return invalidtag;
    }
}
