package com.fenyx.graphics;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;

/**
 *
 * @author DarkPartizaN
 */
public class AWTImage {

    public static final ColorModel alphaColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, Transparency.TRANSLUCENT, DataBufferByte.TYPE_BYTE);
    public static final ColorModel simpleColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 0}, false, false, Transparency.OPAQUE, DataBufferByte.TYPE_BYTE);

    public String name;
    public int width;
    public int height;
    public boolean has_alpha;
    public ByteBuffer raw;

    public final BufferedImage target_image;
    private final Graphics2D target_graphics;

    public AWTImage(String name, BufferedImage source_image) {
        this.name = name;
        this.width = source_image.getWidth();
        this.height = source_image.getHeight();

        WritableRaster raster;

        if (has_alpha = source_image.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(0, source_image.getWidth(), source_image.getHeight(), 4, null);
            target_image = new BufferedImage(alphaColorModel, raster, false, null);
        } else {
            raster = Raster.createInterleavedRaster(0, source_image.getWidth(), source_image.getHeight(), 3, null);
            target_image = new BufferedImage(simpleColorModel, raster, false, null);
        }

        target_graphics = target_image.createGraphics();
        target_graphics.drawImage(source_image, 0, 0, null);

        byte[] data = ((DataBufferByte) target_image.getRaster().getDataBuffer()).getData();

        ByteBuffer imageData = BufferUtils.createByteBuffer(data.length);
        imageData.order(ByteOrder.nativeOrder());
        imageData.put(data, 0, data.length);
        imageData.flip();

        this.raw = imageData;
    }

    public void refreshImage(BufferedImage source_image) {
        target_graphics.drawImage(source_image, 0, 0, null);

        byte[] data = ((DataBufferByte) target_image.getRaster().getDataBuffer()).getData();

        ByteBuffer imageData = BufferUtils.createByteBuffer(data.length);
        imageData.order(ByteOrder.nativeOrder());
        imageData.put(data, 0, data.length);
        imageData.flip();

        this.raw = imageData;
    }
}