package dev.velmora.client.gui;

import dev.velmora.client.module.AutoSprint;
import dev.velmora.client.module.CritCheck;
import dev.velmora.client.module.TriggerBot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** Original dark-purple client menu with movement and combat diagnostics. */
public final class VelmoraScreen extends Screen {
    private static final int BG = 0xF20A0814;
    private static final int PANEL = 0xF2120D20;
    private static final int PANEL_ALT = 0xF21A102B;
    private static final int PURPLE = 0xFFB45CFF;
    private static final int PURPLE_DARK = 0xFF6F2AB7;
    private static final int TEXT = 0xFFF5EDFF;
    private static final int MUTED = 0xFFB8A9C9;
    private boolean combatTab = false;

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
        context.fill(x, y, x + w, y + 76, PANEL);
        context.fill(x, y + 74, x + w, y + 76, PURPLE_DARK);
        context.drawTextWithShadow(this.textRenderer, "V", x + 22, y + 18, PURPLE);
        context.drawTextWithShadow(this.textRenderer, "VELMORA", x + 44, y + 15, TEXT);
        context.drawTextWithShadow(this.textRenderer, "CLIENT  /  1.21.11", x + 44, y + 32, MUTED);
        context.drawTextWithShadow(this.textRenderer, "INSERT  •  CLOSE", x + w - 135, y + 28, MUTED);

        int sidebarW = Math.min(158, w / 3);
        context.fill(x + 10, y + 88, x + sidebarW, y + h - 12, PANEL);
        context.fill(x + 20, y + (combatTab ? 143 : 105), x + sidebarW - 10, y + (combatTab ? 168 : 130), PURPLE_DARK);
        context.drawTextWithShadow(this.textRenderer, "⌂  Главная", x + 30, y + 113, combatTab ? MUTED : TEXT);
        context.drawTextWithShadow(this.textRenderer, "⚔  Combat", x + 30, y + 151, combatTab ? TEXT : MUTED);
        context.drawTextWithShadow(this.textRenderer, "◈  Movement", x + 30, y + 181, PURPLE);
        context.drawTextWithShadow(this.textRenderer, "◉  Render", x + 30, y + 211, MUTED);
        context.drawTextWithShadow(this.textRenderer, "⚙  Settings", x + 30, y + 241, MUTED);

        int contentX = x + sidebarW + 22;
        int contentW = w - sidebarW - 34;
        context.drawTextWithShadow(this.textRenderer, combatTab ? "COMBAT / DIAGNOSTICS" : "MOVEMENT", contentX, y + 94, PURPLE);
        context.drawTextWithShadow(this.textRenderer, combatTab ? "Состояние игрока и соединения" : "Твои настройки. Твой стиль.", contentX, y + 110, MUTED);

        if (!combatTab) {
            drawCard(context, contentX, y + 143, contentW, 92, "Auto Sprint",
                    "Автоматический спринт при движении вперёд.");
            drawToggle(context, contentX + contentW - 72, y + 157, AutoSprint.isEnabled());
            context.drawTextWithShadow(this.textRenderer, AutoSprint.isEnabled() ? "ON" : "OFF",
                    contentX + contentW - 72, y + 186, AutoSprint.isEnabled() ? PURPLE : MUTED);
            context.fill(contentX, y + 248, contentX + contentW, y + 311, PANEL);
            context.drawTextWithShadow(this.textRenderer, "Velmora Client", contentX + 14, y + 261, TEXT);
            context.drawTextWithShadow(this.textRenderer, "Выбери Combat слева для диагностики.", contentX + 14, y + 281, MUTED);
        } else {
            MinecraftClient client = MinecraftClient.getInstance();
            drawCard(context, contentX, y + 143, contentW, 62, "Crit Check",
                    "Состояние: " + CritCheck.getStatus(client));
            drawCard(context, contentX, y + 216, contentW, 62, "Server TPS",
                    "TPS: нет достоверных данных от сервера");
            drawCard(context, contentX, y + 289, contentW, 104, "Trigger Bot",
                    "Статус: " + (TriggerBot.isEnabled() ? "ВКЛ" : "ВЫКЛ") + "  •  клик по заголовку");
            context.drawTextWithShadow(this.textRenderer, "Режим: " + (TriggerBot.getHitMode() == TriggerBot.HitMode.CRITS_ONLY ? "Только криты" : "Криты + сплеш"),
                    contentX + 14, y + 325, PURPLE);
            context.drawTextWithShadow(this.textRenderer, "Не бить во время еды: " + (TriggerBot.isDoNotHitWhileEating() ? "ВКЛ" : "ВЫКЛ"),
                    contentX + 14, y + 341, MUTED);
            context.drawTextWithShadow(this.textRenderer, "Требовать меч: " + (TriggerBot.isRequireSword() ? "ВКЛ" : "ВЫКЛ"),
                    contentX + 14, y + 357, MUTED);
            context.drawTextWithShadow(this.textRenderer, "Дистанция: " + TriggerBot.getRange() + "  •  задержка: " + TriggerBot.getDelayTicks() + " тиков",
                    contentX + 14, y + 373, MUTED);
            context.drawTextWithShadow(this.textRenderer, "Только игроки: " + (TriggerBot.isPlayersOnly() ? "ДА" : "НЕТ"),
                    contentX + 14, y + 385, MUTED);
        }
        context.drawTextWithShadow(this.textRenderer, "VELMORA  •  CUSTOM CLIENT", x + 18, y + h - 25, MUTED);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawCard(DrawContext context, int x, int y, int w, int h, String title, String subtitle) {
        context.fill(x, y, x + w, y + h, PANEL_ALT);
        context.fill(x, y, x + 3, y + h, PURPLE);
        context.drawTextWithShadow(this.textRenderer, title, x + 14, y + 12, TEXT);
        context.drawTextWithShadow(this.textRenderer, subtitle, x + 14, y + 32, MUTED);
    }

    private void drawToggle(DrawContext context, int x, int y, boolean enabled) {
        context.fill(x, y, x + 56, y + 22, enabled ? PURPLE_DARK : 0xFF40364C);
        context.fill(x + (enabled ? 34 : 4), y + 4, x + (enabled ? 50 : 20), y + 18,
                enabled ? PURPLE : 0xFF9A8AA8);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        if (click.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
            int w = Math.min(760, this.width - 32);
            int h = Math.min(440, this.height - 32);
            int x = (this.width - w) / 2;
            int y = (this.height - h) / 2;
            int sidebarW = Math.min(158, w / 3);
            if (mouseX >= x + 20 && mouseX <= x + sidebarW - 10) {
                if (mouseY >= y + 138 && mouseY <= y + 173) {
                    combatTab = true;
                    return true;
                }
                if (mouseY >= y + 100 && mouseY <= y + 135) {
                    combatTab = false;
                    return true;
                }
            }
            int contentX = x + sidebarW + 22;
            int contentW = w - sidebarW - 34;
            int cardY = y + 143;
            if (!combatTab && mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= cardY && mouseY <= cardY + 92) {
                AutoSprint.setEnabled(!AutoSprint.isEnabled());
                return true;
            }
            if (combatTab && mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= y + 289 && mouseY <= y + 314) {
                TriggerBot.setEnabled(!TriggerBot.isEnabled());
                return true;
            }
            if (combatTab && mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= y + 316 && mouseY <= y + 336) {
                TriggerBot.cycleHitMode();
                return true;
            }
            if (combatTab && mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= y + 337 && mouseY <= y + 352) {
                TriggerBot.setDoNotHitWhileEating(!TriggerBot.isDoNotHitWhileEating());
                return true;
            }
            if (combatTab && mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= y + 353 && mouseY <= y + 368) {
                TriggerBot.setRequireSword(!TriggerBot.isRequireSword());
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
