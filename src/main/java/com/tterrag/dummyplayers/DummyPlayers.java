package com.tterrag.dummyplayers;

import com.tterrag.dummyplayers.client.renderer.dummy.DummyPlayerEntityRenderer;
import com.tterrag.dummyplayers.client.renderer.dummy.DummyStandLayer;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;
import com.tterrag.dummyplayers.network.LTExtrasNetwork;
import com.tterrag.registrate.Registrate;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("dummyplayers")
public class DummyPlayers {

	public static final String MODID = "dummyplayers";

    private static NonNullLazy<Registrate> registrate = NonNullLazy.of(() -> 
    	Registrate.create(MODID)
			.defaultCreativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    public static Registrate registrate() {
    	return registrate.get();
    }

	@SuppressWarnings("deprecation")
	public DummyPlayers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		DummyPlayerEntity.register();

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			modBus.addListener(this::registerEntityRenderers);
			modBus.addListener(this::registerLayers);
		});
		modBus.addListener(this::createAttributes);

		LTExtrasNetwork.register();
	}

	@OnlyIn(Dist.CLIENT)
	private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(DummyPlayerEntity.DUMMY_PLAYER.get(), DummyPlayerEntityRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(DummyStandLayer.LAYER, DummyStandLayer::createLayer);
	}

	private void createAttributes(EntityAttributeCreationEvent event) {
		event.put(DummyPlayerEntity.DUMMY_PLAYER.get(), LivingEntity.createLivingAttributes().build());
	}
}
