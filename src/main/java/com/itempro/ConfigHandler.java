package com.itempro.config;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import java.io.File;
import java.util.List;

public class ConfigHandler implements IConfigHandler {
    public static final ConfigBoolean ModEnabled = new ConfigBoolean("ModEnabled", true, "Master switch for the mod.");
    public static final ConfigInteger TickDelay = new ConfigInteger("TickDelay", 2, 0, 20, "Delay between item operations (Ticks).");
    public static final ConfigBoolean AutoRefill = new ConfigBoolean("AutoRefill", true, "Auto move enchanted items to Grindstone.");
    public static final ConfigBoolean AutoDrop = new ConfigBoolean("AutoDrop", false, "Drop items immediately after disenchanting.");
    public static final ConfigBoolean StopIfInventoryFull = new ConfigBoolean("StopIfInventoryFull", true, "Stop when no empty slots in main inventory.");
    public static final ConfigInteger ButtonOffsetX = new ConfigInteger("ButtonOffsetX", 0, -100, 100, "X Offset for the GUI button.");
    public static final ConfigInteger ButtonOffsetY = new ConfigInteger("ButtonOffsetY", 0, -100, 100, "Y Offset for the GUI button.");

    public static final List<fi.dy.masa.malilib.config.options.IConfigBase> OPTIONS = List.of(
        ModEnabled, TickDelay, AutoRefill, AutoDrop, StopIfInventoryFull, ButtonOffsetX, ButtonOffsetY
    );

    public static void load() {
        // Note: Real maLiLib usage would use ConfigUtils.load(file, instance)
    }

    @Override
    public void save() {
        // Save implementation
    }

    // Metadata for Tweakeroo-style UI
    public static String getAuthorInfo() {
        return "Author: songquan";
    }
}