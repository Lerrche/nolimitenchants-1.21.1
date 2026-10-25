package com.nolimitenchants.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.nolimitenchants.nolimitenchants;

import net.neoforged.fml.loading.FMLPaths;

/** Stores the universal enchantment-level cap in config/nolimitenchants.properties. */
public final class MaxLevelConfig {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 255;
    public static final int DEFAULT_LEVEL = 15;

    private static final String CONFIG_FILE = "nolimitenchants.properties";
    private static volatile int maxLevel = DEFAULT_LEVEL;

    private MaxLevelConfig() {
    }

    public static int getMaxLevel() {
        return maxLevel;
    }

    public static synchronized void load() {
        maxLevel = DEFAULT_LEVEL;
        Path file = getConfigPath();
        if (!Files.exists(file)) {
            save();
            return;
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(file)) {
            properties.load(reader);
            String value = properties.getProperty("maxEnchantmentLevel");
            if (value != null) {
                maxLevel = clamp(Integer.parseInt(value.trim()));
            }
        } catch (IOException | NumberFormatException exception) {
            nolimitenchants.LOGGER.warn("Could not load {}. Using the default enchantment level cap of {}.", CONFIG_FILE, DEFAULT_LEVEL, exception);
            maxLevel = DEFAULT_LEVEL;
        }
    }

    public static synchronized boolean setMaxLevel(int level) {
        maxLevel = clamp(level);
        return save();
    }

    private static boolean save() {
        Path file = getConfigPath();
        Properties properties = new Properties();
        properties.setProperty("maxEnchantmentLevel", Integer.toString(maxLevel));

        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file)) {
                properties.store(writer, "No Limit Enchants settings");
            }
            return true;
        } catch (IOException exception) {
            nolimitenchants.LOGGER.error("Could not save {}.", CONFIG_FILE, exception);
            return false;
        }
    }

    /** Applies the server-selected value on a client without writing over the client's local config. */
    public static void setSyncedMaxLevel(int level) {
        maxLevel = clamp(level);
    }

    private static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE);
    }

    private static int clamp(int level) {
        return Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, level));
    }
}
