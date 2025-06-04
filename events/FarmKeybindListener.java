package com.minhafarm.events;

import com.minhafarm.config.ConfigHandler;
import com.minhafarm.farming.FarmManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class FarmKeybindListener {

    private final KeyBinding farmingToggleKey = new KeyBinding("Toggle Farming Mode", ConfigHandler.farmingKeyToggle, "MinhaFarm");
    private final Minecraft mc = Minecraft.getMinecraft();
    private float savedSensitivity = -1;

    public FarmKeybindListener() {
        ClientRegistry.registerKeyBinding(farmingToggleKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (farmingToggleKey.isPressed()) {
            toggleFarmingMode();
        }

        // Only check direction keys when farming mode is active
        if (FarmManager.getInstance().isFarming()) {
            checkDirectionKeys();
        }
    }

    private void toggleFarmingMode() {
        FarmManager farmManager = FarmManager.getInstance();
        boolean newState = !farmManager.isFarming();
        farmManager.setFarming(newState);

        if (newState) {
            // Save current sensitivity and set to farming sensitivity
            if (savedSensitivity < 0) {
                savedSensitivity = mc.gameSettings.mouseSensitivity;
                ConfigHandler.updateDefaultSensitivity(savedSensitivity);
            }
            mc.gameSettings.mouseSensitivity = ConfigHandler.farmingSensitivity;
            mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rFarming mode §aENABLED§r. Press a direction key to start farming."));
        } else {
            // Restore original sensitivity
            mc.gameSettings.mouseSensitivity = savedSensitivity > 0 ? savedSensitivity : ConfigHandler.defaultSensitivity;
            savedSensitivity = -1;
            mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rFarming mode §cDISABLED§r."));
        }
    }

    private void checkDirectionKeys() {
        FarmManager farmManager = FarmManager.getInstance();

        if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if (!farmManager.isDirectionSet() || !farmManager.getCurrentDirection().equals("left")) {
                farmManager.setDirection("left");
                mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rNow farming to the §eleft§r."));
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (!farmManager.isDirectionSet() || !farmManager.getCurrentDirection().equals("right")) {
                farmManager.setDirection("right");
                mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rNow farming to the §eright§r."));
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && mc.thePlayer != null && mc.theWorld != null) {
            FarmManager.getInstance().updateFarming();
        }
    }
}