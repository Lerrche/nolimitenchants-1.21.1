package com.nolimitenchants.network;

import com.nolimitenchants.config.MaxLevelConfig;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Handles synchronization of the current cap from the server to clients. */
public final class EnchantmentLimitNetworking {
    private EnchantmentLimitNetworking() {
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(MaxLevelSyncPayload.TYPE, MaxLevelSyncPayload.STREAM_CODEC,
                (payload, context) -> MaxLevelConfig.setSyncedMaxLevel(payload.maxLevel()));
    }

    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // In an integrated client, the client and logical server share a JVM. Reloading here
            // prevents a cap received from a previous multiplayer server leaking into a new world.
            MaxLevelConfig.load();
            PacketDistributor.sendToPlayer(player, new MaxLevelSyncPayload(MaxLevelConfig.getMaxLevel()));
        }
    }

    public static void syncToAllPlayers() {
        PacketDistributor.sendToAllPlayers(new MaxLevelSyncPayload(MaxLevelConfig.getMaxLevel()));
    }
}
