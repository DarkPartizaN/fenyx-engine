package com.fenyx.ui;

import com.fenyx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author DarkPartizaN
 */
public class UIManager {

    public static int UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT;
    public static boolean UI_DRAW_BOUNDS = true;

    private static final HashMap<Integer, UILayer> layers = new HashMap<>();
    private static boolean state = false;
    private static int last_layer = 0; //Protection from 'genius' people, who decide to add a crap bunch of random layer numbers

    //!CALL IT AT FIRST TO CORRECT INITIALIZE UI!
    public static void init(int width, int height) {
        UI_SCREEN_WIDTH = width;
        UI_SCREEN_HEIGHT = height;

        //Create default layer
        layers.put(0, new UILayer());
    }

    //!And this before your main loop started
    public static void setActive(boolean active) {
        state = active;
    }

    public static void add(UI ui) {
        add(ui, 0);
    }

    public static void add(UI ui, int layer) {
        if (layers.get(layer) == null)
            layers.put(layer, new UILayer());
        if (last_layer < layer)
            last_layer = layer; //HACKHACK: now, it 'OutOfBounds'-proof :)

        layers.get(layer).add(ui);
    }

    public static void remove(UI ui) {
        for (UILayer layer : layers.values()) {
            layer.remove(ui);
            break; //NOTE: we search for first match, cause there's no doublers of UI object in normal case though
        }
    }

    public static void remove(UI ui, int layer) {
        //NOTE: use UI.setVisible(boolean) method instead, if you wanna to re-use this UI object lately
        layers.get(layer).remove(ui);
    }

    public static void removeAllUI(int layer) {
        //NOTE: use UI.setVisible(boolean) method instead, if you wanna to re-use this UI object lately
        layers.get(layer).removeAll();
    }

    public static void removeAllLayers() {
        layers.values().forEach((layer) -> {
            layer.removeAll();
        });

        layers.clear();
    }

    public static void clearLayer(int layer) {
        layers.get(layer).uis.clear(); //Not destroying UIs, safe for re-use them, just clear layer from UI objects
    }

    public static void frame() {
        if (!state) return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        UILayer tmp;
        //HACKHACK: we don't need sort, cause hash key is number of layer :crazy.gif:
        for (int i = 0; i <= last_layer; i++) {
            tmp = layers.get(i);
            if (tmp != null) tmp.draw();
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScissor(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT);
    }

    //Simple class for layer representation
    private static class UILayer {

        private final ArrayList<UI> uis = new ArrayList<>();

        public void add(UI ui) {
            if (!uis.contains(ui)) uis.add(ui);
        }

        public void remove(UI ui) {
            if (uis.contains(ui)) {
                ui.onDestroy();
                uis.remove(ui);
            }
        }

        public void removeAll() {
            uis.forEach(UI::onDestroy);
            uis.clear();
        }

        public void draw() {
            uis.forEach((UI ui) -> {
                if (ui != null) {
                    ui.update();
                    
                    int tmp_y = (int) (UI_SCREEN_HEIGHT - ui.clip_y - ui.clip_h);
                    GL11.glScissor(ui.clip_x - 1, tmp_y - 1, ui.clip_w + 2, ui.clip_h + 2);
                    
                    if (UI_DRAW_BOUNDS) {
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                        GL11.glColor4f(Color.red.r, Color.red.g, Color.red.b, Color.red.a);
                        GL11.glBegin(GL11.GL_LINE_LOOP);
                        {
                            GL11.glVertex2f(ui.clip_x, ui.clip_y);
                            GL11.glVertex2f(ui.clip_x + ui.clip_w, ui.clip_y);
                            GL11.glVertex2f(ui.clip_x + ui.clip_w, ui.clip_y + ui.clip_h);
                            GL11.glVertex2f(ui.clip_x - 1f, ui.clip_y + ui.clip_h);
                        }
                        GL11.glEnd();
                    }
                    
                    if (ui.isVisible())
                        ui.onDraw();
                }
            });
        }
    }
}
