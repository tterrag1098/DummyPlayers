package com.tterrag.dummyplayers.entity;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.tterrag.dummyplayers.DummyPlayers;
import com.tterrag.dummyplayers.item.DummyPlayerItem;
import com.tterrag.dummyplayers.network.LTExtrasNetwork;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

public class DummyPlayerEntity extends ArmorStand {

	public static final RegistryEntry<EntityType<DummyPlayerEntity>> DUMMY_PLAYER = DummyPlayers.registrate().object("dummy_player")
			.<DummyPlayerEntity>entity(DummyPlayerEntity::new, MobCategory.MISC)
			.properties(b -> b.sized(0.6F, 1.8F)) // Copied from player definition
			.register();

	public static final ItemEntry<DummyPlayerItem> SPAWNER = DummyPlayers.registrate()
			.item(DummyPlayerItem::new)
			.register();

	private static final EntityDataSerializer<GameProfile> PROFILE_SERIALIZER = new EntityDataSerializer<GameProfile>() {
		public GameProfile copy(GameProfile value) {
			return value == null ? null : new GameProfile(value.getId(), value.getName());
		}

		@Override
		public GameProfile read(FriendlyByteBuf buf) {
			int mode = buf.readByte();
			UUID id = null;
			String name = null;
			if ((mode & 0b01) > 0) {
				id = buf.readUUID();
			}
			if ((mode & 0b10) > 0) {
				name = buf.readUtf(100);
			}
			if (id == null && name == null) {
				return null;
			}
			return new GameProfile(id, name);
		}

		@Override
		public void write(FriendlyByteBuf buf, GameProfile value) {
			int mode = 0;
			if (value != null && value.getId() != null) {
				mode |= 0b01;
			}
			if (value != null && value.getName() != null) {
				mode |= 0b10;
			}
			buf.writeByte(mode);
			if ((mode & 0b01) > 0) {
				buf.writeUUID(value.getId());
			}
			if ((mode & 0b10) > 0) {
				buf.writeUtf(value.getName(), 100);
			}
		}
	};
	static {
		EntityDataSerializers.registerSerializer(PROFILE_SERIALIZER);
	}

	public static final GameProfile NULL_PROFILE = new GameProfile(UUID.fromString("e664daf0-5962-45a5-b8df-e4c992d372a9"), null);
	private static final EntityDataAccessor<GameProfile> GAME_PROFILE = SynchedEntityData.defineId(DummyPlayerEntity.class, PROFILE_SERIALIZER);
	private static final EntityDataAccessor<Optional<Component>> PREFIX = SynchedEntityData.defineId(DummyPlayerEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
	private static final EntityDataAccessor<Optional<Component>> SUFFIX = SynchedEntityData.defineId(DummyPlayerEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);

	// TODO is this ok in singleplayer
	private static final NonNullLazy<GameProfileCache> PROFILE_CACHE = NonNullLazy.of(() ->
    	new GameProfileCache(ServerLifecycleHooks.getCurrentServer().getProfileRepository(), new File(".", "dummyplayercache.json"))
	);

	public static void register() {}

	// Clientside texture info
	private boolean reloadTextures = true;
	
	// Holder to avoid leaking entity reference
	private static class SkinInfo {
		private final Map<Type, ResourceLocation> playerTextures = Maps.newEnumMap(Type.class);
		@Nullable
		private String skinType;
	}
	
	private final SkinInfo skinInfo = new SkinInfo();

	protected DummyPlayerEntity(EntityType<? extends DummyPlayerEntity> type, Level worldIn) {
		super(type, worldIn);
		if (!worldIn.isClientSide) {
			// Show arms always on
			this.entityData.set(DATA_CLIENT_FLAGS, (byte) 0b100);
		}
	}

	public DummyPlayerEntity(Level worldIn, double posX, double posY, double posZ) {
		this(DUMMY_PLAYER.get(), worldIn);
		this.setPos(posX, posY, posZ);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(GAME_PROFILE, NULL_PROFILE);
		this.entityData.define(PREFIX, Optional.empty());
		this.entityData.define(SUFFIX, Optional.empty());
	}

	@Override
	public Component getTypeName() {
		return getProfile().getName() == null ? super.getTypeName() : new TextComponent(getProfile().getName());
	}

	@Override
	public Component getDisplayName() {
		MutableComponent ret = super.getDisplayName().copy();
		Component prefix = this.entityData.get(PREFIX).orElse(null);
		Component suffix = this.entityData.get(SUFFIX).orElse(null);
		if (prefix != null) {
			ret = prefix.copy().append(ret);
		}
		if (suffix != null) {
			ret = ret.append(suffix.copy());
		}
		return ret;
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(SPAWNER.get());
	}

	public GameProfile getProfile() {
		return this.entityData.get(GAME_PROFILE);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);

		this.entityData.get(PREFIX)
			.ifPresent(prefix -> compound.putString("NamePrefix", Component.Serializer.toJson(prefix)));
		this.entityData.get(SUFFIX)
			.ifPresent(suffix -> compound.putString("NameSuffix", Component.Serializer.toJson(suffix)));

		GameProfile profile = getProfile();
		if (profile.equals(NULL_PROFILE)) return;
		if (profile.getId() != null) {
			compound.putUUID("ProfileID", profile.getId());
		} else if (profile.getName() != null) {
			compound.putString("ProfileName", profile.getName());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		if (compound.contains("NamePrefix", Tag.TAG_STRING)) {
			this.entityData.set(PREFIX, Optional.ofNullable(Component.Serializer.fromJson(compound.getString("NamePrefix"))));
		}
		if (compound.contains("NameSuffix", Tag.TAG_STRING)) {
			this.entityData.set(SUFFIX, Optional.ofNullable(Component.Serializer.fromJson(compound.getString("NameSuffix"))));
		}

		if (compound.contains("ProfileName", Tag.TAG_STRING)) {
			String name = compound.getString("ProfileName");
			GameProfile old = getProfile();
			if (!StringUtils.isBlank(name)) {
				this.entityData.set(GAME_PROFILE, new GameProfile(null, compound.getString("ProfileName")));
			} else {
				this.entityData.set(GAME_PROFILE, NULL_PROFILE);
			}
			if (old == null || old.getName() == null || !old.getName().equals(name)) {
				fillProfile();
			}
		} else if (compound.hasUUID("ProfileID")) {
			String existingName = getProfile().getName();
			UUID newId = compound.getUUID("ProfileID");
			if (getProfile() == null || getProfile().getId() == null && getProfile().getName() == null
					|| !getProfile().getId().equals(newId)) {
				// Only update the profile (and thus the texture) if it has changed in some way
				// Avoids unnecessary texture reloads on the client when changing pose/name
				this.entityData.set(GAME_PROFILE, new GameProfile(compound.getUUID("ProfileID"), existingName));
				fillProfile();
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide && reloadTextures) {
			LTExtrasNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new UpdateDummyTexturesMessage(this.getId()));
			reloadTextures = false;
		}
	}

	void fillProfile() {
		final GameProfile profile = getProfile();
		if (!profile.equals(NULL_PROFILE)) {
			reloadTextures();
		}
		CompletableFuture.supplyAsync(() -> {
			Optional<GameProfile> ret;
			if (profile.getId() != null) {
				ret = PROFILE_CACHE.get().get(profile.getId());
				if (ret.isPresent()) return ret.get();
				if (SkullBlockEntity.sessionService == null) return profile;
				try {
					return SkullBlockEntity.sessionService.fillProfileProperties(profile, true);
				} catch (Exception e) {
					e.printStackTrace();
					return profile;
				}
			} else {
				GameProfileCache cache = PROFILE_CACHE.get();
				synchronized (cache) {
					ret = cache.get(getProfile().getName());
				}
			}
			return ret.orElse(profile);
		}).thenAcceptAsync(gp -> {
			DummyPlayerEntity.this.entityData.set(GAME_PROFILE, gp);
			reloadTextures();
		}, getMainThreadExecutor());
	}

	private Executor getMainThreadExecutor() {
		return this.getCommandSenderWorld().isClientSide ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> Minecraft::getInstance) : getServer();
	}

	@OnlyIn(Dist.CLIENT)
	protected void updateSkinTexture() {
		if (reloadTextures) {
			synchronized (this) {
				if (reloadTextures) {
					this.skinInfo.playerTextures.clear();
					reloadTextures = false;
					if (getProfile().equals(NULL_PROFILE) || getProfile().getId() == null) return;
					LogManager.getLogger().info("Loading skin data for GameProfile: " + getProfile());
					final SkinInfo skinInfo = this.skinInfo;
					Minecraft.getInstance().getSkinManager().registerSkins(getProfile(), (p_210250_1_, p_210250_2_, p_210250_3_) -> {
						synchronized (skinInfo) {
							skinInfo.playerTextures.put(p_210250_1_, p_210250_2_);
							if (p_210250_1_ == Type.SKIN) {
								skinInfo.skinType = p_210250_3_.getMetadata("model");
								if (skinInfo.skinType == null) {
									skinInfo.skinType = "default";
								}
							}
						}
					}, true);
				}
			}
		}
	}

	private UUID getSkinUUID() {
		return getProfile().equals(NULL_PROFILE) || getProfile().getId() == null ? getUUID() : getProfile().getId();
	}

	public ResourceLocation getSkin() {
		updateSkinTexture();
		synchronized (skinInfo) {
			return skinInfo.playerTextures.computeIfAbsent(Type.SKIN,
					$ -> DefaultPlayerSkin.getDefaultSkin(getSkinUUID()));
		}
	}

	public ResourceLocation getCape() {
		return skinInfo.playerTextures.get(Type.CAPE);
	}

	public ResourceLocation getElytra() {
		return skinInfo.playerTextures.getOrDefault(Type.ELYTRA, getCape());
	}

	public String getSkinType() {
		return this.skinInfo.skinType == null ? DefaultPlayerSkin.getSkinModelName(getSkinUUID()) : this.skinInfo.skinType;
	}

	void reloadTextures() {
		this.reloadTextures = true;
	}
}
