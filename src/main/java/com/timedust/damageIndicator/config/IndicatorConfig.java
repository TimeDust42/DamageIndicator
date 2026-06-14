package com.timedust.damageIndicator.config;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;

public class IndicatorConfig {

    private final boolean standaloneListenerEnabled;
    private final String defaultTemplate;
    private final String critTemplate;
    private final long riseTicks;
    private final long fadeTicks;
    private final float riseHeight;
    private final float scale;
    private final boolean shadowed;
    private final Color backgroundColor;

    public IndicatorConfig(FileConfiguration config) {
        this.standaloneListenerEnabled = config.getBoolean("standalone-listener", true);
        this.defaultTemplate = config.getString("templates.default", "<red>-<damage> ❤</red>");
        this.critTemplate = config.getString("templates.crit",    "<gold>✦ -<damage> ❤</gold>");
        this.riseTicks = config.getLong("animation.rise-ticks",   22L);
        this.fadeTicks = config.getLong("animation.fade-ticks",    7L);
        this.riseHeight = (float) config.getDouble("animation.rise-height", 0.7);
        this.scale = (float) config.getDouble("display.scale", 1.0);
        this.shadowed = config.getBoolean("display.shadowed", false);

        int a = config.getInt("display.background-color.a", 160);
        int r = config.getInt("display.background-color.r", 20);
        int g = config.getInt("display.background-color.g", 20);
        int b = config.getInt("display.background-color.b", 20);
        this.backgroundColor = Color.fromARGB(a, r, g, b);
    }

    // getters
    public boolean isStandaloneListenerEnabled() { return standaloneListenerEnabled; }
    public String getDefaultTemplate() { return defaultTemplate; }
    public String getCritTemplate() { return critTemplate; }
    public long getRiseTicks() { return riseTicks; }
    public long getFadeTicks() { return fadeTicks; }
    public float getRiseHeight() { return riseHeight; }
    public float getScale() { return scale; }
    public boolean isShadowed() { return shadowed; }
    public Color getBackgroundColor() { return backgroundColor; }
}