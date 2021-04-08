package com.fenyx.graphics.font;

import com.fenyx.graphics.Texture;

/**
 *
 * @author DarkPartizaN
 */
public class FenyxFont {

    public String name;
    Texture font_texture;

    private String key_table;
    float[] char_offset;
    float[] char_width;
    public int font_height;
    public float ascent;

    void setKeyTable(String table) {
        key_table = table;
    }

    public String getKeyTable() {
        return key_table;
    }

    public float getCharX(char c) {
        int idx = key_table.indexOf(c);
        if (idx == -1) return 0;

        return char_offset[idx];
    }

    public int getHeight() {
        return font_height;
    }

    public float charWidth(int c) {
        int idx = key_table.indexOf(c);
        if (idx == -1) return 0;

        return char_width[idx];
    }

    public int stringWidth(String s) {
        if (s == null || s.isEmpty()) return 0;

        int w = 0;
        for (char c : s.toCharArray()) w += charWidth(c);

        return w;
    }

    public Texture getFontTexture() {
        return font_texture;
    }
}