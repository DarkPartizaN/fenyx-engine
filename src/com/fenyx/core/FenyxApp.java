package com.fenyx.core;

import java.awt.Toolkit;

/**
 *
 * @author DarkPartizaN
 */
public abstract class FenyxApp {

    private EngineWindow window;

    public abstract void startApp();
    public abstract void destroyApp();

    public void createWindow(String title, int x, int y, int width, int height, boolean fullscreen) {
        window = new EngineWindow(title, x, y, width, height, fullscreen);
    }

    public void createWindow() {
        int w = AppParams.getInt("com.fenyx.Width");
        int h = AppParams.getInt("com.fenyx.Height");

        createWindow(AppParams.getString("com.fenyx.AppName"), Toolkit.getDefaultToolkit().getScreenSize().width / 2 - w / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - h / 2, w, h, AppParams.getBool("com.fenyx.Fullscreen"));
    }

    public void displayWindow() {
        window.display();
    }
}
