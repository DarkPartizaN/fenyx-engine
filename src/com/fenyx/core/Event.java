package com.fenyx.core;

/**
 *
 * @author DarkPartizaN
 */
public abstract class Event {

    public static final int EVENT_INSTANT = 0, EVENT_DELAYED = 1, EVENT_REGULAR = 2, EVENT_POST = 3;
    public int type;
    public float delay = 0f;
    public long startTime = 0;

    public abstract void runEvent();

    public Event(int type) {
        this.type = type;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}