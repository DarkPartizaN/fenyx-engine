package com.fenyx.graphics.font;

import java.awt.FontMetrics;

import com.fenyx.graphics.Texture;

/**
 *
 * @author DarkPartizaN
 */
public class FenyxFont {

    private String key_table;
    public Texture font_texture;
    public FontMetrics font_metrics;
    float[] char_offset;
    float[] char_width;
    float font_height;

    void setKeyTable(String table) {
        this.key_table = table;
    }

    public String getKeyTable() {
        return this.key_table;
    }

    public float getCharX(char c) {
        int idx = this.key_table.indexOf(c);
        return this.char_offset[idx];
    }

    public int getHeight() {
        return this.font_metrics.getHeight();
    }

    public float charWidth(int c) {
        int idx = this.key_table.indexOf(c);
        return this.char_width[idx];
    }

    public int stringWidth(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        return this.font_metrics.stringWidth(s);
    }

    public Texture getFontTexture() {
        return this.font_texture;
    }
}
