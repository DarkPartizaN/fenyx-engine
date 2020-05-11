package com.fenyx.graphics;

import java.nio.ByteBuffer;

/**
 *
 * @author DarkPartizaN
 */
public final class Texture {

    public int id;
    public int width;
    public int height;
    public TextureFormat textureFormat;
    public ByteBuffer raw;

    Texture() {
    }

}
