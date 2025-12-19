package dev.flomik.uikitmc.scene.node;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class ImageNode extends UiNode {
    private final ResourceLocation texture;
    private int u, v;
    private int texW, texH;

    public ImageNode(ResourceLocation texture, int x, int y, int width, int height, int u, int v, int texW, int texH) {
        super(x, y, width, height);
        this.texture = texture;
        this.u = u; this.v = v;
        this.texW = texW; this.texH = texH;
    }

    @Override
    public void render(GuiGraphics g) {
        if (!visible) return;
        g.blit(texture, x, y, u, v, width, height, texW, texH);
    }

    public ResourceLocation texture() { return texture; }
    public int u() { return u; }
    public int v() { return v; }
    public int texW() { return texW; }
    public int texH() { return texH; }

    public void setUv(int u, int v) { this.u = u; this.v = v; }
    public void setTexSize(int w, int h) { this.texW = w; this.texH = h; }
}
