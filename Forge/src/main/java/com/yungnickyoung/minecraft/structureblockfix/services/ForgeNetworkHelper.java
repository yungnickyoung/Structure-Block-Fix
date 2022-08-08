package com.yungnickyoung.minecraft.structureblockfix.services;

import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import com.yungnickyoung.minecraft.structureblockfix.module.NetworkModuleForge;
import com.yungnickyoung.minecraft.structureblockfix.network.packet.SetMegaStructureBlockC2SPacketForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;

public class ForgeNetworkHelper implements INetworkHelper {
    @Override
    public void sendSetMegaStructureBlockC2SPacket(BlockPos blockPos,
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
                                                   long seed) {
        NetworkModuleForge.sendToServer(new SetMegaStructureBlockC2SPacketForge(blockPos,updateType, structureMode, name,
                offset, size, mirror, rotation, data, ignoreEntities, showAir, showBoundingBox, integrity, seed));
    }
}
