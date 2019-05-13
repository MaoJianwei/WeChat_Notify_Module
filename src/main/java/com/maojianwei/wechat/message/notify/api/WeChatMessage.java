package com.maojianwei.wechat.message.notify.api;

import com.maojianwei.wechat.message.notify.lib.WeChatUser;

import static com.maojianwei.wechat.message.notify.api.WeChatMessageLevel.INFO;

public class WeChatMessage {

    private WeChatUser receiver;
    private String appId;
    private WeChatMessageLevel level;
    private String message;
    private boolean justMessage;

    public WeChatMessage(WeChatUser receiver, String appId, WeChatMessageLevel level, String msg, boolean justMsg) {
        this.receiver = receiver;
        this.appId = appId;
        this.level = level;
        this.message = msg;
        this.justMessage = justMsg;
    }

    public WeChatUser getReceiver() {
        return receiver;
    }

    public String getAppId() {
        return appId;
    }

    public WeChatMessageLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public boolean isJustMessage() {
        return justMessage;
    }

    @Override
    public String toString() {
        return justMessage ?
                String.format("%s", message) :
                String.format("%s - %s - %s", appId, level, message);
    }


    public static WeChatMessageBuilder builder() {
        return new WeChatMessageBuilder();
    }

    public static class WeChatMessageBuilder {

        private WeChatUser receiver = null;
        private String appId = "";
        private WeChatMessageLevel level = INFO;
        private String message = "";
        private boolean justMessage = false;

        private WeChatMessageBuilder() {
        }

        public WeChatMessageBuilder setReceiver(WeChatUser receiver) {
            this.receiver = receiver;
            return this;
        }

        public WeChatMessageBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public WeChatMessageBuilder setLevel(WeChatMessageLevel level) {
            this.level = level;
            return this;
        }

        public WeChatMessageBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public WeChatMessageBuilder setJustMessage(boolean justMessage) {
            this.justMessage = justMessage;
            return this;
        }

        public WeChatMessage build() {
            return new WeChatMessage(receiver, appId, level, message, justMessage);
        }
    }
}
