package com.timedust.damageIndicator.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class IndicatorBuilder {

    private final JavaPlugin plugin;
    private final Location baseLocation;
    private final double damage;

    private String defaultAttackTemplate = "<red>-<damage> ❤</red>";
    private String critAttackTemplate    = "<gold>-<damage> ❤</gold>";

    private long riseTicks   = 20L;  // сколько тиков идёт подъём
    private long fadeTicks   = 8L;   // сколько тиков занимает fade (несколько шагов)
    private long fadeDelay   = 4L;   // задержка перед стартом движения
    private float riseHeight = 0.8f; // высота подъёма в блоках (не слишком высоко)

    private boolean isCrit = false;
    private Color backgroundColor = Color.fromARGB(160, 20, 20, 20);
    private Display.Billboard billboard = Display.Billboard.CENTER;
    private float scale   = 1.0f;
    private boolean shadowed = false;

    public IndicatorBuilder(JavaPlugin plugin, Location location, double damage) {
        this.plugin        = plugin;
        this.baseLocation  = location.clone();
        this.damage        = damage;
    }

    public IndicatorBuilder(JavaPlugin plugin, LivingEntity victim, double damage) {
        this.plugin  = plugin;
        this.damage  = damage;

        Location loc = victim.getLocation().add(0, victim.getHeight() + 0.1, 0);
        double offsetX = (Math.random() - 0.5) * 0.7;
        double offsetZ = (Math.random() - 0.5) * 0.7;
        double offsetY = Math.random() * 0.3;
        this.baseLocation = loc.add(offsetX, offsetY, offsetZ);
    }


    public IndicatorBuilder defaultAttackTemplate(String miniMessageFormat) {
        this.defaultAttackTemplate = miniMessageFormat;
        return this;
    }

    public IndicatorBuilder critAttackTemplate(String miniMessageFormat) {
        this.critAttackTemplate = miniMessageFormat;
        return this;
    }

    public IndicatorBuilder riseTicks(long ticks) {
        this.riseTicks = ticks;
        return this;
    }

    public IndicatorBuilder fadeTicks(long ticks) {
        this.fadeTicks = ticks;
        return this;
    }

    public IndicatorBuilder riseHeight(float height) {
        this.riseHeight = height;
        return this;
    }

    public IndicatorBuilder isCrit(boolean isCrit) {
        this.isCrit = isCrit;
        return this;
    }

    public IndicatorBuilder backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public IndicatorBuilder billboard(Display.Billboard billboard) {
        this.billboard = billboard;
        return this;
    }

    public IndicatorBuilder scale(float scale) {
        this.scale = scale;
        return this;
    }

    public IndicatorBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }

    public TextDisplay spawn() {
        if (baseLocation.getWorld() == null) return null;

        double roundedDamage = Math.round(damage * 10.0) / 10.0;
        TagResolver damagePlaceholder = Placeholder.parsed("damage", String.valueOf(roundedDamage));
        String template = isCrit ? critAttackTemplate : defaultAttackTemplate;

        Component text = MiniMessage.miniMessage().deserialize(template, damagePlaceholder);
        if (isCrit) {
            text = text.decorate(TextDecoration.BOLD);
        }

        final Component finalText = text;

        TextDisplay indicator = baseLocation.getWorld().spawn(baseLocation, TextDisplay.class, entity -> {
            entity.text(finalText);
            entity.setBillboard(billboard);
            entity.setShadowed(shadowed);
            entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

            float startScale = scale * 0.4f;
            entity.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),
                    new Quaternionf(),
                    new Vector3f(startScale, startScale, startScale),
                    new Quaternionf()
            ));
        });

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!indicator.isValid()) return;
            indicator.setInterpolationDuration(3);
            indicator.setInterpolationDelay(0);
            indicator.setBackgroundColor(backgroundColor);
            indicator.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),
                    new Quaternionf(),
                    new Vector3f(scale, scale, scale),
                    new Quaternionf()
            ));
        }, 1L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!indicator.isValid()) return;
            indicator.setInterpolationDuration((int) riseTicks);
            indicator.setInterpolationDelay(0);
            indicator.setTransformation(new Transformation(
                    new Vector3f(0, riseHeight, 0),
                    new Quaternionf(),
                    new Vector3f(scale, scale, scale),
                    new Quaternionf()
            ));
        }, fadeDelay);

        int fadeSteps = 5;
        long fadeStart = fadeDelay + riseTicks;

        int bgA = backgroundColor.getAlpha();
        int bgR = backgroundColor.getRed();
        int bgG = backgroundColor.getGreen();
        int bgB = backgroundColor.getBlue();

        for (int i = 1; i <= fadeSteps; i++) {
            final int step = i;
            long stepDelay = fadeStart + (long) Math.ceil((double) fadeTicks * step / fadeSteps);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!indicator.isValid()) return;

                float progress = (float) step / fadeSteps;
                float alpha = 1.0f - progress;

                int newBgAlpha = (int) (bgA * alpha);
                indicator.setBackgroundColor(Color.fromARGB(newBgAlpha, bgR, bgG, bgB));

                indicator.setSeeThrough(step >= fadeSteps / 2);
            }, stepDelay);
        }

        long totalDuration = fadeStart + fadeTicks + 2L;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (indicator.isValid()) indicator.remove();
        }, totalDuration);

        return indicator;
    }
}