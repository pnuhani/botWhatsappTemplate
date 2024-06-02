package com.carevego.model;


public class Text {
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Text{" +
                "body='" + body + '\'' +
                '}';
    }
}
