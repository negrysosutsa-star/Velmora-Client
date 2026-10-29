package dev.velmora.client.module;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

/**
 * Automatically sprints while the player is moving forward.
 * Does not change sprint state while a screen is open or the player is unavailable.
 */
public final class AutoSprint {
    private static boolean enabled = true;

    private AutoSprint() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(AutoSprint::tick);
    }

    private static void tick(MinecraftClient client) {
        if (!enabled || client.player == null || client.world == null || client.currentScreen != null) {
            return;
        }

        KeyBinding forward = client.options.forwardKey;
        boolean movingForward = forward.isPressed();
        boolean canSprint = movingForward
                && !client.player.isSneaking()
                && !client.player.isTouchingWater()
                && !client.player.isSubmergedInWater()
                && client.player.getHungerManager().getFoodLevel() > 6;

        if (canSprint && !client.player.isSprinting()) {
            client.player.setSprinting(true);
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }
}
