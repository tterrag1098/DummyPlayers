package com.tterrag.dummyplayers.entity;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateDummyTexturesMessage {

	private final int id;

	public UpdateDummyTexturesMessage(int id) {
		this.id = id;
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeInt(id);
	}

	public static UpdateDummyTexturesMessage fromBytes(PacketBuffer buf) {
		return new UpdateDummyTexturesMessage(buf.readInt());
	}

	@SuppressWarnings("resource")
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Entity e = Minecraft.getInstance().world.getEntityByID(id);
			if (e instanceof DummyPlayerEntity) {
				((DummyPlayerEntity) e).reloadTextures();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
