package com.fenyx.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 *
 * @author DarkPartizaN
 */
public final class TextureFormat {

    private static TextureFormat defaultOpaque;
    private static TextureFormat defaultAlpha;
    private static TextureFormat defaultOpaqueNearest;
    private static TextureFormat defaultAlphaNearest;

    public int format;
    public int internalFormat;
    public int wrapping;
    public int minFilter;
    public int magFilter;
    public boolean has_alpha;

    public static TextureFormat getDefault() {
        if (TextureFormat.defaultOpaque == null) {
            TextureFormat.defaultOpaque = new TextureFormat();
            TextureFormat.defaultOpaque.internalFormat = GL11.GL_RGB8;
            TextureFormat.defaultOpaque.format = GL11.GL_RGB;
            TextureFormat.defaultOpaque.wrapping = GL12.GL_CLAMP_TO_EDGE;
            TextureFormat.defaultOpaque.minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;
            TextureFormat.defaultOpaque.magFilter = GL11.GL_LINEAR;
            TextureFormat.defaultOpaque.has_alpha = false;
        }

        return TextureFormat.defaultOpaque;
    }

    public static TextureFormat getDefaultAlpha() {
        if (TextureFormat.defaultAlpha == null) {
            TextureFormat.defaultAlpha = new TextureFormat();
            TextureFormat.defaultAlpha.internalFormat = GL11.GL_RGBA8;
            TextureFormat.defaultAlpha.format = GL11.GL_RGBA;
            TextureFormat.defaultAlpha.wrapping = GL12.GL_CLAMP_TO_EDGE;
            TextureFormat.defaultAlpha.minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;
            TextureFormat.defaultAlpha.magFilter = GL11.GL_LINEAR;
            TextureFormat.defaultAlpha.has_alpha = true;
        }

        return TextureFormat.defaultAlpha;
    }

    public static TextureFormat getDefaultNearest() {
        if (TextureFormat.defaultOpaqueNearest == null) {
            TextureFormat.defaultOpaqueNearest = new TextureFormat();
            TextureFormat.defaultOpaqueNearest.internalFormat = GL11.GL_RGB8;
            TextureFormat.defaultOpaqueNearest.format = GL11.GL_RGB;
            TextureFormat.defaultOpaqueNearest.wrapping = GL12.GL_CLAMP_TO_EDGE;
            TextureFormat.defaultOpaqueNearest.minFilter = GL11.GL_NEAREST_MIPMAP_NEAREST;
            TextureFormat.defaultOpaqueNearest.magFilter = GL11.GL_NEAREST;
            TextureFormat.defaultOpaqueNearest.has_alpha = false;
        }

        return TextureFormat.defaultOpaqueNearest;
    }

    public static TextureFormat getDefaultAlphaNearest() {
        if (TextureFormat.defaultAlphaNearest == null) {
            TextureFormat.defaultAlphaNearest = new TextureFormat();
            TextureFormat.defaultAlphaNearest.internalFormat = GL11.GL_RGBA8;
            TextureFormat.defaultAlphaNearest.format = GL11.GL_RGBA;
            TextureFormat.defaultAlphaNearest.wrapping = GL12.GL_CLAMP_TO_EDGE;
            TextureFormat.defaultAlphaNearest.minFilter = GL11.GL_NEAREST_MIPMAP_NEAREST;
            TextureFormat.defaultAlphaNearest.magFilter = GL11.GL_NEAREST;
            TextureFormat.defaultAlphaNearest.has_alpha = true;
        }

        return TextureFormat.defaultAlphaNearest;
    }
}