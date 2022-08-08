package com.yungnickyoung.minecraft.structureblockfix;

import com.yungnickyoung.minecraft.structureblockfix.client.StructureBlockFixForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(StructureBlockFixCommon.MOD_ID)
public class StructureBlockFixForge {
    public StructureBlockFixForge() {
        StructureBlockFixCommon.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> StructureBlockFixForgeClient::init);
    }
}