package com.timedust.damageIndicator.listeners;

import com.timedust.damageIndicator.api.DamageIndicatorAPI;
import com.timedust.damageIndicator.api.IndicatorBuilder;
import com.timedust.damageIndicator.config.IndicatorConfig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private final IndicatorConfig config;

    public DamageListener(IndicatorConfig config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        double damage = event.getFinalDamage();
        if (damage < 0) return;

        boolean isCrit = event.isCritical();

        IndicatorBuilder builder = DamageIndicatorAPI.builder(victim, damage);
        if (builder == null) return;

        builder.defaultAttackTemplate(config.getDefaultTemplate())
                .critAttackTemplate(config.getCritTemplate())
                .isCrit(isCrit)
                .riseTicks(config.getRiseTicks())
                .fadeTicks(config.getFadeTicks())
                .riseHeight(config.getRiseHeight())
                .scale(config.getScale())
                .shadowed(config.isShadowed())
                .backgroundColor(config.getBackgroundColor())
                .spawn();
    }
}