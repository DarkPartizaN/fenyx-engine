package com.fenyx.core;

/**
 *
 * @author DarkPartizaN
 */
public abstract class AppState {

    public AppState prevState, nextState;
    public boolean active = false, finished = false;
    public int id;

    public abstract void init();
    protected abstract void onActivate();
    public abstract void process();
    public abstract void handleEvents();
    protected abstract void onDeactivate();
    public abstract void onStop();

    protected void specialAction() {
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