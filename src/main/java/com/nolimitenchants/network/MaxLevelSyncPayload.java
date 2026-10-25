package com.nolimitenchants.network;

import com.nolimitenchants.nolimitenchants;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Server-authoritative max-level setting synchronized to clients. */
public record MaxLevelSyncPayload(int maxLevel) implements CustomPacketPayload {
    public static final Type<MaxLevelSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(nolimitenchants.MODID, "max_level_sync"));

    public static final StreamCodec<ByteBuf, MaxLevelSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            MaxLevelSyncPayload::maxLevel,
            MaxLevelSyncPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
