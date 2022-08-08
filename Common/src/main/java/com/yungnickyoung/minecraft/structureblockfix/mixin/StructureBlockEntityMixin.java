package com.yungnickyoung.minecraft.structureblockfix.mixin;

import net.minecraft.core.BlockPos;
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
    private static int structureblockfix_increaseStructureBlockCornerSearchRange(int constant) {
        return 256; // Cannot go above 127 due to byte size in packet transmission
    }

    @Inject(method = "load", at = @At("RETURN"))
    public void structureblockfix_removeStructureBlockRangeConstraint(CompoundTag compoundTag, CallbackInfo ci) {
        int i = compoundTag.getInt("posX");
        int j = compoundTag.getInt("posY");
        int k = compoundTag.getInt("posZ");
        this.structurePos = new BlockPos(i, j, k);
        int l = compoundTag.getInt("sizeX");
        int i1 = compoundTag.getInt("sizeY");
        int j1 = compoundTag.getInt("sizeZ");
        this.structureSize = new Vec3i(l, i1, j1);
    }
}
