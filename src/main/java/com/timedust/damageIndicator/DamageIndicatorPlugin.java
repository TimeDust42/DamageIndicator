package com.timedust.damageIndicator;

import com.timedust.damageIndicator.api.DamageIndicatorAPI;
import com.timedust.damageIndicator.api.IndicatorBuilder;
import com.timedust.damageIndicator.commands.ReloadCommand;
import com.timedust.damageIndicator.config.IndicatorConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DamageIndicatorPlugin extends JavaPlugin implements DamageIndicatorAPI {

    private IndicatorConfig indicatorConfig;
    private boolean apiEnabled = true;

    @Override
    public void onEnable() {
        Bukkit.getServicesManager().register(
                DamageIndicatorAPI.class,
                this,
                this,
                ServicePriority.Normal
        );

        saveDefaultConfig();

        reloadPluginConfig();

        Objects.requireNonNull(getCommand("damageindicator")).setExecutor(new ReloadCommand(this));

        if (indicatorConfig.isSelfWorkingEnabled()) {
            getLogger().info("DamageIndicator запущен в самостоятельном режиме.");
        } else {
            getLogger().info("DamageIndicator запущен в режиме библиотеки.");
        }
    }

    public void reloadPluginConfig() {
        this.reloadConfig();

        this.indicatorConfig = new IndicatorConfig(this.getConfig());
    }

    @Override
    public @Nullable IndicatorBuilder builder(@NotNull LivingEntity victim, double damage) {
        if (!apiEnabled) return null;
        return new IndicatorBuilder(this, victim, damage);
    }

    @Override
    public @Nullable IndicatorBuilder builder(@NotNull Player damager, @NotNull LivingEntity victim, double damage) {
        if (!apiEnabled) return null;
        return new IndicatorBuilder(this, damager, victim, damage);
    }

    @Override
    public @Nullable IndicatorBuilder builder(@NotNull Location location, double damage) {
        if (!apiEnabled) return null;
        return new IndicatorBuilder(this, location, damage);
    }

    @Override
    public void setApiEnabled(boolean enabled) {
        this.apiEnabled = enabled;
    }

    @Override
    public boolean isApiEnabled() {
        return this.apiEnabled;
    }

    public IndicatorConfig getIndicatorConfig() {
        return indicatorConfig;
    }
}