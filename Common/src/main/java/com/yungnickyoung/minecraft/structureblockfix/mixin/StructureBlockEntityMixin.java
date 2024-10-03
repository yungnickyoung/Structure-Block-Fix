package com.yungnickyoung.minecraft.structureblockfix.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlockEntity.class)
public abstract class StructureBlockEntityMixin {
    @Shadow
    private BlockPos structurePos = new BlockPos(0, 1, 0);

    @Shadow
    private Vec3i structureSize = Vec3i.ZERO;

    @ModifyConstant(
            method = "detectSize",
            constant = @Constant(intValue = 80),
            require = 0
    )
    private int structureblockfix_increaseStructureBlockCornerSearchRange(int constant) {
        return 128;
    }

    @Inject(method = "loadAdditional", at = @At("RETURN"))
    public void structureblockfix_removeStructureBlockRangeConstraint1(CompoundTag tag, HolderLookup.Provider $$1, CallbackInfo ci) {
        int i = tag.getInt("posX");
        int j = tag.getInt("posY");
        int k = tag.getInt("posZ");
        this.structurePos = new BlockPos(i, j, k);
        int l = tag.getInt("sizeX");
        int i1 = tag.getInt("sizeY");
        int j1 = tag.getInt("sizeZ");
        this.structureSize = new Vec3i(l, i1, j1);
    }
}
