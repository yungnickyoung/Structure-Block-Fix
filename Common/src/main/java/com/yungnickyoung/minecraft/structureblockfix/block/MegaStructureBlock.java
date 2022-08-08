package com.yungnickyoung.minecraft.structureblockfix.block;

import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MegaStructureBlock extends BaseEntityBlock implements GameMasterBlock {
    public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;

    public MegaStructureBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MODE, StructureMode.LOAD));
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MegaStructureBlockEntity(blockPos, blockState);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof MegaStructureBlockEntity) {
            return ((MegaStructureBlockEntity) blockEntity).usedBy(player) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if (!level.isClientSide) {
            if (livingEntity != null) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof MegaStructureBlockEntity) {
                    ((MegaStructureBlockEntity) blockEntity).createdBy(livingEntity);
                }
            }

        }
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MODE);
    }

    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (level instanceof ServerLevel) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof MegaStructureBlockEntity megaStructureBlockEntity) {
                boolean hasNeighborSignal = level.hasNeighborSignal(blockPos);
                boolean isPowered = megaStructureBlockEntity.isPowered();
                if (hasNeighborSignal && !isPowered) {
                    megaStructureBlockEntity.setPowered(true);
                    this.trigger((ServerLevel) level, megaStructureBlockEntity);
                } else if (!hasNeighborSignal && isPowered) {
                    megaStructureBlockEntity.setPowered(false);
                }
            }
        }
    }

    private void trigger(ServerLevel serverLevel, MegaStructureBlockEntity megaStructureBlockEntity) {
        switch (megaStructureBlockEntity.getMode()) {
            case SAVE:
                megaStructureBlockEntity.saveStructure(false);
                break;
            case LOAD:
                megaStructureBlockEntity.loadStructure(serverLevel, false);
                break;
            case CORNER:
                megaStructureBlockEntity.unloadStructure();
            case DATA:
        }
    }
}
