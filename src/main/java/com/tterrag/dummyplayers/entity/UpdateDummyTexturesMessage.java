package com.tterrag.dummyplayers.entity;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateDummyTexturesMessage {

	private final int id;

	public UpdateDummyTexturesMessage(int id) {
		this.id = id;
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(id);
	}

	public static UpdateDummyTexturesMessage fromBytes(FriendlyByteBuf buf) {
		return new UpdateDummyTexturesMessage(buf.readInt());
	}

	@SuppressWarnings("resource")
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Entity e = Minecraft.getInstance().level.getEntity(id);
			if (e instanceof DummyPlayerEntity) {
				((DummyPlayerEntity) e).reloadTextures();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
