package com.fenyx.core;

/**
 *
 * @author DarkPartizaN
 */
public class EngineTimer {

    public static int timeSpeed = 100;
    private static long lastTime = System.currentTimeMillis(), lastSystemTime = System.currentTimeMillis();
    public static long time;
    public static float systemTime, tmpSystemTime;
    public static float frametime;
    public static int fps;

    public static void tick() {
        long delta;
        float systemDelta;
        long currentTime = System.currentTimeMillis();

        systemDelta = currentTime - lastSystemTime;

        if (systemDelta >= 1) {
            systemTime += systemDelta;
            lastSystemTime = currentTime;
        }

        if (timeSpeed <= 0) return;

        if (currentTime - lastTime >= (long) (1000 / timeSpeed)) {
            delta = (long) ((float) timeSpeed / 1000.0f * frametime);
            if (delta < 1L) delta = 1L;

            time += delta;
            lastTime = currentTime;
        }
    }
}
