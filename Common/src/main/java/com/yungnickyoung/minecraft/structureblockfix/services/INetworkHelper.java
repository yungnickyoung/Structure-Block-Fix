package com.yungnickyoung.minecraft.structureblockfix.services;

import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;

public interface INetworkHelper {
    void sendSetMegaStructureBlockC2SPacket(BlockPos blockPos,
                                            MegaStructureBlockEntity.UpdateType updateType,
                                            StructureMode structureMode,
                                            String name,
                                            BlockPos offset,
                                            Vec3i size,
                                            Mirror mirror,
                                            Rotation rotation,
                                            String data,
                                            boolean ignoreEntities,
                                            boolean showAir,
                                            boolean showBoundingBox,
                                            float integrity,
                                            long seed);
}
