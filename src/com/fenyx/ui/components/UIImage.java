package com.fenyx.ui.components;

import com.fenyx.graphics.AWTImage;
import com.fenyx.graphics.Texture;
import com.fenyx.graphics.TextureManager;

/**
 *
 * @author DarkPartizaN
 */
public class UIImage extends UIComponent {

    private Texture texture;

    public UIImage(AWTImage image) {
        texture = TextureManager.createTexture(image);
    }

    public UIImage(Texture image) {
        texture = image;
    }

    public void setImage(AWTImage image) {
        texture = TextureManager.createTexture(image);
    }

    public void setTexture(Texture image) {
        texture = image;
    }

    public Texture getTexture() {
        return texture;
    }
}