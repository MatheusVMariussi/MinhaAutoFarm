package com.minhafarm.farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class FarmManager {
    private static final FarmManager INSTANCE = new FarmManager();
    private final Minecraft mc = Minecraft.getMinecraft();

    private boolean farming = false;
    private String direction = null;
    private Crop selectedCrop = null;
    private final List<Crop> crops = new ArrayList<>(); // Store crops here

    private FarmManager() {
        // Add default crops (or load from config)
        crops.add(new Crop("Wheat", 0.0f, 0.0f));
        crops.add(new Crop("Carrot", 45.0f, 30.0f));
        crops.add(new Crop("Potato", -45.0f, 30.0f));
    }

    public static FarmManager getInstance() {
        return INSTANCE;
    }

    public boolean isFarming() {
        return farming;
    }

    public void setFarming(boolean farming) {
        this.farming = farming;
        if (!farming) {
            this.direction = null;
            // Release keys when farming is disabled
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        }
    }

    public void toggleFarming() {
        setFarming(!farming);
    }

    public boolean isDirectionSet() {
        return direction != null;
    }

    public String getCurrentDirection() {
        return direction != null ? direction : "none";
    }

    public void setDirection(String direction) {
        // Release any active movement keys first
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);

        this.direction = direction;
    }

    public void setSelectedCrop(Crop crop) {
        this.selectedCrop = crop;
    }

    public Crop getSelectedCrop() {
        return selectedCrop;
    }

    public List<Crop> getCrops() {
        return crops;
    }

    public void addCrop(Crop crop) {
        crops.add(crop);
    }

    public void updateCrop(int index, Crop crop) {
        if (index >= 0 && index < crops.size()) {
            crops.set(index, crop);
        }
    }

    public void updateCameraAngle() {
        if (selectedCrop != null && mc.thePlayer != null) {
            // Lock the camera to the selected crop's yaw and pitch
            mc.thePlayer.rotationYaw = selectedCrop.getYaw();
            mc.thePlayer.rotationPitch = selectedCrop.getPitch();
        }
    }

    public void updateFarming() {
        if (!farming || !isDirectionSet() || mc.thePlayer == null) {
            return;
        }

        // Lock the camera angle to the selected crop
        updateCameraAngle();

        // Attack key is always pressed when farming
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

        // Set movement key based on direction
        if ("left".equals(direction)) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        } else if ("right".equals(direction)) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
        }
    }
}