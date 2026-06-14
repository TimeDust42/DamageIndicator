package com.timedust.damageIndicator.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class IndicatorHook {

    private DamageIndicatorAPI api;

    public boolean init() {
        if (Bukkit.getPluginManager().getPlugin("DamageIndicator") == null) {
            return false;
        }

        RegisteredServiceProvider<DamageIndicatorAPI> provider =
                Bukkit.getServicesManager().getRegistration(DamageIndicatorAPI.class);

        if (provider != null) {
            this.api = provider.getProvider();
            return true;
        }

        return false;
    }

    public boolean isAvailable() {
        return api != null && api.isApiEnabled();
    }
    public DamageIndicatorAPI getAPI() {
        return api;
    }

}
