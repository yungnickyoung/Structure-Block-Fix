package com.yungnickyoung.minecraft.structureblockfix.mixin;

import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundSetStructureBlockPacket.class)
public abstract class ServerboundSetStructureBlockPacketMixin {
    @Shadow @Final @Mutable private Vec3i size;

    @Redirect(
            method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(III)I")
    )
    private int structureblockfix_removeStructureBlockRangeConstraint(int value, int min, int max) {
        return value;
    }

    @Inject(method="<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "RETURN"))
    private void structureblockfix_removeStructureBlockRangeConstraint2(FriendlyByteBuf buf, CallbackInfo ci) {
        this.size = new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Inject(method="write", at = @At(value = "RETURN"))
    private void structureblockfix_removeStructureBlockRangeConstraint3(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeInt(this.size.getX());
        buf.writeInt(this.size.getY());
        buf.writeInt(this.size.getZ());
    }
}
