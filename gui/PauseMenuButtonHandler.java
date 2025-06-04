package com.minhafarm.gui;

import com.minhafarm.gui.SimpleCropSelectionGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PauseMenuButtonHandler {

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        // Check if the current screen is the pause menu
        if (event.gui instanceof GuiIngameMenu) {
            // Add a custom button to the pause menu (bottom left)
            int buttonWidth = 100;
            int buttonHeight = 20;
            int buttonX = 10; // Left side
            int buttonY = event.gui.height - 30; // Bottom

            GuiButton cropButton = new GuiButton(100, buttonX, buttonY, buttonWidth, buttonHeight, "Crop Selection");
            event.buttonList.add(cropButton);
        }
    }

    @SubscribeEvent
    public void onButtonClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
        // Check if the button clicked is our custom button
        if (event.gui instanceof GuiIngameMenu && event.button.id == 100) {
            // Open the crop selection GUI
            Minecraft.getMinecraft().displayGuiScreen(new SimpleCropSelectionGUI());
        }
    }
}