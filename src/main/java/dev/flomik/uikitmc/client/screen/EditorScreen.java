package dev.flomik.uikitmc.client.screen;

import dev.flomik.uikitmc.scene.Scene;
import dev.flomik.uikitmc.scene.node.ImageNode;
import dev.flomik.uikitmc.scene.node.UiNode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class EditorScreen extends Screen {

    private final Scene scene = new Scene();

    private UiNode selected = null;
    private boolean dragging = false;
    private int dragOffX, dragOffY;

    // “Холст” (в будущем: zoom/pan; пока просто рамка)
    private int canvasX, canvasY, canvasW, canvasH;

    public EditorScreen() {
        super(Component.literal("UIKitMC Editor"));
    }

    @Override
    protected void init() {
        super.init();

        // Холст = безопасная область минус панели (пока условно)
        canvasX = 40;
        canvasY = 30;
        canvasW = this.width - 40 - 220;   // справа место под инспектор
        canvasH = this.height - 30 - 30;

        // DEMO: добавляем одну текстуру (ванильные widgets) чтобы проверить drag/blit сразу
        // Можно заменить на свою: new ResourceLocation("uikitmc", "textures/...")
        ResourceLocation widgets = new ResourceLocation("minecraft", "textures/gui/widgets.png");

        // Возьмём кусок текстуры как “кнопку”
        scene.add(new ImageNode(
                widgets,
                canvasX + 60, canvasY + 60,
                200, 20,       // на экране
                0, 66,         // UV в widgets.png
                256, 256       // размер текстуры
        ));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // тёмный фон
        this.renderBackground(g);

        // рамка холста
        g.fill(canvasX - 1, canvasY - 1, canvasX + canvasW + 1, canvasY + canvasH + 1, 0xFF1E1E1E);
        g.fill(canvasX, canvasY, canvasX + canvasW, canvasY + canvasH, 0xFF0E0E10);

        // рендер сцены
        for (UiNode node : scene.nodes()) {
            node.render(g);
        }

        // рамка выделения
        if (selected != null) {
            int x = selected.x(), y = selected.y(), w = selected.width(), h = selected.height();
            // белая рамка 1px
            g.fill(x - 1, y - 1, x + w + 1, y, 0xFFFFFFFF);         // top
            g.fill(x - 1, y + h, x + w + 1, y + h + 1, 0xFFFFFFFF); // bottom
            g.fill(x - 1, y, x, y + h, 0xFFFFFFFF);                 // left
            g.fill(x + w, y, x + w + 1, y + h, 0xFFFFFFFF);         // right
        }

        // правая панель инспектора
        int panelX = this.width - 210;
        g.fill(panelX, 0, this.width, this.height, 0xFF121215);
        g.drawString(this.font, "INSPECTOR", panelX + 12, 12, 0xFFFFFFFF, false);

        if (selected != null) {
            g.drawString(this.font, "x: " + selected.x(), panelX + 12, 36, 0xFFBDBDBD, false);
            g.drawString(this.font, "y: " + selected.y(), panelX + 12, 48, 0xFFBDBDBD, false);
            g.drawString(this.font, "w: " + selected.width(), panelX + 12, 60, 0xFFBDBDBD, false);
            g.drawString(this.font, "h: " + selected.height(), panelX + 12, 72, 0xFFBDBDBD, false);
        } else {
            g.drawString(this.font, "(no selection)", panelX + 12, 36, 0xFF6E6E6E, false);
        }

        // подсказка хоткеев
        g.drawString(this.font, "LMB: select/drag | ESC: close", 10, this.height - 18, 0xFF9A9A9A, false);

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return super.mouseClicked(mx, my, button);

        // клик только внутри холста
        if (!insideCanvas((int) mx, (int) my)) {
            selected = null;
            return true;
        }

        UiNode hit = pickTopmost((int) mx, (int) my);
        selected = hit;

        if (selected != null) {
            dragging = true;
            dragOffX = (int) mx - selected.x();
            dragOffY = (int) my - selected.y();
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (button != 0) return super.mouseDragged(mx, my, button, dx, dy);

        if (dragging && selected != null) {
            int nx = (int) mx - dragOffX;
            int ny = (int) my - dragOffY;

            nx = clamp(nx, canvasX, canvasX + canvasW - selected.width());
            ny = clamp(ny, canvasY, canvasY + canvasH - selected.height());

            selected.setPos(nx, ny);
            return true;
        }

        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (button == 0) {
            dragging = false;
        }
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // ESC закрыть (обычно так и работает, но оставим явно)
        if (keyCode == 256) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean insideCanvas(int x, int y) {
        return x >= canvasX && x < canvasX + canvasW && y >= canvasY && y < canvasY + canvasH;
    }

    private UiNode pickTopmost(int mx, int my) {
        List<UiNode> nodes = scene.nodes();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            UiNode n = nodes.get(i);
            if (n.contains(mx, my)) return n;
        }
        return null;
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
