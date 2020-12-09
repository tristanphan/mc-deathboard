package com.tristanphan.DeathBoard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConstructTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player &&
                command.getName().equalsIgnoreCase("wi") &&
                args.length >= 1 ) {
            List<String> arguments = new ArrayList<>();
            if (sender.hasPermission("deathboard.disable")) {
                arguments.add("disable");
                arguments.add("enable");
            }
            if (sender.hasPermission("deathboard.reload")) {
                arguments.add("reload");
            }
            return arguments;
        }
        return null;
    }
}
