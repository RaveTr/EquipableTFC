package com.mememan.twilightforesttweaks.api.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public interface ITFTPacket {
	void encode(PacketBuffer buf);
	void onRecieve(Supplier<Context> ctx);
}
