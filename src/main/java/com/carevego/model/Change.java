package com.carevego.model;

public class Change {
    private Value value;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Change{" +
                "value=" + value +
                '}';
    }
}
