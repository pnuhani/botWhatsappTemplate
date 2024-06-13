package com.carevego.util;

import com.carevego.model.Message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
