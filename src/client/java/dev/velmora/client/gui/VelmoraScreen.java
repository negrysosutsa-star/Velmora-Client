package dev.velmora.client.gui;

import dev.velmora.client.module.AutoSprint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Lightweight purple-themed client menu. Uses vanilla drawing APIs so it stays dependency-light.
 */
public final class VelmoraScreen extends Screen {
    private static final int BG = 0xF20A0814;
    private static final int PANEL = 0xF2120D20;
    private static final int PANEL_ALT = 0xF21A102B;
    private static final int PURPLE = 0xFFB45CFF;
    private static final int PURPLE_DARK = 0xFF6F2AB7;
    private static final int TEXT = 0xFFF5EDFF;
    private static final int MUTED = 0xFFB8A9C9;

    public VelmoraScreen() {
        super(Text.literal("Velmora Client"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int w = Math.min(760, this.width - 32);
        int h = Math.min(440, this.height - 32);
        int x = (this.width - w) / 2;
        int y = (this.height - h) / 2;

        context.fill(x - 2, y - 2, x + w + 2, y + h + 2, 0xAA8C39D8);
        context.fill(x, y, x + w, y + h, BG);

        // Header
        context.fill(x, y, x + w, y + 76, PANEL);
        context.fill(x, y + 74, x + w, y + 76, PURPLE_DARK);
        context.drawTextWithShadow(this.textRenderer, "V", x + 22, y + 18, PURPLE);
        context.drawTextWithShadow(this.textRenderer, "VELMORA", x + 44, y + 15, TEXT);
        context.drawTextWithShadow(this.textRenderer, "CLIENT  /  1.21.11", x + 44, y + 32, MUTED);
        context.drawTextWithShadow(this.textRenderer, "RIGHT SHIFT  •  CLOSE", x + w - 166, y + 28, MUTED);

        int sidebarW = Math.min(158, w / 3);
        context.fill(x + 10, y + 88, x + sidebarW, y + h - 12, PANEL);
        context.fill(x + 20, y + 105, x + sidebarW - 10, y + 130, PURPLE_DARK);
        context.drawTextWithShadow(this.textRenderer, "⌂  Главная", x + 30, y + 113, TEXT);
        context.drawTextWithShadow(this.textRenderer, "⚔  Combat", x + 30, y + 151, MUTED);
        context.drawTextWithShadow(this.textRenderer, "◈  Movement", x + 30, y + 181, PURPLE);
        context.drawTextWithShadow(this.textRenderer, "◉  Render", x + 30, y + 211, MUTED);
        context.drawTextWithShadow(this.textRenderer, "⚙  Settings", x + 30, y + 241, MUTED);

        int contentX = x + sidebarW + 22;
        int contentW = w - sidebarW - 34;
        context.drawTextWithShadow(this.textRenderer, "MOVEMENT", contentX, y + 94, PURPLE);
        context.drawTextWithShadow(this.textRenderer, "Твои настройки. Твой стиль.", contentX, y + 110, MUTED);

        int cardY = y + 143;
        context.fill(contentX, cardY, contentX + contentW, cardY + 92, PANEL_ALT);
        context.fill(contentX, cardY, contentX + 3, cardY + 92, PURPLE);
        context.drawTextWithShadow(this.textRenderer, "Auto Sprint", contentX + 15, cardY + 13, TEXT);
        context.drawTextWithShadow(this.textRenderer, "Автоматически включает спринт при движении вперёд.", contentX + 15, cardY + 32, MUTED);

        boolean active = AutoSprint.isEnabled();
        int bx = contentX + contentW - 72;
        int by = cardY + 14;
        context.fill(bx, by, bx + 56, by + 22, active ? PURPLE_DARK : 0xFF40364C);
        context.fill(bx + (active ? 34 : 4), by + 4, bx + (active ? 50 : 20), by + 18, active ? PURPLE : 0xFF9A8AA8);
        context.drawTextWithShadow(this.textRenderer, active ? "ON" : "OFF", bx, by + 29, active ? PURPLE : MUTED);

        context.fill(contentX, cardY + 105, contentX + contentW, cardY + 168, PANEL);
        context.drawTextWithShadow(this.textRenderer, "Velmora Client", contentX + 14, cardY + 118, TEXT);
        context.drawTextWithShadow(this.textRenderer, "Нажми на карточку Auto Sprint, чтобы переключить функцию.", contentX + 14, cardY + 138, MUTED);

        context.drawTextWithShadow(this.textRenderer, "VELMORA  •  CUSTOM CLIENT", x + 18, y + h - 25, MUTED);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            int w = Math.min(760, this.width - 32);
            int h = Math.min(440, this.height - 32);
            int x = (this.width - w) / 2;
            int y = (this.height - h) / 2;
            int sidebarW = Math.min(158, w / 3);
            int contentX = x + sidebarW + 22;
            int contentW = w - sidebarW - 34;
            int cardY = y + 143;
            if (mouseX >= contentX && mouseX <= contentX + contentW && mouseY >= cardY && mouseY <= cardY + 92) {
                AutoSprint.setEnabled(!AutoSprint.isEnabled());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
