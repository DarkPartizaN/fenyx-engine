package com.fenyx.ui;

import com.fenyx.core.Input;
import java.util.ArrayList;

/**
 *
 * @author DarkPartizaN
 */
public abstract class UI {

    protected int x, y, width, height;
    protected int clip_x, clip_y, clip_w, clip_h;
    protected boolean visible, active, enabled, focused, was_focused;

    //Event system
    private long last_idle, idle_delay;
    private long last_event, event_delay;
    private long remove_delay, remove_start;
    private boolean should_remove = false;

    //Parent system
    private UI parent;
    private final ArrayList<UI> elements;

    public UI() {
        visible = true;
        active = true;
        enabled = true;

        elements = new ArrayList<>();

        last_event = System.currentTimeMillis();
        last_idle = System.currentTimeMillis();
    }

    public abstract void onShow();
    public abstract void onEnable();
    public abstract void onIdle();
    public abstract void onMove();
    public abstract void onResize();
    public abstract void onFocus();
    public abstract void onFocusLost();
    public abstract void onMouseMove();
    public abstract void onClick();
    public abstract void onKeyPressed();
    public abstract void onKeyReleased();
    public abstract void onDraw();
    public abstract void onDisable();
    public abstract void onHide();
    public abstract void onDestroy();

    public final void add(UI ui) {
        if (elements.contains(ui)) return;

        if (ui.hasParent())
            ui.setPosition(ui.x - ui.getParent().x, ui.y - ui.getParent().y);

        ui.parent = this;
        ui.setPosition(this.x + ui.x, this.y + ui.y);

        elements.add(ui);
    }

    public final void remove(UI ui) {
        if (elements.isEmpty()) return;
        if (!elements.contains(ui)) return;

        elements.remove(ui);

        if (ui.parent == this) {
            ui.setPosition(ui.x - this.x, ui.y - this.y);
            ui.parent = null;
        }
    }

    public final void removeLately(UI ui, int delay) {
        ui.remove_start = System.currentTimeMillis();
        ui.remove_delay = delay;
        ui.should_remove = true;
    }

    public final void setPosition(int x, int y) {
        int delta_x = x - getX();
        int delta_y = y - getY();

        this.x += delta_x;
        this.y += delta_y;

        onMove();

        elements.forEach((e) -> {
            e.setPosition(e.getX() + delta_x, e.getY() + delta_y);
        });
    }

    public final void centerElement() {
        if (parent == null)
            setPosition(UIManager.UI_SCREEN_WIDTH / 2 - width / 2, UIManager.UI_SCREEN_HEIGHT / 2 - height / 2);
        else
            setPosition(parent.x + parent.width / 2 - width / 2, parent.y + parent.height / 2 - height / 2);
    }

    public final void setX(int x) {
        if (!hasParent()) {
            setPosition(x, y);
        } else {
            setPosition(parent.x + x, y);
        }
    }

    public final void setY(int y) {
        if (!hasParent()) {
            setPosition(x, y);
        } else {
            setPosition(x, parent.y + y);
        }
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        onResize();
    }

    public final void setWidth(int width) {
        setSize(width, this.height);
    }

    public final void setHeight(int height) {
        setSize(this.width, height);
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final int getBoundWidth() {
        return x + width;
    }

    public final int getBoundHeight() {
        return y + height;
    }

    protected void checkClipBounds() {
        if (hasParent()) {
            if (this.clip_x < getParent().x) {
                this.clip_x = getParent().x;
                this.clip_w -= getParent().x - this.x;
            }
            if (this.clip_y < getParent().y) {
                this.clip_y = getParent().y;
                this.clip_h -= getParent().y - this.y;
            }
            if (this.x + this.clip_w > getParent().getBoundWidth())
                this.clip_w = (getParent().getBoundWidth() - this.x);
            if (this.y + this.clip_h > getParent().getBoundHeight()) {
                this.clip_h = (getParent().getBoundHeight() - this.y);
            }
        } else {
            clip_x = x;
            clip_y = y;
            clip_w = width;
            clip_h = height;
        }
    }

    public final boolean hasParent() {
        return parent != null;
    }

    public final UI getParent() {
        return parent;
    }

    public ArrayList<UI> getElements() {
        return elements;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;

        if (visible) {
            onShow();
        } else {
            onHide();
        }
    }

    public final boolean isVisible() {
        return visible;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setActive(boolean active) {
        this.active = active;
    }

    public final boolean isActive() {
        return active;
    }

    public final boolean isFocused() {
        return focused;
    }

    public boolean canEvent() {
        if (hasParent() && (!parent.isActive() || !parent.isVisible())) return false;
        if (!isActive() || !isVisible()) return false;

        if (System.currentTimeMillis() - last_event > event_delay) {
            last_event = System.currentTimeMillis();

            return true;
        }

        return false;
    }

    public final void setEventDelay(int delay) {
        event_delay = delay;
    }

    public final void setIdleDelay(int delay) {
        idle_delay = delay;
    }

    private boolean canIdle() {
        if (System.currentTimeMillis() - last_idle > idle_delay) {
            last_idle = System.currentTimeMillis();

            return true;
        }

        return false;
    }

    public final void update() {
        if (canIdle()) onIdle();

        focused = Input.mouseInRect(x, y, width, height);

        if (canEvent()) {
            if (focused) {
                if (Input.isKeyPressed(0)) {
                    onClick();
                }
                if (!was_focused) {
                    was_focused = true;
                    onFocus();
                }
            } else if (was_focused) {
                was_focused = false;
                onFocusLost();
            }

            if (Input.isKeyPressed(Input.KEY_ANY))
                onKeyPressed();
            if (Input.mouseMoved())
                onMouseMove();
        }

        this.elements.forEach((e) -> {
            if (!e.should_remove)
                e.update();
            else if (System.currentTimeMillis() == e.remove_start + e.remove_delay) //Remove lately hook
                remove(e);
        });
    }
}
