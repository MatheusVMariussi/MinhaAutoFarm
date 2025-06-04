package com.minhafarm.hud;

import com.minhafarm.config.ConfigHandler;
import com.minhafarm.farming.FarmManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class FarmHUD {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (!ConfigHandler.enableFarmingHUD) {
            return;
        }

        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            FarmManager farmManager = FarmManager.getInstance();

            if (farmManager.isFarming()) {
                this.renderFarmingHUD(event.resolution);
            }
        }
    }

    private void renderFarmingHUD(ScaledResolution resolution) {
        FarmManager farmManager = FarmManager.getInstance();
        FontRenderer fr = mc.fontRendererObj;

        // Position HUD in the top right corner
        int right = resolution.getScaledWidth() - 5;
        int top = 5;

        // Background
        drawRect(right - 110, top, right, top + 52, new Color(0, 0, 0, 150).getRGB());

        // Title
        String title = "MinhaFarm";
        fr.drawStringWithShadow("§a" + title + "§r", right - 55 - fr.getStringWidth(title) / 2f, top + 5, 0xFFFFFF);

        // Status
        String status = "Status: " + (farmManager.isFarming() ? "§aActive" : "§cInactive");
        fr.drawStringWithShadow(status, right - 105, top + 17, 0xFFFFFF);

        // Direction
        String direction = "Direction: ";
        if (farmManager.isDirectionSet()) {
            direction += "§e" + farmManager.getCurrentDirection().substring(0, 1).toUpperCase() +
                    farmManager.getCurrentDirection().substring(1);
        } else {
            direction += "§7None";
        }
        fr.drawStringWithShadow(direction, right - 105, top + 27, 0xFFFFFF);

        // Selected Crop
        String crop = "Crop: ";
        if (farmManager.getSelectedCrop() != null) {
            crop += "§e" + farmManager.getSelectedCrop().getName();
        } else {
            crop += "§7None";
        }
        fr.drawStringWithShadow(crop, right - 105, top + 37, 0xFFFFFF);
    }

    private void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int temp = left;
            left = right;
            right = temp;
        }

        if (top < bottom) {
            int temp = top;
            top = bottom;
            bottom = temp;
        }

        float alpha = (float)(color >> 24 & 255) / 255.0F;
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;

        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        net.minecraft.client.renderer.GlStateManager.enableBlend();
        net.minecraft.client.renderer.GlStateManager.disableTexture2D();
        net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        net.minecraft.client.renderer.GlStateManager.color(red, green, blue, alpha);

        worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();

        net.minecraft.client.renderer.GlStateManager.enableTexture2D();
        net.minecraft.client.renderer.GlStateManager.disableBlend();
    }
}