package com.tterrag.dummyplayers;

import com.tterrag.dummyplayers.client.renderer.dummy.DummyPlayerEntityRenderer;
import com.tterrag.dummyplayers.client.renderer.dummy.DummyStandLayer;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;
import com.tterrag.dummyplayers.item.DummyPlayerItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod("dummyplayers")
public class DummyPlayers {

	public static final String MODID = "dummyplayers";

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() ->
    	Registrate.create(MODID)
			.defaultCreativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    public static Registrate registrate() {
    	return REGISTRATE.get();
    }

	private static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZER_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, DummyPlayers.MODID);

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<ResolvableProfile>> PROFILE_SERIALIZER = registrate().object("profile")
			.generic(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, () -> EntityDataSerializer.forValueType(ResolvableProfile.STREAM_CODEC))
			.register();

	public static final EntityEntry<DummyPlayerEntity> DUMMY_PLAYER = registrate().object("dummy_player")
			.<DummyPlayerEntity>entity(DummyPlayerEntity::new, MobCategory.MISC)
			.properties(b -> b.sized(Player.DEFAULT_BB_WIDTH, Player.DEFAULT_BB_HEIGHT))
			.register();

	public static final ItemEntry<DummyPlayerItem> SPAWNER = registrate()
			.item(DummyPlayerItem::new)
			.register();

	public DummyPlayers(IEventBus modBus) {
		DATA_SERIALIZER_REGISTER.register(modBus);
		modBus.addListener(this::createAttributes);
	}

	private void createAttributes(EntityAttributeCreationEvent event) {
		event.put(DUMMY_PLAYER.get(), LivingEntity.createLivingAttributes().build());
	}

	@EventBusSubscriber(modid = DummyPlayers.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class Client {
		@SubscribeEvent
		public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(DUMMY_PLAYER.get(), DummyPlayerEntityRenderer::new);
		}

		@SubscribeEvent
		public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(DummyStandLayer.LAYER, DummyStandLayer::createLayer);
		}
	}
}
