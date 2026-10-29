package dev.velmora.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main client entrypoint for Velmora Client.
 * Feature modules will be added here as the project develops.
 */
public final class VelmoraClient implements ClientModInitializer {
    public static final String MOD_ID = "velmora";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Velmora Client initialized.");
    }
}
