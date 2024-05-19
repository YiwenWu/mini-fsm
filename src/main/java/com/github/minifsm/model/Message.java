package com.github.minifsm.model;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public class Message<S, E> implements Serializable {

    private final S source;
    private final E event;
    private final MessageHeaders headers;

    public static <S, E> Message<S, E> of(S source, E event, MessageHeaders headers) {
        return new Message<>(source, event, headers);
    }

    private Message(S source, E event, MessageHeaders headers) {
        this.source = source;
        this.event = requireNonNull(event);
        this.headers = headers;
    }

    public S getSource() {
        return source;
    }

    public E getEvent() {
        return event;
    }

    public MessageHeaders getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Message{");
        sb.append("source=").append(source);
        sb.append(", event=").append(event);
        sb.append('}');
        return sb.toString();
    }
}
