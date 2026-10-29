package dev.velmora.client;

import dev.velmora.client.gui.VelmoraScreen;
import dev.velmora.client.module.AutoSprint;
import dev.velmora.client.module.TriggerBot;
import dev.velmora.client.module.FakePlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VelmoraClient implements ClientModInitializer {
    public static final String MOD_ID = "velmora";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.velmora.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                KeyBinding.Category.create("category.velmora")
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen instanceof VelmoraScreen) {
                    client.setScreen(null);
                } else if (client.currentScreen == null) {
                    client.setScreen(new VelmoraScreen());
                }
            }
        });

        AutoSprint.register();
        TriggerBot.register();
        FakePlayer.register();
        LOGGER.info("Velmora Client initialized.");
    }
}
