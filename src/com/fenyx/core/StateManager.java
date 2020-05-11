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
        AppState state = states.get(id);
        currentState = state;
    }

    public static void pushState(int id) {
        AppState state = states.get(id);
        state.prevState = currentState;
        currentState = state;
    }

    public static void popState() {
        currentState = currentState.getPrevState();
    }

    public static AppState getState(int id) {
        return states.get(id);
    }

    public static AppState getCurrentState() {
        return currentState;
    }

    public static void processState() {
        if (currentState == null) return;

        currentState.process();

        if (currentState.finished) {
            currentState.onStop();

            if (currentState.getNextState() != null) {
                StateManager.pushState(currentState.getNextState().id);
                currentState.setActive(true);
            }
        }
    }
}
