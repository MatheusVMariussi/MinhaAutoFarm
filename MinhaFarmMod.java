package com.minhafarm;

import com.minhafarm.commands.FarmCommand;
import com.minhafarm.config.ConfigHandler;
import com.minhafarm.events.FarmKeybindListener;
import com.minhafarm.gui.PauseMenuButtonHandler;
import com.minhafarm.hud.FarmHUD;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = MinhaFarmMod.MODID,
        name = MinhaFarmMod.MODNAME,
        version = MinhaFarmMod.VERSION)
public class MinhaFarmMod {

    public static final String MODID = "minhaFarm";
    public static final String MODNAME = "Minha Farm 1.8.9";
    public static final String VERSION = "1.1.10";

    // Declare the instance variable
    @Mod.Instance(MinhaFarmMod.MODID)
    public static MinhaFarmMod instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Set the instance when the mod is initialized
        instance = this;
        ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Register commands
        ClientCommandHandler.instance.registerCommand(new FarmCommand());

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new FarmKeybindListener());
        MinecraftForge.EVENT_BUS.register(new FarmHUD());

        MinecraftForge.EVENT_BUS.register(new PauseMenuButtonHandler());
    }

    // Add a getter for the instance
    public static MinhaFarmMod getInstance() {
        return instance;
    }
}