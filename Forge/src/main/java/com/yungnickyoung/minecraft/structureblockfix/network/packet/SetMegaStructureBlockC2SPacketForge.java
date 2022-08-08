package com.yungnickyoung.minecraft.structureblockfix.network.packet;

import com.yungnickyoung.minecraft.structureblockfix.block.entity.MegaStructureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetMegaStructureBlockC2SPacketForge {
    private static final int FLAG_IGNORE_ENTITIES = 1;
    private static final int FLAG_SHOW_AIR = 2;
    private static final int FLAG_SHOW_BOUNDING_BOX = 4;
    private final BlockPos pos;
    private final MegaStructureBlockEntity.UpdateType updateType;
    private final StructureMode mode;
    private final String name;
    private final BlockPos offset;
    private final Vec3i size;
    private final Mirror mirror;
    private final Rotation rotation;
    private final String data;
    private final boolean ignoreEntities;
    private final boolean showAir;
    private final boolean showBoundingBox;
    private final float integrity;
    private final long seed;

    public SetMegaStructureBlockC2SPacketForge(BlockPos blockPos,
                                               MegaStructureBlockEntity.UpdateType updateType,
                                               StructureMode structureMode,
                                               String name,
                                               BlockPos offset,
                                               Vec3i size,
                                               Mirror mirror,
                                               Rotation rotation,
                                               String data,
                                               boolean ignoreEntities,
                                               boolean showAir,
                                               boolean showBoundingBox,
                                               float integrity,
                                               long seed) {
        this.pos = blockPos;
        this.updateType = updateType;
        this.mode = structureMode;
        this.name = name;
        this.offset = offset;
        this.size = size;
        this.mirror = mirror;
        this.rotation = rotation;
        this.data = data;
        this.ignoreEntities = ignoreEntities;
        this.showAir = showAir;
        this.showBoundingBox = showBoundingBox;
        this.integrity = integrity;
        this.seed = seed;
    }

    public SetMegaStructureBlockC2SPacketForge(FriendlyByteBuf byteBuf) {
        this.pos = byteBuf.readBlockPos();
        this.updateType = byteBuf.readEnum(MegaStructureBlockEntity.UpdateType.class);
        this.mode = byteBuf.readEnum(StructureMode.class);
        this.name = byteBuf.readUtf();
        this.offset = new BlockPos(byteBuf.readByte(), byteBuf.readByte(), byteBuf.readByte());
        this.size = new Vec3i(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.mirror = byteBuf.readEnum(Mirror.class);
        this.rotation = byteBuf.readEnum(Rotation.class);
        this.data = byteBuf.readUtf(128);
        this.integrity = Mth.clamp(byteBuf.readFloat(), 0.0F, 1.0F);
        this.seed = byteBuf.readVarLong();
        int flags = byteBuf.readByte();
        this.ignoreEntities = (flags & FLAG_IGNORE_ENTITIES) != 0;
        this.showAir = (flags & FLAG_SHOW_AIR) != 0;
        this.showBoundingBox = (flags & FLAG_SHOW_BOUNDING_BOX) != 0;
    }

    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeBlockPos(this.pos);
        byteBuf.writeEnum(this.updateType);
        byteBuf.writeEnum(this.mode);
        byteBuf.writeUtf(this.name);
        byteBuf.writeByte(this.offset.getX());
        byteBuf.writeByte(this.offset.getY());
        byteBuf.writeByte(this.offset.getZ());
        byteBuf.writeInt(this.size.getX());
        byteBuf.writeInt(this.size.getY());
        byteBuf.writeInt(this.size.getZ());
        byteBuf.writeEnum(this.mirror);
        byteBuf.writeEnum(this.rotation);
        byteBuf.writeUtf(this.data);
        byteBuf.writeFloat(this.integrity);
        byteBuf.writeVarLong(this.seed);
        int flags = 0;
        if (this.ignoreEntities) flags |= FLAG_IGNORE_ENTITIES;
        if (this.showAir) flags |= FLAG_SHOW_AIR;
        if (this.showBoundingBox) flags |= FLAG_SHOW_BOUNDING_BOX;
        byteBuf.writeByte(flags);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Server-side logic
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer.canUseGameMasterBlocks()) {
                ServerLevel serverLevel = serverPlayer.getLevel();
                BlockPos blockpos = this.getPos();
                BlockState blockstate = serverLevel.getBlockState(blockpos);
                BlockEntity blockentity = serverLevel.getBlockEntity(blockpos);
                if (blockentity instanceof MegaStructureBlockEntity megaStructureBlockEntity) {
                    megaStructureBlockEntity.setMode(this.getMode());
                    megaStructureBlockEntity.setStructureName(this.getName());
                    megaStructureBlockEntity.setStructurePos(this.getOffset());
                    megaStructureBlockEntity.setStructureSize(this.getSize());
                    megaStructureBlockEntity.setMirror(this.getMirror());
                    megaStructureBlockEntity.setRotation(this.getRotation());
                    megaStructureBlockEntity.setMetaData(this.getData());
                    megaStructureBlockEntity.setIgnoreEntities(this.isIgnoreEntities());
                    megaStructureBlockEntity.setShowAir(this.isShowAir());
                    megaStructureBlockEntity.setShowBoundingBox(this.isShowBoundingBox());
                    megaStructureBlockEntity.setIntegrity(this.getIntegrity());
                    megaStructureBlockEntity.setSeed(this.getSeed());
                    if (megaStructureBlockEntity.hasStructureName()) {
                        String name = megaStructureBlockEntity.getStructureName();
                        if (this.getUpdateType() == MegaStructureBlockEntity.UpdateType.SAVE_AREA) {
                            if (megaStructureBlockEntity.saveStructure()) {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.save_success", name), false);
                            } else {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.save_failure", name), false);
                            }
                        } else if (this.getUpdateType() == MegaStructureBlockEntity.UpdateType.LOAD_AREA) {
                            if (!megaStructureBlockEntity.isStructureLoadable()) {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.load_not_found", name), false);
                            } else if (megaStructureBlockEntity.loadStructure(serverLevel)) {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.load_success", name), false);
                            } else {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.load_prepare", name), false);
                            }
                        } else if (this.getUpdateType() == MegaStructureBlockEntity.UpdateType.SCAN_AREA) {
                            if (megaStructureBlockEntity.detectSize()) {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.size_success", name), false);
                            } else {
                                serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.size_failure"), false);
                            }
                        }
                    } else {
                        serverPlayer.displayClientMessage(new TranslatableComponent("structure_block.invalid_structure_name", this.getName()), false);
                    }

                    megaStructureBlockEntity.setChanged();
                    serverPlayer.level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public MegaStructureBlockEntity.UpdateType getUpdateType() {
        return this.updateType;
    }

    public StructureMode getMode() {
        return this.mode;
    }

    public String getName() {
        return this.name;
    }

    public BlockPos getOffset() {
        return this.offset;
    }

    public Vec3i getSize() {
        return this.size;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public String getData() {
        return this.data;
    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    public boolean isShowAir() {
        return this.showAir;
    }

    public boolean isShowBoundingBox() {
        return this.showBoundingBox;
    }

    public float getIntegrity() {
        return this.integrity;
    }

    public long getSeed() {
        return this.seed;
    }
}
