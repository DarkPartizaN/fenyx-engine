package com.fenyx.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author DarkPartizaN
 */
public abstract class AppState {

    public AppState prevState, nextState;
    public boolean active = false, finished = false;
    public int id;
    private final Queue<Event> events = new ConcurrentLinkedQueue<>();
    private final Queue<Event> delayedEvents = new ConcurrentLinkedQueue<>();
    private final Queue<Event> postEvents = new ConcurrentLinkedQueue<>();

    public abstract void init();
    protected abstract void onActivate();
    public abstract void process();
    protected abstract void onDeactivate();
    public abstract void onStop();

    protected void specialAction() {
    }

    public void sendEvent(Event event) {
        switch (event.type) {
            case Event.EVENT_INSTANT:
                events.add(event);
                break;
            case Event.EVENT_DELAYED:
            case Event.EVENT_REGULAR:
                 event.startTime = (long) (EngineTimer.systemTime + (event.delay * 1000));
                 delayedEvents.add(event);
                 break;
            case Event.EVENT_POST:
                 postEvents.add(event);
                 break;
            default:
                events.add(event);
                break;
        }
    }

    void handleEvents() {
        for (Event e : events) {
                e.runEvent();
                events.remove(e);
        }

        handleDelayedEvents();
    }

    void handleDelayedEvents() {
        for (Event e : delayedEvents) {
            if (EngineTimer.systemTime >= e.startTime) {
                e.runEvent();
                if (e.type == Event.EVENT_REGULAR) e.startTime = (long) (e.startTime + (e.delay * 1000));
                if (e.type != Event.EVENT_REGULAR) delayedEvents.remove(e);
            }
        }
    }

    void handlePostEvents() {
        for (Event e : postEvents) {
            e.runEvent();
            postEvents.remove(e);
        }
    }

    public final void setActive(boolean active) {
        this.active = active;

        if (active)
            onActivate();
        else
            onDeactivate();
    }

    public final boolean isActive() {
        return active;
    }

    public AppState getPrevState() {
        return prevState;
    }

    public void setNextState(AppState next) {
        nextState = next;
    }

    public AppState getNextState() {
        return nextState;
    }

    public void markToFinish() {
        finished = true;
    }
}
