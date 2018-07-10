package com.dgk.klibrary.demo.rxjava.event;

public class TextChangeEvent {

    private String content;

    public TextChangeEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TextChangeEvent{" +
                "content='" + content + '\'' +
                '}';
    }
}
