package dev.velmora.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

/** Read-only diagnostics for common vanilla critical-hit conditions. */
public final class CritCheck {
    private CritCheck() {}

    public static String getStatus(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return "Нет игрока";
        if (player.isOnGround()) return "На земле";
        if (player.isTouchingWater() || player.isSubmergedInWater()) return "В воде";
        if (player.isClimbing()) return "На лестнице";
        if (player.hasVehicle()) return "На транспорте";
        if (player.getAbilities().flying) return "Полёт";
        if (player.getFallDistance() <= 0.0F) return "Нет падения";
        return "Падение: " + String.format(java.util.Locale.ROOT, "%.2f", player.getFallDistance());
    }
}
