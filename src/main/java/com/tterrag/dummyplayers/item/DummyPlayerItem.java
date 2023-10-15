package com.tterrag.dummyplayers.item;

import java.util.List;

import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

// Direct copy of ArmorStandItem with entity creation changed
public class DummyPlayerItem extends Item {
	public DummyPlayerItem(Item.Properties builder) {
		super(builder);
	}

	public InteractionResult useOn(UseOnContext context) {
		Direction direction = context.getClickedFace();
		if (direction == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			Level world = context.getLevel();
			BlockPlaceContext blockitemusecontext = new BlockPlaceContext(context);
			BlockPos blockpos = blockitemusecontext.getClickedPos();
			BlockPos blockpos1 = blockpos.above();
			if (blockitemusecontext.canPlace() && world.getBlockState(blockpos1).canBeReplaced(blockitemusecontext)) {
				double d0 = (double) blockpos.getX();
				double d1 = (double) blockpos.getY();
				double d2 = (double) blockpos.getZ();
				List<Entity> list = world.getEntities((Entity) null,
						new AABB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
				if (!list.isEmpty()) {
					return InteractionResult.FAIL;
				} else {
					ItemStack itemstack = context.getItemInHand();
					if (!world.isClientSide) {
						world.removeBlock(blockpos, false);
						world.removeBlock(blockpos1, false);
						DummyPlayerEntity armorstandentity = new DummyPlayerEntity(world, d0 + 0.5D, d1, d2 + 0.5D);
						float f = (float) Mth
								.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F)
								* 45.0F;
						armorstandentity.moveTo(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
						this.applyRandomRotations(armorstandentity, world.random);
						EntityType.updateCustomEntityTag(world, context.getPlayer(), armorstandentity, itemstack.getTag());
						world.addFreshEntity(armorstandentity);
						world.playSound((Player) null, armorstandentity.getX(), armorstandentity.getY(),
								armorstandentity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS,
								0.75F, 0.8F);
					}

					itemstack.shrink(1);
					return InteractionResult.SUCCESS;
				}
			} else {
				return InteractionResult.FAIL;
			}
		}
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