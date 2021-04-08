package com.fenyx.graphics.font;

import com.fenyx.graphics.AWTImage;
import com.fenyx.graphics.TextureFormat;
import com.fenyx.graphics.TextureManager;
import com.fenyx.utils.ResourceUtils;
import com.fenyx.utils.StringUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

/**
 *
 * @author DarkPartizaN
 */
public class FontManager {

    public static final int FLAG_PLAIN = 0;
    public static final int FLAG_BOLD = 1;

    private static final HashMap<String, FenyxFont> cached_fonts = new HashMap<>();

    //Avaliable symbols
    private static final String key_table = StringUtils.concat(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase(),
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ",
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toLowerCase(),
            "0123456789",
            " $+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~");

    public static final String getDefaultCharTable() {
        return key_table;
    }

    public static FenyxFont getDefault() {
        return createFont("defaultFont", ResourceUtils.loadTTF("", 16), FontManager.getDefaultCharTable());
    }

    public static FenyxFont getDefault(int size) {
        return createFont("defaultFont", ResourceUtils.loadTTF("", size), FontManager.getDefaultCharTable());
    }

    public static FenyxFont getDefault(int size, int flags) {
        return createFont("defaultFont", ResourceUtils.loadTTF("", size), FontManager.getDefaultCharTable());
    }

    public static String getAvaliableChars(Font font, String key_table) {
        String tmpChars = "";

        for (char c : key_table.toCharArray()) {
            if (font.canDisplay(c)) tmpChars = StringUtils.concat(tmpChars, String.valueOf(c));
        }

        return tmpChars;
    }

    public static FenyxFont createFont(String name, Font font, String key_table) {
        name = StringUtils.concat(name, font.getSize());
        key_table = getAvaliableChars(font, key_table);

        FenyxFont tmp = new FenyxFont();
        tmp.name = name;
        tmp.setKeyTable(key_table);

        Graphics2D g = new BufferedImage(1, 1, 2).createGraphics();
        g.setFont(font);
        FontMetrics font_metrics = g.getFontMetrics();

        float w = (float) font_metrics.getStringBounds(key_table, g).getWidth();
        float h = (float) font_metrics.getStringBounds(key_table, g).getHeight();
        tmp.font_height = (int) h;
        tmp.ascent = h - font_metrics.getMaxAscent();

        WritableRaster raster = Raster.createInterleavedRaster(0, (int) w, (int) h, 4, null);
        BufferedImage source_image = new BufferedImage(AWTImage.alphaColorModel, raster, false, null);

        g = source_image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(Color.white);

        char[] char_table = key_table.toCharArray();
        float[] char_offset = new float[char_table.length];

        float tmp_x = 0.0f;

        for (int i = 0; i < char_table.length; i++) {
            g.drawChars(char_table, i, 1, (int) tmp_x, g.getFontMetrics().getMaxAscent());

            char_offset[i] = tmp_x;
            tmp_x += g.getFontMetrics().charWidth(char_table[i]);
        }
        tmp.char_offset = char_offset;
        tmp.char_width = new float[char_offset.length];

        for (int i = 0; i < char_offset.length; i++) {
            int idx = i;
            int idx2 = idx + 1;

            if (idx == key_table.length() - 1) {
                idx2 = idx;
                idx--;
            }
            tmp.char_width[i] = char_offset[idx2] - char_offset[idx];
        }

        byte[] data = ((DataBufferByte) source_image.getRaster().getDataBuffer()).getData();

        ByteBuffer imageData = BufferUtils.createByteBuffer(data.length);
        imageData.put(data, 0, data.length).flip();
        tmp.font_texture = TextureManager.createTexture(name, (int) w, (int) h, TextureFormat.getDefaultAlpha(), imageData);

        FontManager.cached_fonts.put(name, tmp);

        return tmp;
    }

    public static FenyxFont createFont(String name, Font font) {
        return createFont(name, font, FontManager.getDefaultCharTable());
    }

    public static int cashedFontNum() {
        return cached_fonts.size();
    }
}
