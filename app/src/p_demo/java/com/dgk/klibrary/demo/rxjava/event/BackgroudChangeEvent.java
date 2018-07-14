package com.dgk.klibrary.demo.rxjava.event;

public class BackgroudChangeEvent {

    private int color;

    public BackgroudChangeEvent(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
