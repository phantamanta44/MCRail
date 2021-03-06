package io.github.phantamanta44.mcrail.command;

import io.github.phantamanta44.mcrail.Rail;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandItem implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        } else if (!sender.hasPermission("rail.item")) {
            sender.sendMessage(ChatColor.RED + "No permission!");
            return true;
        } else if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Must provide an item ID!");
            return true;
        } else if (!Rail.itemRegistry().exists(args[0])) {
            sender.sendMessage(ChatColor.RED + "Invalid item ID!");
            return true;
        }
        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount!");
                return true;
            }
        }
        ((Player)sender).getInventory().addItem(Rail.itemRegistry().create(args[0], amount));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1)
            return null;
        return Rail.itemRegistry().stream()
                .map(Map.Entry::getKey)
                .filter(n -> n.startsWith(args[0]))
                .collect(Collectors.toList());
    }

}
