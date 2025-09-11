package de.acranum.holograms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class hologramCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("holograms")) {
            }
        }
        return false;
    }
}
