package com.carevego.model;

public class Error {
    private String code;
    private String title;

    // Add other fields as necessary

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
