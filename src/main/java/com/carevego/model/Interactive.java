package com.carevego.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Interactive {
    private String type;
    private NFMReply nfmReply;

    // Getters and Setters
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("nfm_reply")
    public NFMReply getNfmReply() {
        return nfmReply;
    }

    public void setNfmReply(NFMReply nfmReply) {
        this.nfmReply = nfmReply;
    }
}
