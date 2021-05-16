package com.fenyx.ui.components;

import com.fenyx.graphics.Color;
import com.fenyx.graphics.font.FenyxFont;
import com.fenyx.graphics.font.FontManager;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author DarkPartizaN
 */
public class UIText extends UIComponent {

    protected FenyxFont font= FontManager.getDefault(); //We need a valid font here;
    protected String text;
    public Color color = Color.white.get(); //We need a valid color

    public UIText() {
        setSize(0, (int) (this.font.getHeight() - font.ascent));
        this.text = "";
    }

    public UIText(String text) {
        this();
        this.text = text;
        setSize(this.font.stringWidth(text), (int) (this.font.getHeight() - font.ascent));
    }

    public void setText(String text) {
        this.text = text;
        setSize(this.font.stringWidth(text), (int) (this.font.getHeight() - font.ascent));
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        if (isInteger())
            return Integer.parseInt(text);

        return 0;
    }

    public boolean isInteger() {
        try {
            Integer.valueOf(text);

            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public boolean isEmpty() {
        return (text == null || text.isEmpty());
    }

    public boolean equals(String s) {
        return s.equals(text);
    }

    public final void setFont(FenyxFont font) {
        this.font = font;
        setSize(font.stringWidth(text), font.getHeight());
    }

    public final FenyxFont getFont() {
        return font;
    }

    public void onDraw() {
        if (text == null) return;
        if (text.length() <= 0) return;

        float tmp_x = x - 1;
        float tmp_y = y - font.ascent;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getFontTexture().id);
        GL11.glColor4f(color.r, color.g, color.b, color.a);

        float fw = 1.0f / font.getFontTexture().width;
        float u, v = 0f;
        float u2, v2 = 1f;

        GL11.glBegin(GL11.GL_QUADS);
        {
            for (char c : text.toCharArray()) {
                float w = font.charWidth(c);
                float h = font.getHeight();

                u = fw * font.getCharX(c);
                u2 = u + fw * w;

                GL11.glTexCoord2f(u, v);
                GL11.glVertex2f(tmp_x, tmp_y);

                GL11.glTexCoord2f(u2, v);
                GL11.glVertex2f(tmp_x + w, tmp_y);

                GL11.glTexCoord2f(u2, v2);
                GL11.glVertex2f(tmp_x + w, tmp_y + h);

                GL11.glTexCoord2f(u, v2);
                GL11.glVertex2f(tmp_x, tmp_y + h);

                tmp_x += w;
            }
        }
        GL11.glEnd();

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
