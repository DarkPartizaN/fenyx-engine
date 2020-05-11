package com.fenyx.core;

/**
 *
 * @author DarkPartizaN
 */
public class EngineTimer {

    public static int timeSpeed = 100;
    private static long lastTime = System.currentTimeMillis();
    public static long time;
    public static float frametime;
    public static int fps;

    public static void tick() {
        if (timeSpeed <= 0) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > (long)(1000 / timeSpeed)) {
            long delta = (long)((float)timeSpeed / 1000.0f * frametime);
            if (delta < 1L) delta = 1L;

            time += delta;
            lastTime = currentTime;
        }
    }
}
