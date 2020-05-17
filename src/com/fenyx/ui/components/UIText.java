package com.fenyx.ui.components;

import com.fenyx.graphics.Color;
import com.fenyx.graphics.font.FenyxFont;
import com.fenyx.graphics.font.FontManager;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author DarkPartizaN
 */
public class UIText extends UIItem {

    protected FenyxFont font;
    protected String text;
    public Color color = Color.white.get();

    public UIText() {
        setFont(FontManager.getDefault());
        setSize(0, font.getHeight());
        this.text = "";
    }

    public UIText(String text) {
        this();
        this.text = text;
        setSize(this.font.stringWidth(text), this.font.getHeight());
    }

    public void setText(String text) {
        this.text = text;
        setSize(this.font.stringWidth(text), this.font.getHeight());
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
        }
        catch (NumberFormatException numberFormatException) {
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

        float tmp_x, tmp_y;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getFontTexture().id);
        GL11.glColor4f(color.r, color.g, color.b, color.a);

        GL11.glBegin(GL11.GL_QUADS);

        tmp_x = x;
        tmp_y = y - font.ascent;

        for (char c : text.toCharArray()) {
            float w = font.charWidth(c);
            float h = font.getHeight();

            if (c == '\n') y += h;

            float u = 1.0F / font.getFontTexture().width * font.getCharX(c);
            float v = 0f;
            float u2 = u + 1.0F / font.getFontTexture().width * w;
            float v2 = 1f;

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

        GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}