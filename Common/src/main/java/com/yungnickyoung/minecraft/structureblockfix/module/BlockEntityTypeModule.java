package com.yungnickyoung.minecraft.structureblockfix.module;

import com.yungnickyoung.minecraft.structureblockfix.StructureBlockFixCommon;
import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterBlockEntityType;

@AutoRegister(StructureBlockFixCommon.MOD_ID)
public class BlockEntityTypeModule {
    @AutoRegister("mega_structure_block")
    public static AutoRegisterBlockEntityType<MegaStructureBlockEntity> MEGA_STRUCTURE_BLOCK_ENTITY = AutoRegisterBlockEntityType.of(() ->
            AutoRegisterBlockEntityType.Builder
                    .of(MegaStructureBlockEntity::new, BlockModule.MEGA_STRUCTURE_BLOCK.get())
                    .build());
}
