package com.fenyx.core;

import com.fenyx.ui.UIManager;
import com.fenyx.utils.ResourceUtils;
import com.fenyx.utils.StringUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.lwjgl.BufferUtils;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author DarkPartizaN
 */
final class EngineWindow {

    private long window_handle;
    private boolean show_system_cursor = true;
    boolean isNeedClose = false, isNeedScreenshot = false;

    //FPS
    private int fps;
    private int tmpFps;
    private long fpsUpdate;
    private long currentFrametime;
    private long lastFrametime = System.currentTimeMillis();
    private float frametime;

    EngineWindow(String title, int x, int y, int width, int height, boolean fullscreen) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        window_handle = GLFW.glfwCreateWindow(width, height, title, (long) (fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0), 0);
        if (window_handle == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        GLFW.glfwSetKeyCallback(window_handle, new KeyCallback());
        GLFW.glfwSetCharCallback(window_handle, new CharCallback());
        GLFW.glfwSetMouseButtonCallback(window_handle, new MouseButtonsCallback());

        GLFW.glfwMakeContextCurrent(window_handle);
        GLFW.glfwSwapInterval(1);

        ScreenConfig.setupScreen(width, height, fullscreen);
        UIManager.init(width, height);

        GLFW.glfwSetWindowPos(window_handle, x, y);

        if (GL.createCapabilities() == null)
            throw new RuntimeException("Failed to create GLCapabilities");

        GLFW.glfwShowWindow(window_handle);
    }

    void display() {
        double[] mouse_x = new double[1];
        double[] mouse_y = new double[1];

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        //LOOP
        while (!GLFW.glfwWindowShouldClose(window_handle)) {
            currentFrametime = System.currentTimeMillis();

            //Hide/show cursor
            if (show_system_cursor != Input.show_system_cursor) {
                if (!Input.show_system_cursor) {
                    GLFW.glfwSetInputMode(window_handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
                } else {
                    GLFW.glfwSetInputMode(window_handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                }
                show_system_cursor = Input.show_system_cursor;
            }

            //Update cursor position
            GLFW.glfwGetCursorPos((long) this.window_handle, (double[]) mouse_x, (double[]) mouse_y);
            Input.updateMousePos((int) mouse_x[0], (int) mouse_y[0]);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            GL11.glScissor(0, 0, ScreenConfig.screen_width, ScreenConfig.screen_height);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glColor4f(1f, 1f, 1f, 1f);

            //Process state
            EngineTimer.tick();
            StateManager.processState();

            //UI
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0f, ScreenConfig.screen_width, ScreenConfig.screen_height, 0.0f, 0.0f, 1.0f);

            UIManager.frame();

            GLFW.glfwSwapBuffers(window_handle);
            GLFW.glfwPollEvents();

            if (isNeedScreenshot) screenshot();

            if (currentFrametime - fpsUpdate >= 1000L) {
                fpsUpdate = currentFrametime;
                fps = tmpFps;
                tmpFps = 0;
            } else {
                tmpFps++;
            }
            frametime = (float) (currentFrametime - lastFrametime) / 10f;
            lastFrametime = currentFrametime;

            EngineTimer.frametime = frametime;
            EngineTimer.fps = fps;
        }

        //OpenGL stuff here
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        StateManager.getCurrentState().onStop();
        StateManager.getCurrentState().finished = true;
        StateManager.setState(StateManager.STATE_NULL);

        Callbacks.glfwFreeCallbacks((long) this.window_handle);
        GLFW.glfwDestroyWindow(window_handle);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private char charFromKeyCode(int key) {
        switch (key) {
            case 259:
                return '\b';
            case 258:
                return '\t';
            case 261:
                return '';
            case 257:
                return '\n';
        }

        return '\u0000';
    }

    private final class MouseButtonsCallback extends GLFWMouseButtonCallback {

        public void invoke(long window, int button, int action, int mods) {
            if (action == 1 || action == 2) {
                Input.pressKey(button);
            }
            if (action == 0) {
                Input.resetKey(button);
            }
        }
    }

    private final class CharCallback extends GLFWCharCallback {

        public void invoke(long window, int codepoint) {
            Input.inputChar((char) codepoint);
        }
    }

    private class KeyCallback extends GLFWKeyCallback {

        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == 256 && action == 0) {
                GLFW.glfwSetWindowShouldClose((long) window, (boolean) true);
            }
            if (key == GLFW.GLFW_KEY_F5) isNeedScreenshot = true;

            if (action == 1) {
                char c = EngineWindow.this.charFromKeyCode(key);
                if (c != '\u0000') {
                    Input.inputChar(c);
                }
                Input.pressKey(key);
            }

            if (action == 0) {
                Input.resetKey(key);
            }
        }
    }

    private void screenshot() {
        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = ScreenConfig.screen_width;
        int height = ScreenConfig.screen_height;
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp).order(ByteOrder.nativeOrder());

        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss");

        ResourceUtils.writePNG(StringUtils.concat(ResourceUtils.DIR_ROOT, "screenshots/", formatter.format(date), ".png"), buffer, width, height);

        isNeedScreenshot = false;
    }
}
