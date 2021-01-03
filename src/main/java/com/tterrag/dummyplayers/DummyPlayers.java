package com.tterrag.dummyplayers;

import com.tterrag.dummyplayers.client.renderer.dummy.DummyPlayerEntityRenderer;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;
import com.tterrag.dummyplayers.network.LTExtrasNetwork;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.NonNullLazyValue;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("dummyplayers")
public class DummyPlayers {

	public static final String MODID = "dummyplayers";

    private static NonNullLazyValue<Registrate> registrate = new NonNullLazyValue<>(() -> 
    	Registrate.create(MODID)
			.itemGroup(() -> ItemGroup.MISC));

    public static Registrate registrate() {
    	return registrate.get();
    }

	@SuppressWarnings("deprecation")
	public DummyPlayers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		DummyPlayerEntity.register();

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			modBus.addListener(this::clientSetup);
		});

		LTExtrasNetwork.register();
	}

	@OnlyIn(Dist.CLIENT)
	private void clientSetup(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(DummyPlayerEntity.DUMMY_PLAYER.get(), DummyPlayerEntityRenderer::new);
	}
}
