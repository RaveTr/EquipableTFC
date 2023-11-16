package com.mememan.twilightforesttweaks.api.network;

import it.unimi.dsi.fastutil.Function;
import net.minecraft.network.PacketBuffer;

@FunctionalInterface
public interface IPacketReader<T> extends Function<PacketBuffer, T> {
}
