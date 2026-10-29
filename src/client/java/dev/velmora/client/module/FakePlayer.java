package dev.velmora.client.module;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

/**
 * Local-only dummy for aim and critical-hit practice.
 * It is not a server-side player and cannot be used to test server damage rules.
 */
public final class FakePlayer {
    private static final int ENTITY_ID = -721104;
    private static OtherClientPlayerEntity dummy;
    private static boolean enabled;

    private FakePlayer() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(FakePlayer::tick);
    }

    private static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            remove();
            return;
        }
        if (!enabled) {
            remove();
            return;
        }
        if (dummy != null) return;

        remove();
        ClientWorld world = client.world;
        GameProfile profile = new GameProfile(UUID.fromString("d34dbeef-1234-4abc-8def-0123456789ab"), "VelmoraDummy");
        dummy = new OtherClientPlayerEntity(world, profile);
        dummy.setId(ENTITY_ID);
        dummy.copyPositionAndRotation(client.player);
        dummy.setYaw(client.player.getYaw());
        dummy.setHeadYaw(client.player.getHeadYaw());
        dummy.setBodyYaw(client.player.getBodyYaw());
        dummy.setHealth(dummy.getMaxHealth());
        dummy.setVelocity(0, 0, 0);
        dummy.setNoGravity(true);
        world.addEntity(dummy);
    }

    public static void setEnabled(boolean value) {
        enabled = value;
        if (!value) remove();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static void remove() {
        if (dummy != null) {
            dummy.discard();
            dummy = null;
        }
    }
}
