package com.fenyx.core;

/**
 *
 * @author DarkPartizaN
 */
public class ScreenConfig {

    public static int screen_width;
    public static int screen_height;
    public static boolean fullscreen;
    public static float screen_aspect;

    public static void setupScreen(int width, int height, boolean fullscreen) {
        screen_width = width;
        screen_height = height;
        screen_aspect = (float)width / (float)height;
        ScreenConfig.fullscreen = fullscreen;
    }
}
