package com.fenyx.ui;

/**
 *
 * @author DarkPartizaN
 */
public final class UIColor {

    public static final UIColor transparent = new UIColor(0.0F, 0.0F, 0.0F, 0.0F);
    public static final UIColor black = new UIColor(0.0F, 0.0F, 0.0F, 1.0F);
    public static final UIColor white = new UIColor(1.0F, 1.0F, 1.0F, 1.0F);
    public static final UIColor gray = new UIColor(0.5F, 0.5F, 0.5F, 1.0F);
    public static final UIColor dark_gray = new UIColor(0.2F, 0.2F, 0.2F, 1.0F);
    public static final UIColor red = new UIColor(1.0F, 0.0F, 0.0F, 1.0F);
    public static final UIColor green = new UIColor(0.0F, 1.0F, 0.0F, 1.0F);
    public static final UIColor blue = new UIColor(0.0F, 0.0F, 1.0F, 1.0F);
    public static final UIColor cyan = new UIColor(0.0F, 0.5F, 1.0F, 1.0F);
    public static final UIColor yellow = new UIColor(1.0F, 1.0F, 0.0F, 1.0F);
    public static final UIColor orange = new UIColor(1.0F, 0.5F, 0.0F, 1.0F);
    public static final UIColor violet = new UIColor(1.0F, 0.0F, 1.0F, 1.0F);
    public static final UIColor brown = new UIColor(0.5F, 0.25F, 0.0F, 1.0F);

    public float r, g, b, a;

    public UIColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public UIColor(int rgba) {
        this.r = ((0xFF & rgba >> 16) / 255.0F);
        this.g = ((0xFF & rgba >> 8) / 255.0F);
        this.b = ((0xFF & rgba) / 255.0F);
        this.a = ((0xFF & rgba >> 24) / 255.0F);
    }

    public void set(int rgba) {
        this.r = ((0xFF & rgba >> 16) / 255.0F);
        this.g = ((0xFF & rgba >> 8) / 255.0F);
        this.b = ((0xFF & rgba) / 255.0F);
        this.a = ((0xFF & rgba >> 24) / 255.0F);
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int get() {
        return (int) (this.r * 255.0F) << 24 | (int) (this.g * 255.0F) << 16 | (int) (this.b * 255.0F) << 8 | (int) this.a * 255;
    }

    public UIColor mix(UIColor c) {
        return mix(c, 0.5F);
    }

    public UIColor mix(UIColor c, float percents) {
        return new UIColor(this.r * (1.0F - percents) + c.r * percents, this.g * (1.0F - percents) + c.g * percents, this.b * (1.0F - percents) + c.b * percents, this.a * (1.0F - percents) + c.a * percents);
    }

    public UIColor invert() {
        return new UIColor(1.0F - this.r, 1.0F - this.g, 1.0F - this.b, 1.0F - this.a);
    }

    public UIColor invert_no_alpha() {
        return new UIColor(1.0F - this.r, 1.0F - this.g, 1.0F - this.b, this.a);
    }
}
