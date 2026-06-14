package com.timedust.damageIndicator.commands;

import com.timedust.damageIndicator.DamageIndicatorPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    private final DamageIndicatorPlugin plugin;

    public ReloadCommand(DamageIndicatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("damageindicator.reload")) {
                sender.sendMessage(Component.text("You do not have permission to use this command").color(NamedTextColor.RED));
                return true;
            }
            plugin.reloadPluginConfig();
            sender.sendMessage(Component.text("Reload complete").color(NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("Usage: /damageIndicator reload").color(NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }

        return List.of();
    }
}
