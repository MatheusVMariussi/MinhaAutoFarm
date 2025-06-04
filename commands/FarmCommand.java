package com.minhafarm.commands;

import com.minhafarm.MinhaFarmMod;
import com.minhafarm.farming.FarmManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class FarmCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "minhafarm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName() + " [toggle|status|help|crops]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "toggle":
                    FarmManager.getInstance().toggleFarming();
                    sender.addChatMessage(new ChatComponentText(
                            FarmManager.getInstance().isFarming()
                                    ? "§a[MinhaFarm] §rFarming mode §aENABLED§r. Press a direction key to start farming."
                                    : "§a[MinhaFarm] §rFarming mode §cDISABLED§r."
                    ));
                    break;

                case "status":
                    boolean active = FarmManager.getInstance().isFarming();
                    String direction = FarmManager.getInstance().getCurrentDirection();
                    sender.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rStatus:"));
                    sender.addChatMessage(new ChatComponentText(" - Farming Mode: " + (active ? "§aActive" : "§cInactive")));
                    if (active) {
                        sender.addChatMessage(new ChatComponentText(" - Direction: §e" + direction));
                    }
                    break;

                case "help":
                default:
                    sender.addChatMessage(new ChatComponentText("§a[MinhaFarm] §rCommands:"));
                    sender.addChatMessage(new ChatComponentText(" - /minhafarm toggle - Enable/disable farming mode"));
                    sender.addChatMessage(new ChatComponentText(" - /minhafarm status - Display current farming status"));
                    sender.addChatMessage(new ChatComponentText(" - /minhafarm help - Show this help message"));
                    break;
            }
        } else {
            processCommand(sender, new String[]{"help"});
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        final String[] options = new String[]{"toggle", "status", "help"};
        return getListOfStringsMatchingLastWord(args, options);
    }
}