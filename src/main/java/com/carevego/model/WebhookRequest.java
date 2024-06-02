package com.carevego.model;



import java.util.List;

public class WebhookRequest {
    private List<Entry> entry;
    private List<Error> errors;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "WebhookRequest{" +
                "entry=" + entry +
                ", errors=" + errors +
                '}';
    }
}
