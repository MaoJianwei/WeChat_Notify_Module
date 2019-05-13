package com.maojianwei.wechat.message.notify.rest;

public final class WeChatTokenRequest {

    private WeChatTokenRequest() {
    }

    public static String getAccessTokenUrl(String corpId, String corpSecret) {
        return String.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
                corpId, corpSecret);
    }
}
