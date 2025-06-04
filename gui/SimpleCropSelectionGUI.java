package com.minhafarm.gui;

import com.minhafarm.farming.Crop;
import com.minhafarm.farming.FarmManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.List;

public class SimpleCropSelectionGUI extends GuiScreen {

    private final FarmManager farmManager = FarmManager.getInstance();
    private GuiTextField nameField;
    private GuiTextField yawField;
    private GuiTextField pitchField;
    private Crop selectedCropForEdit = null;
    private boolean isAddingCrop = false;

    @Override
    public void initGui() {
        buttonList.clear();

        if (isAddingCrop) {
            // Input fields for adding/editing a crop
            nameField = new GuiTextField(0, fontRendererObj, width / 2 - 100, 80, 200, 20);
            yawField = new GuiTextField(1, fontRendererObj, width / 2 - 100, 110, 200, 20);
            pitchField = new GuiTextField(2, fontRendererObj, width / 2 - 100, 140, 200, 20);

            // Save and Cancel buttons
            buttonList.add(new GuiButton(0, width / 2 - 100, 170, 95, 20, "Save"));
            buttonList.add(new GuiButton(1, width / 2 + 5, 170, 95, 20, "Cancel"));

            // Pre-fill fields if editing a crop
            if (selectedCropForEdit != null) {
                nameField.setText(selectedCropForEdit.getName());
                yawField.setText(String.valueOf(selectedCropForEdit.getYaw()));
                pitchField.setText(String.valueOf(selectedCropForEdit.getPitch()));
            }
        } else {
            // Crop list
            List<Crop> crops = farmManager.getCrops();
            int y = 50;
            for (int i = 0; i < crops.size(); i++) {
                buttonList.add(new GuiButton(10 + i, width / 2 - 100, y, 150, 20, crops.get(i).getName()));
                buttonList.add(new GuiButton(100 + i, width / 2 + 60, y, 40, 20, "Edit"));
                y += 25;
            }

            // Add Crop and Close buttons
            buttonList.add(new GuiButton(200, 20, height - 30, 80, 20, "Add Crop"));
            buttonList.add(new GuiButton(201, width - 100, height - 30, 80, 20, "Close"));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw background
        this.drawDefaultBackground();

        if (isAddingCrop) {
            // Draw title for adding/editing a crop
            String title = selectedCropForEdit != null ? "Edit Crop" : "Add Crop";
            this.drawCenteredString(fontRendererObj, title, width / 2, 40, 0xFFFFFF);

            // Draw input fields
            nameField.drawTextBox();
            yawField.drawTextBox();
            pitchField.drawTextBox();

            // Draw labels for input fields
            this.drawString(fontRendererObj, "Name:", width / 2 - 100, 70, 0xFFFFFF);
            this.drawString(fontRendererObj, "Yaw:", width / 2 - 100, 100, 0xFFFFFF);
            this.drawString(fontRendererObj, "Pitch:", width / 2 - 100, 130, 0xFFFFFF);

            // Draw Save and Cancel buttons
            for (GuiButton button : buttonList) {
                if (button.id == 0 || button.id == 1) {
                    button.drawButton(mc, mouseX, mouseY);
                }
            }
        } else {
            // Draw title for crop selection
            String title = "Crop Selection";
            this.drawCenteredString(fontRendererObj, title, width / 2, 20, 0xFFFFFF);

            // Draw buttons
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 200) { // Add Crop
            isAddingCrop = true;
            selectedCropForEdit = null;
            initGui();
        } else if (button.id == 201) { // Close
            mc.displayGuiScreen(null);
        } else if (button.id >= 100) { // Edit buttons
            int index = button.id - 100;
            selectedCropForEdit = farmManager.getCrops().get(index);
            isAddingCrop = true;
            initGui();
        } else if (button.id >= 10) { // Select crop
            farmManager.setSelectedCrop(farmManager.getCrops().get(button.id - 10));
            mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rSelected crop: §e" + farmManager.getSelectedCrop().getName()));
        } else if (button.id == 0) { // Save
            String name = nameField.getText();
            float yaw = Float.parseFloat(yawField.getText());
            float pitch = Float.parseFloat(pitchField.getText());

            if (selectedCropForEdit != null) {
                // Update existing crop
                farmManager.updateCrop(farmManager.getCrops().indexOf(selectedCropForEdit), new Crop(name, yaw, pitch));
                mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rEdited crop: §e" + name));
            } else {
                // Add new crop
                farmManager.addCrop(new Crop(name, yaw, pitch));
                mc.thePlayer.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rAdded crop: §e" + name));
            }

            isAddingCrop = false;
            initGui();
        } else if (button.id == 1) { // Cancel
            isAddingCrop = false;
            selectedCropForEdit = null;
            initGui();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false; // Ensure the GUI doesn't pause the game
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (isAddingCrop) {
            // Handle keyboard input for text fields
            nameField.textboxKeyTyped(typedChar, keyCode);
            yawField.textboxKeyTyped(typedChar, keyCode);
            pitchField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isAddingCrop) {
            // Handle mouse clicks for text fields
            nameField.mouseClicked(mouseX, mouseY, mouseButton);
            yawField.mouseClicked(mouseX, mouseY, mouseButton);
            pitchField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}