package com.yungnickyoung.minecraft.structureblockfix.mixin;

import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import com.yungnickyoung.minecraft.structureblockfix.client.gui.MegaStructureBlockEditScreen;
import com.yungnickyoung.minecraft.structureblockfix.util.IMegaStructureBlockScreenOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements IMegaStructureBlockScreenOpener {
    @Shadow @Final protected Minecraft minecraft;

    @Override
    public void openMegaStructureBlock(MegaStructureBlockEntity megaStructureBlockEntity) {
        this.minecraft.setScreen(new MegaStructureBlockEditScreen(megaStructureBlockEntity));
    }
}
