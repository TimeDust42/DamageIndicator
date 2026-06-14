package com.timedust.damageIndicator.api;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamageIndicatorAPI {

    private static JavaPlugin hostPlugin;
    private static boolean enabled = true;

    private DamageIndicatorAPI() {}

    public static void init(JavaPlugin plugin) {
        hostPlugin = plugin;
    }

    public static boolean isInitialized() {
        return hostPlugin != null;
    }
    public static void setEnabled(boolean value) {
        enabled = value;
    }

    /**
     * Создать индикатор у сущности.
     * Возвращает null если API не инициализировано или отключено.
     */
    public static IndicatorBuilder builder(LivingEntity victim, double damage) {
        if (!enabled || hostPlugin == null) return null;
        return new IndicatorBuilder(hostPlugin, victim, damage);
    }

    /**
     * Создать индикатор на конкретной локации.
     */
    public static IndicatorBuilder builder(Location location, double damage) {
        if (!enabled || hostPlugin == null) return null;
        return new IndicatorBuilder(hostPlugin, location, damage);
    }
}
