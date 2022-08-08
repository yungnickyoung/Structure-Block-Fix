package com.yungnickyoung.minecraft.structureblockfix.module;

import com.yungnickyoung.minecraft.structureblockfix.StructureBlockFixCommon;
import com.yungnickyoung.minecraft.structureblockfix.block.MegaStructureBlock;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

@AutoRegister(StructureBlockFixCommon.MOD_ID)
public class BlockModule {
    @AutoRegister("mega_structure_block")
    public static AutoRegisterBlock MEGA_STRUCTURE_BLOCK = AutoRegisterBlock.of(() -> new MegaStructureBlock(
            BlockBehaviour.Properties
                    .of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY)
                    .strength(-1.0F, 3600000.0F)
                    .noDrops()));
}
