package dev.velmora.client.module;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Basic crosshair trigger for local testing. No aim correction or anti-cheat evasion.
 */
public final class TriggerBot {
    private static boolean enabled = false;
    private static boolean playersOnly = true;
    private static double range = 3.0;
    private static int delayTicks = 0;
    private static int ticksSinceAttack = 0;

    private TriggerBot() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(TriggerBot::tick);
    }

    private static void tick(MinecraftClient client) {
        if (!enabled || client.player == null || client.world == null
                || client.interactionManager == null || client.currentScreen != null) {
            return;
        }
        if (ticksSinceAttack < delayTicks) {
            ticksSinceAttack++;
            return;
        }
        if (!(client.crosshairTarget instanceof EntityHitResult hit)) return;
        if (!(hit.getEntity() instanceof LivingEntity target) || target == client.player) return;
        if (!target.isAlive() || client.player.distanceTo(target) > range) return;
        if (playersOnly && !(target instanceof PlayerEntity)) return;
        if (client.player.getAttackCooldownProgress(0.5F) < 1.0F) return;

        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
        ticksSinceAttack = 0;
    }

    public static boolean isEnabled() { return enabled; }
    public static void setEnabled(boolean value) { enabled = value; }
    public static boolean isPlayersOnly() { return playersOnly; }
    public static void setPlayersOnly(boolean value) { playersOnly = value; }
    public static double getRange() { return range; }
    public static void setRange(double value) { range = Math.max(1.0, Math.min(6.0, value)); }
    public static int getDelayTicks() { return delayTicks; }
    public static void setDelayTicks(int value) { delayTicks = Math.max(0, Math.min(20, value)); }
}
