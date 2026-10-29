package dev.velmora.client.module;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Crosshair trigger with optional sword/eating guards and vanilla-style hit timing.
 * Critical/sweep mode only chooses when to attempt an attack; the game/server still
 * determines whether a hit is a critical or sweep.
 */
public final class TriggerBot {
    public enum HitMode {
        CRITS_ONLY,
        CRIT_AND_SWEEP
    }

    private static boolean enabled = false;
    private static boolean playersOnly = true;
    private static boolean doNotHitWhileEating = true;
    private static boolean requireSword = true;
    private static double range = 3.0;
    private static int delayTicks = 0;
    private static int ticksSinceAttack = 0;
    private static HitMode hitMode = HitMode.CRIT_AND_SWEEP;

    private TriggerBot() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(TriggerBot::tick);
    }

    private static void tick(MinecraftClient client) {
        if (!enabled || client.player == null || client.world == null
                || client.interactionManager == null || client.currentScreen != null) {
            return;
        }

        PlayerEntity player = client.player;
        if (doNotHitWhileEating && player.isUsingItem() && player.getActiveItem().contains(DataComponentTypes.FOOD)) return;

        ItemStack held = player.getMainHandStack();
        if (requireSword && !(held.getItem() instanceof SwordItem)) return;

        if (ticksSinceAttack < delayTicks) {
            ticksSinceAttack++;
            return;
        }
        if (!(client.crosshairTarget instanceof EntityHitResult hit)) return;
        if (!(hit.getEntity() instanceof LivingEntity target) || target == player) return;
        if (!target.isAlive() || player.distanceTo(target) > range) return;
        if (playersOnly && !(target instanceof PlayerEntity)) return;
        if (player.getAttackCooldownProgress(0.5F) < 1.0F) return;

        boolean critWindow = !player.isOnGround()
                && player.fallDistance > 0.0F
                && !player.isTouchingWater()
                && !player.isSubmergedInWater()
                && !player.isClimbing()
                && !player.hasVehicle()
                && !player.getAbilities().flying;

        if (hitMode == HitMode.CRITS_ONLY && !critWindow) return;
        if (hitMode == HitMode.CRIT_AND_SWEEP && !critWindow) {
            // Ground hits can sweep only when the vanilla sweep conditions are met.
            // Avoid sprinting so the normal sword sweep mechanic can trigger.
            if (player.isSprinting()) return;
        }

        client.interactionManager.attackEntity(player, target);
        player.swingHand(Hand.MAIN_HAND);
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
    public static boolean isDoNotHitWhileEating() { return doNotHitWhileEating; }
    public static void setDoNotHitWhileEating(boolean value) { doNotHitWhileEating = value; }
    public static boolean isRequireSword() { return requireSword; }
    public static void setRequireSword(boolean value) { requireSword = value; }
    public static HitMode getHitMode() { return hitMode; }
    public static void cycleHitMode() {
        hitMode = hitMode == HitMode.CRITS_ONLY ? HitMode.CRIT_AND_SWEEP : HitMode.CRITS_ONLY;
    }
}
