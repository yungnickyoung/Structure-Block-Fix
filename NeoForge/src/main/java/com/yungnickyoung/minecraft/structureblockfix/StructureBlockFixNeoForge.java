package com.yungnickyoung.minecraft.structureblockfix;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(StructureBlockFixCommon.MOD_ID)
public class StructureBlockFixForge {
    public StructureBlockFixForge() {
        StructureBlockFixCommon.init();
    }
}