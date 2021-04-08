package com.fenyx.core;

import java.util.HashMap;

/**
 *
 * @author DarkPartizaN
 */
public class StateManager {

    public static final int STATE_NULL = -1;

    private static final HashMap<Integer, AppState> states = new HashMap<>();
    private static AppState currentState;

    //States
    public static void registerState(AppState state) {
        state.init();
        states.put(state.id, state);
    }

    public static void setState(int id) {
        if (currentState != null) currentState.setActive(false);
        if (id == STATE_NULL) {
            currentState = null;
            return;
        }

        AppState state = states.get(id);
        currentState = state;
        currentState.setActive(true);
    }

    public static void pushState(int id) {
        if (currentState != null) currentState.setActive(false);

        AppState state = states.get(id);
        state.prevState = currentState;
        currentState = state;
        currentState.setActive(true);
    }

    public static void popState() {
        if (currentState.getPrevState() != null) {
            currentState.setActive(false);
            currentState = currentState.getPrevState();
            currentState.setActive(true);
        }
    }

    public static AppState getState(int id) {
        return states.get(id);
    }

    public static AppState getCurrentState() {
        return currentState;
    }

    public static void processState() {
        if (currentState == null) return;

        if (currentState.isActive()) {
            currentState.handleEvents();
            currentState.process();
            currentState.handlePostEvents();

            if (currentState.finished) {
                currentState.onStop();

                if (currentState.getNextState() != null) {
                    StateManager.pushState(currentState.getNextState().id);
                    currentState.setActive(true);
                }
            }
        }
    }
}