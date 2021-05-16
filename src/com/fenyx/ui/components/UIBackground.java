package com.fenyx.ui.components;

import com.fenyx.graphics.Color;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author DarkPartizaN
 */
public class UIBackground extends UIComponent {

    public Color color = Color.violet.get(); //We need a valid color here
    public UIImage image;
    public boolean tiled = false;

    public void onDraw() {
        if (image == null) {
            GL11.glColor4f(color.r, color.g, color.b, color.a);

            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2f(x, y);
                GL11.glVertex2f(x + width, y);
                GL11.glVertex2f(x + width, y + height);
                GL11.glVertex2f(x, y + height);
            }
            GL11.glEnd();

            GL11.glColor4f(1f, 1f, 1f, 1f);
            return;
        }
        else
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getTexture().id);

        int tmp_x, tmp_y;

        GL11.glBegin(GL11.GL_QUADS);
        {
            if (this.tiled) {
                for (tmp_y = y; tmp_y < height; tmp_y += image.getHeight()) {
                    for (tmp_x = x; tmp_x < width; tmp_x += image.getWidth()) {
                        GL11.glTexCoord2f(0, 0);
                        GL11.glVertex2f(tmp_x, tmp_y);
                        GL11.glTexCoord2f(1, 0);
                        GL11.glVertex2f(tmp_x + image.getWidth(), tmp_y);
                        GL11.glTexCoord2f(1, 1);
                        GL11.glVertex2f(tmp_x + image.getWidth(), tmp_y + image.getHeight());
                        GL11.glTexCoord2f(0, 1);
                        GL11.glVertex2f(tmp_x, tmp_y + image.getHeight());
                    }
                }
            } else {
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(x, y);
                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f(x + width, y);
                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(x + height, y + height);
                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f(x, y + height);
            }
        }
        GL11.glEnd();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
