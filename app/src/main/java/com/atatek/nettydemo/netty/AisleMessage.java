package com.atatek.nettydemo.netty;

public class AisleMessage {

    private AisleMsgHeader header;

    private String content;

    public AisleMessage(AisleMsgHeader header, String content) {
        this.header = header;
        this.content = content;
    }

    public AisleMsgHeader getHeader() {
        return header;
    }

    public void setHeader(AisleMsgHeader header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AisleMessage{" +
                "header=" + header +
                ", content='" + content + '\'' +
                '}';
    }
}
