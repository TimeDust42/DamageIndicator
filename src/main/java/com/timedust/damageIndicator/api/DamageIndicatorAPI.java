package com.timedust.damageIndicator.api;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DamageIndicatorAPI {

    /**
     * Создает билдер для индикатора урона на основе сущности-жертвы.
     * Индикатор будет виден всем игрокам.
     */
    @Nullable
    IndicatorBuilder builder(@NotNull LivingEntity victim, double damage);

    /**
     * Создает билдер для индикатора урона, привязанного к атакующему игроку.
     * Индикатор будет скрыт от всех, кроме этого игрока.
     */
    @Nullable
    IndicatorBuilder builder(@NotNull Player damager, @NotNull LivingEntity victim, double damage);

    /**
     * Создает билдер для индикатора урона на определенной локации.
     */
    @Nullable
    IndicatorBuilder builder(@NotNull Location location, double damage);

    /**
     * Включить или выключить отображение индикаторов через API.
     */
    void setApiEnabled(boolean enabled);

    /**
     * Проверить, включено ли API в данный момент.
     */
    boolean isApiEnabled();
}