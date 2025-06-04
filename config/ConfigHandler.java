package com.minhafarm.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {

    private static Configuration config;

    public static boolean enableFarmingHUD;
    public static int farmingKeyToggle;
    public static float defaultSensitivity;
    public static float farmingSensitivity;


    public static void loadConfig(File file) {
        config = new Configuration(file);
        config.load();

        enableFarmingHUD = config.get("General", "enableFarmingHUD", true,
                "Enable or disable the farming HUD display").getBoolean();

        farmingKeyToggle = config.get("Controls", "farmingKeyToggle", 34,
                "Key to toggle farming mode (Default: G)").getInt();

        defaultSensitivity = (float) config.get("Sensitivity", "defaultSensitivity", 0.5,
                "Default mouse sensitivity (will be restored when farming mode is disabled)").getDouble();

        farmingSensitivity = (float) config.get("Sensitivity", "farmingSensitivity", 0.1,
                "Sensitivity used while farming mode is active").getDouble();
    }

    public static void updateDefaultSensitivity(float sensitivity) {
        defaultSensitivity = sensitivity;
    }
}