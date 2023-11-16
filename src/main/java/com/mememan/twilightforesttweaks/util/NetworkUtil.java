package com.mememan.twilightforesttweaks.util;

import java.util.List;
import java.util.function.IntFunction;

import com.google.common.collect.Lists;
import com.mememan.twilightforesttweaks.api.network.IPacketReader;
import com.mememan.twilightforesttweaks.api.network.IPacketWriter;

import net.minecraft.network.PacketBuffer;

public final class NetworkUtil {
	
	private NetworkUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}
	
	public static <T> void listToNetwork(List<T> targetList, IPacketWriter<T> packetWriter, PacketBuffer curPacketBuf) {
		curPacketBuf.writeVarInt(targetList.size());
		
		targetList.forEach(targetObj -> packetWriter.accept(curPacketBuf, targetObj));
	}
	
	@SuppressWarnings("unchecked")
	public static <T, L extends List<T>> L listFromNetwork(IPacketReader<T> packetReader, PacketBuffer curPacketBuf) {
		int curVarInt = curPacketBuf.readVarInt();
		IntFunction<?> newList = Lists::newArrayListWithCapacity;
		L filledCapList = (L) newList.apply(curVarInt);
		
		for (int i = 0; i < curVarInt; i++) filledCapList.add(packetReader.apply(curPacketBuf));
		
		return filledCapList;
	}
}