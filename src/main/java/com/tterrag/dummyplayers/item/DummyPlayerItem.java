package com.tterrag.dummyplayers.item;

import com.tterrag.dummyplayers.DummyPlayers;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

// Direct copy of ArmorStandItem with entity creation changed
public class DummyPlayerItem extends Item {
	public DummyPlayerItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() == Direction.DOWN) {
			return InteractionResult.FAIL;
		}

        Level level = context.getLevel();
        BlockPos blockPos = new BlockPlaceContext(context).getClickedPos();

		Vec3 placePos = Vec3.atBottomCenterOf(blockPos);
		AABB aabb = DummyPlayers.DUMMY_PLAYER.get().getDimensions().makeBoundingBox(placePos.x(), placePos.y(), placePos.z());
        if (!level.noCollision(null, aabb) || !level.getEntities(null, aabb).isEmpty()) {
            return InteractionResult.FAIL;
        }

        ItemStack itemStack = context.getItemInHand();
        if (level instanceof ServerLevel serverLevel) {
            Consumer<DummyPlayerEntity> consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
            ResolvableProfile profile = itemStack.get(DataComponents.PROFILE);
            if (profile != null) {
                consumer = consumer.andThen(dummy -> dummy.setAndFillProfile(profile));
            }

            DummyPlayerEntity dummy = DummyPlayers.DUMMY_PLAYER.get().create(serverLevel, consumer, blockPos, MobSpawnType.SPAWN_EGG, true, true);
            if (dummy == null) {
                return InteractionResult.FAIL;
            }

            float angle = Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
			dummy.moveTo(dummy.getX(), dummy.getY(), dummy.getZ(), angle, 0.0F);
            this.applyRandomRotations(dummy, level.random);
            serverLevel.addFreshEntityWithPassengers(dummy);
            level.playSound(null, dummy.getX(), dummy.getY(),
                    dummy.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS,
                    0.75F, 0.8F);

        }

        itemStack.consume(1, context.getPlayer());
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

	private void applyRandomRotations(ArmorStand armorStand, RandomSource rand) {
		Rotations rotations = armorStand.getHeadPose();
		float f = rand.nextFloat() * 5.0F;
		float f1 = rand.nextFloat() * 20.0F - 10.0F;
		Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
		armorStand.setHeadPose(rotations1);
		rotations = armorStand.getBodyPose();
		f = rand.nextFloat() * 10.0F - 5.0F;
		rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
		armorStand.setBodyPose(rotations1);
	}
}
