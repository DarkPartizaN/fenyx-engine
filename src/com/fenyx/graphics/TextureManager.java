package com.fenyx.graphics;

import com.fenyx.utils.ResourceUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

/**
 *
 * @author DarkPartizaN
 */
public class TextureManager {

    private static final HashMap<String, Texture> cached_textures = new HashMap<>();
    private static final Texture null_texture = createTexture(ResourceUtils.NULL_IMAGE);

    public static Texture createTexture(String name, int width, int height, TextureFormat format, ByteBuffer raw) {
        if (!TextureManager.cached_textures.containsKey(name)) {
            Texture tmp = new Texture();
            tmp.width = width;
            tmp.height = height;
            tmp.textureFormat = format;
            tmp.raw = raw;

            compileTexture(tmp);

            TextureManager.cached_textures.put(name, tmp);
        }

        return getTexture(name);
    }

    public static Texture createTexture(AWTImage image) {
        return createTexture(image.name, image.width, image.height, image.has_alpha ? TextureFormat.getDefaultAlpha() : TextureFormat.getDefault(), image.raw);
    }

    public static Texture createTextureNearest(AWTImage image) {
        return createTexture(image.name, image.width, image.height, image.has_alpha ? TextureFormat.getDefaultAlphaNearest(): TextureFormat.getDefaultNearest(), image.raw);
    }

    public static Texture getTexture(String name) {
        if (TextureManager.cached_textures.containsKey(name))
            return TextureManager.cached_textures.get(name);

        return null_texture;
    }

    public static void compileTexture(Texture t) {
        t.id = GL11.glGenTextures();
        copyTextureParams(t);
    }

    private static void copyTextureParams(Texture t) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, t.id);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, t.textureFormat.wrapping);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, t.textureFormat.wrapping);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, t.textureFormat.minFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, t.textureFormat.magFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, t.textureFormat.internalFormat, t.width, t.height, 0, t.textureFormat.format, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) t.raw);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
