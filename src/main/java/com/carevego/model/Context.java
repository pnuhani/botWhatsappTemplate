package com.carevego.model;

public class Context {
    private String from;
    private String id;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Context{" +
                "from='" + from + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
