package com.fenyx.ui.components;

import com.fenyx.graphics.font.FenyxFont;
import com.fenyx.graphics.font.FontManager;

/**
 *
 * @author DarkPartizaN
 */
public class UIPanel extends UIComponent {

    public UIBackground background;
    public UIBorder border;
    public FenyxFont font = FontManager.getDefault(); //We need a valid font here

    public void setFont(FenyxFont font) {
        this.font = font;
    }

    public final FenyxFont getFont() {
        return font;
    }

    public void onIdle() {
    }

    public final void setBackground(UIBackground background) {
        background.setSize(width, height);

        if (this.background != null) remove(this.background);
        this.background = background;
        add(this.background);
    }

    public final UIBackground getBackground() {
        return background;
    }

    public final void tileBackground(boolean resize) {
        background.tiled = resize;
    }
}
