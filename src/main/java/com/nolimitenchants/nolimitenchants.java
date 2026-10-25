package com.nolimitenchants;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.nolimitenchants.command.EnchantmentLimitCommand;
import com.nolimitenchants.config.MaxLevelConfig;
import com.nolimitenchants.network.EnchantmentLimitNetworking;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(nolimitenchants.MODID)
public class nolimitenchants {
    public static final String MODID = "nolimitenchants";
    public static final Logger LOGGER = LogUtils.getLogger();

    public nolimitenchants(IEventBus modEventBus, ModContainer modContainer) {
        MaxLevelConfig.load();
        modEventBus.addListener(EnchantmentLimitNetworking::registerPayloads);
        NeoForge.EVENT_BUS.addListener(EnchantmentLimitCommand::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(EnchantmentLimitNetworking::onPlayerLogin);
    }
}
