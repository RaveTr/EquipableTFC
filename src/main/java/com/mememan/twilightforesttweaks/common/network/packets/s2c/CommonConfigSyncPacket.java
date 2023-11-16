package com.mememan.twilightforesttweaks.common.network.packets.s2c;

import java.util.function.Supplier;

import com.mememan.twilightforesttweaks.api.network.ITFTPacket;
import com.mememan.twilightforesttweaks.manager.TFTConfigManager;
import com.mememan.twilightforesttweaks.util.NetworkUtil;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CommonConfigSyncPacket implements ITFTPacket {
	private final ConfigValue<?> configValue;
	
	public CommonConfigSyncPacket(ConfigValue<?> configValue) {
		this.configValue = configValue;
	}
	
	public static CommonConfigSyncPacket decode(PacketBuffer buf) {
		ObjectArrayList<String> configPath = NetworkUtil.listFromNetwork((mappedPath) -> buf.readUtf(), buf);
		ConfigValue<?> targetConfigValue = TFTConfigManager.MAIN_COMMON_SPEC.get(configPath);
		
		return new CommonConfigSyncPacket(targetConfigValue);
	}

	@Override
	public void encode(PacketBuffer buf) {
		NetworkUtil.listToNetwork(configValue.getPath(), (packetBuf, pathName) -> packetBuf.writeUtf(pathName), buf);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onRecieve(Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TFTConfigManager.MAIN_COMMON_SPEC.getValues().entrySet().forEach(targetConfigValue -> {
				if (targetConfigValue instanceof ConfigValue) {
					ConfigValue forgeConfigValue = (ConfigValue<?>) targetConfigValue;
					
					forgeConfigValue.set(configValue.get());
				}
			});
		});
		
		ctx.get().setPacketHandled(true);
	}
}
