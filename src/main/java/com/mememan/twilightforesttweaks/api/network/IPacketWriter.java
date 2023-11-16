package com.mememan.twilightforesttweaks.api.network;

import java.util.function.BiConsumer;

import net.minecraft.network.PacketBuffer;

@FunctionalInterface
public interface IPacketWriter<T> extends BiConsumer<PacketBuffer, T> {
}
