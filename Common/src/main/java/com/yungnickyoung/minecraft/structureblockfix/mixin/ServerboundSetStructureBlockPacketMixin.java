package com.yungnickyoung.minecraft.structureblockfix.mixin;

import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerboundSetStructureBlockPacket.class)
public abstract class ServerboundSetStructureBlockPacketMixin {
    @Redirect(
            method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(III)I")
    )
    private int structureblockfix_removeStructureBlockRangeConstraint2(int value, int min, int max) {
        return value;
    }
}
