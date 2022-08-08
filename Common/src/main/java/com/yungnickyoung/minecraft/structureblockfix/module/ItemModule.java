package com.yungnickyoung.minecraft.structureblockfix.module;

import com.yungnickyoung.minecraft.structureblockfix.StructureBlockFixCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterItem;
import net.minecraft.world.item.GameMasterBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

@AutoRegister(StructureBlockFixCommon.MOD_ID)
public class ItemModule {
    @AutoRegister("mega_structure_block")
    public static final AutoRegisterItem MEGA_STRUCTURE_BLOCK_ITEM = AutoRegisterItem.of(() ->
            new GameMasterBlockItem(BlockModule.MEGA_STRUCTURE_BLOCK.get(),
                    new Item.Properties().rarity(Rarity.EPIC)));
}
