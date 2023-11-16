package com.mememan.twilightforesttweaks.manager;

import java.util.Optional;

import com.mememan.twilightforesttweaks.api.network.ITFTPacket;
import com.mememan.twilightforesttweaks.common.network.packets.s2c.CommonConfigSyncPacket;
import com.mememan.twilightforesttweaks.util.NameUtil;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TFTNetworkManager {
	private static final String PROTOCOL_VERSION = "1";
	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(NameUtil.prefix("channel"), () ->
			      PROTOCOL_VERSION, 
			      PROTOCOL_VERSION::equals,
			      PROTOCOL_VERSION::equals);
	
	public static void registerPackets() {
		int networkId = 0;
		
		registerCTSPackets(networkId);
		registerSTCPackets(networkId);
	}
	
	private static void registerCTSPackets(int id) {
	}
	
	private static void registerSTCPackets(int id) {
		CHANNEL.registerMessage(id++, CommonConfigSyncPacket.class, CommonConfigSyncPacket::encode, CommonConfigSyncPacket::decode, CommonConfigSyncPacket::onRecieve, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
	
	/**
	 * Sends an {@link ITFTPacket} from the server to all connected clients. S2C.
	 * @param packetToSend The {@link ITFTPacket} object to send to all connected clients.
	 */
	public static void sendAllClientPacket(ITFTPacket packetToSend) {
		CHANNEL.send(PacketDistributor.ALL.noArg(), packetToSend);
	}
	
	/**
	 * Sends an {@link ITFTPacket} from the server to the specified {@link ServerPlayerEntity}. S2C.
	 * @param targetPlayer The {@link ServerPlayerEntity} to send the specified {@link ITFTPacket} to.
	 * @param packetToSend The {@link ITFTPacket} to send to the specified {@link ServerPlayerEntity}.
	 */
	public static void sendPlayerClientPacket(ServerPlayerEntity targetPlayer, ITFTPacket packetToSend) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> targetPlayer), packetToSend);
	}
}