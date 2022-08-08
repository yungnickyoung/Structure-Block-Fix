package com.yungnickyoung.minecraft.structureblockfix.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class MegaStructureBlockRenderer implements BlockEntityRenderer<MegaStructureBlockEntity> {
    public MegaStructureBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MegaStructureBlockEntity structureBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (Minecraft.getInstance().player.canUseGameMasterBlocks() || Minecraft.getInstance().player.isSpectator()) {
            BlockPos blockPos = structureBlockEntity.getStructurePos();
            Vec3i vec3i = structureBlockEntity.getStructureSize();
            if (vec3i.getX() >= 1 && vec3i.getY() >= 1 && vec3i.getZ() >= 1) {
                if (structureBlockEntity.getMode() == StructureMode.SAVE || structureBlockEntity.getMode() == StructureMode.LOAD) {
                    double x = blockPos.getX();
                    double y = blockPos.getY();
                    double z = blockPos.getZ();
                    double h = y + (double)vec3i.getY();
                    double k, l;
                    switch (structureBlockEntity.getMirror()) {
                        case LEFT_RIGHT -> {
                            k = vec3i.getX();
                            l = (-vec3i.getZ());
                        }
                        case FRONT_BACK -> {
                            k = (-vec3i.getX());
                            l = vec3i.getZ();
                        }
                        default -> {
                            k = vec3i.getX();
                            l = vec3i.getZ();
                        }
                    }

                    double m, n, o, p;
                    switch (structureBlockEntity.getRotation()) {
                        case CLOCKWISE_90 -> {
                            m = l < 0.0 ? x : x + 1.0;
                            n = k < 0.0 ? z + 1.0 : z;
                            o = m - l;
                            p = n + k;
                        }
                        case CLOCKWISE_180 -> {
                            m = k < 0.0 ? x : x + 1.0;
                            n = l < 0.0 ? z : z + 1.0;
                            o = m - k;
                            p = n - l;
                        }
                        case COUNTERCLOCKWISE_90 -> {
                            m = l < 0.0 ? x + 1.0 : x;
                            n = k < 0.0 ? z : z + 1.0;
                            o = m + l;
                            p = n - k;
                        }
                        default -> {
                            m = k < 0.0 ? x + 1.0 : x;
                            n = l < 0.0 ? z + 1.0 : z;
                            o = m + k;
                            p = n + l;
                        }
                    }

                    float q = 1.0F;
                    float r = 0.9F;
                    float s = 0.5F;
                    VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.lines());
                    if (structureBlockEntity.getMode() == StructureMode.SAVE || structureBlockEntity.getShowBoundingBox()) {
                        LevelRenderer.renderLineBox(poseStack, vertexConsumer, m, y, n, o, h, p, r, r, r, q, s, s, s);
                    }

                    if (structureBlockEntity.getMode() == StructureMode.SAVE && structureBlockEntity.getShowAir()) {
                        this.renderInvisibleBlocks(structureBlockEntity, vertexConsumer, blockPos, poseStack);
                    }

                }
            }
        }
    }

    private void renderInvisibleBlocks(MegaStructureBlockEntity structureBlockEntity, VertexConsumer vertexConsumer, BlockPos blockPos, PoseStack poseStack) {
        BlockGetter blockGetter = structureBlockEntity.getLevel();
        BlockPos blockPos2 = structureBlockEntity.getBlockPos();
        BlockPos blockPos3 = blockPos2.offset(blockPos);
        Iterator var8 = BlockPos.betweenClosed(blockPos3, blockPos3.offset(structureBlockEntity.getStructureSize()).offset(-1, -1, -1)).iterator();

        while(true) {
            BlockPos blockPos4;
            boolean bl;
            boolean bl2;
            boolean bl3;
            boolean bl4;
            boolean bl5;
            do {
                if (!var8.hasNext()) {
                    return;
                }

                blockPos4 = (BlockPos)var8.next();
                BlockState blockState = blockGetter.getBlockState(blockPos4);
                bl = blockState.isAir();
                bl2 = blockState.is(Blocks.STRUCTURE_VOID);
                bl3 = blockState.is(Blocks.BARRIER);
                bl4 = blockState.is(Blocks.LIGHT);
                bl5 = bl2 || bl3 || bl4;
            } while(!bl && !bl5);

            float f = bl ? 0.05F : 0.0F;
            double d = ((float)(blockPos4.getX() - blockPos2.getX()) + 0.45F - f);
            double e = ((float)(blockPos4.getY() - blockPos2.getY()) + 0.45F - f);
            double g = ((float)(blockPos4.getZ() - blockPos2.getZ()) + 0.45F - f);
            double h = ((float)(blockPos4.getX() - blockPos2.getX()) + 0.55F + f);
            double i = ((float)(blockPos4.getY() - blockPos2.getY()) + 0.55F + f);
            double j = ((float)(blockPos4.getZ() - blockPos2.getZ()) + 0.55F + f);
            if (bl) {
                LevelRenderer.renderLineBox(poseStack, vertexConsumer, d, e, g, h, i, j, 0.5F, 0.5F, 1.0F, 1.0F, 0.5F, 0.5F, 1.0F);
            } else if (bl2) {
                LevelRenderer.renderLineBox(poseStack, vertexConsumer, d, e, g, h, i, j, 1.0F, 0.75F, 0.75F, 1.0F, 1.0F, 0.75F, 0.75F);
            } else if (bl3) {
                LevelRenderer.renderLineBox(poseStack, vertexConsumer, d, e, g, h, i, j, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F);
            } else if (bl4) {
                LevelRenderer.renderLineBox(poseStack, vertexConsumer, d, e, g, h, i, j, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F);
            }
        }
    }

    @Override
    public boolean shouldRender(MegaStructureBlockEntity $$0, Vec3 $$1) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(MegaStructureBlockEntity structureBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 384;
    }
}
