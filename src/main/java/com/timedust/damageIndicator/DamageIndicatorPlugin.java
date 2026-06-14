package com.timedust.damageIndicator;

import com.timedust.damageIndicator.api.DamageIndicatorAPI;
import com.timedust.damageIndicator.config.IndicatorConfig;
import com.timedust.damageIndicator.listeners.DamageListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageIndicatorPlugin extends JavaPlugin {

    private static DamageIndicatorPlugin instance;
    private IndicatorConfig indicatorConfig;

    @Override
    public void onEnable() {
        instance = this;

        DamageIndicatorAPI.init(this);

        saveDefaultConfig();
        indicatorConfig = new IndicatorConfig(getConfig());

        if (indicatorConfig.isSelfWorkingEnabled()) {
            getServer().getPluginManager().registerEvents(
                    new DamageListener(indicatorConfig), this
            );
            getLogger().info("DamageIndicator запущен в самостоятельном режиме.");
        } else {
            getLogger().info("DamageIndicator запущен в режиме библиотеки.");
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static DamageIndicatorPlugin getInstance() {
        return instance;
    }

    public IndicatorConfig getIndicatorConfig() {
        return indicatorConfig;
    }
}