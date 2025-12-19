package dev.flomik.uikitmc.scene.node;

import net.minecraft.client.gui.GuiGraphics;

public abstract class UiNode {
    protected int x, y;
    protected int width, height;
    protected boolean visible = true;

    protected UiNode(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public final boolean contains(int mx, int my) {
        return visible
                && mx >= x && mx < x + width
                && my >= y && my < y + height;
    }

    public abstract void render(GuiGraphics g);

    public int x() { return x; }
    public int y() { return y; }
    public int width() { return width; }
    public int height() { return height; }

    public void setPos(int x, int y) { this.x = x; this.y = y; }
    public void setSize(int w, int h) { this.width = w; this.height = h; }

    public boolean visible() { return visible; }
    public void setVisible(boolean v) { this.visible = v; }
}