package com.nolimitenchants;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(nolimitenchants.MODID)
public class nolimitenchants {
    public static final String MODID = "nolimitenchants";
    public static final Logger LOGGER = LogUtils.getLogger();

    public nolimitenchants(IEventBus modEventBus, ModContainer modContainer) {
    }
}
