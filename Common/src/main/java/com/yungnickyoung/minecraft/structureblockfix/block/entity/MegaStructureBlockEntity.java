package com.yungnickyoung.minecraft.structureblockfix.block.entity;


import com.yungnickyoung.minecraft.structureblockfix.block.MegaStructureBlock;
import com.yungnickyoung.minecraft.structureblockfix.client.gui.MegaStructureBlockEditScreen;
import com.yungnickyoung.minecraft.structureblockfix.module.BlockEntityTypeModule;
import com.yungnickyoung.minecraft.structureblockfix.module.BlockModule;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class MegaStructureBlockEntity extends BlockEntity {
    private ResourceLocation structureName;
    private String author = "";
    private String metaData = "";
    private BlockPos structurePos = new BlockPos(0, 1, 0);
    private Vec3i structureSize;
    private Mirror mirror;
    private Rotation rotation;
    private StructureMode mode;
    private boolean ignoreEntities;
    private boolean powered;
    private boolean showAir;
    private boolean showBoundingBox;
    private float integrity;
    private long seed;

    public MegaStructureBlockEntity(BlockPos $$0, BlockState $$1) {
        super(BlockEntityTypeModule.MEGA_STRUCTURE_BLOCK_ENTITY.get(), $$0, $$1);
        this.structureSize = Vec3i.ZERO;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.ignoreEntities = true;
        this.showBoundingBox = true;
        this.integrity = 1.0F;
        this.mode = $$1.getValue(MegaStructureBlock.MODE);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putString("name", this.getStructureName());
        compoundTag.putString("author", this.author);
        compoundTag.putString("metadata", this.metaData);
        compoundTag.putInt("posX", this.structurePos.getX());
        compoundTag.putInt("posY", this.structurePos.getY());
        compoundTag.putInt("posZ", this.structurePos.getZ());
        compoundTag.putInt("sizeX", this.structureSize.getX());
        compoundTag.putInt("sizeY", this.structureSize.getY());
        compoundTag.putInt("sizeZ", this.structureSize.getZ());
        compoundTag.putString("rotation", this.rotation.toString());
        compoundTag.putString("mirror", this.mirror.toString());
        compoundTag.putString("mode", this.mode.toString());
        compoundTag.putBoolean("ignoreEntities", this.ignoreEntities);
        compoundTag.putBoolean("powered", this.powered);
        compoundTag.putBoolean("showair", this.showAir);
        compoundTag.putBoolean("showboundingbox", this.showBoundingBox);
        compoundTag.putFloat("integrity", this.integrity);
        compoundTag.putLong("seed", this.seed);
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.setStructureName(compoundTag.getString("name"));
        this.author = compoundTag.getString("author");
        this.metaData = compoundTag.getString("metadata");
        int posX = compoundTag.getInt("posX");
        int posY = compoundTag.getInt("posY");
        int posZ = compoundTag.getInt("posZ");
        this.structurePos = new BlockPos(posX, posY, posZ);
        int sizeX = compoundTag.getInt("sizeX");
        int sizeY = compoundTag.getInt("sizeY");
        int sizeZ = compoundTag.getInt("sizeZ");
        this.structureSize = new Vec3i(sizeX, sizeY, sizeZ);

        try {
            this.rotation = Rotation.valueOf(compoundTag.getString("rotation"));
        } catch (IllegalArgumentException var11) {
            this.rotation = Rotation.NONE;
        }

        try {
            this.mirror = Mirror.valueOf(compoundTag.getString("mirror"));
        } catch (IllegalArgumentException var10) {
            this.mirror = Mirror.NONE;
        }

        try {
            this.mode = StructureMode.valueOf(compoundTag.getString("mode"));
        } catch (IllegalArgumentException var9) {
            this.mode = StructureMode.DATA;
        }

        this.ignoreEntities = compoundTag.getBoolean("ignoreEntities");
        this.powered = compoundTag.getBoolean("powered");
        this.showAir = compoundTag.getBoolean("showair");
        this.showBoundingBox = compoundTag.getBoolean("showboundingbox");
        if (compoundTag.contains("integrity")) {
            this.integrity = compoundTag.getFloat("integrity");
        } else {
            this.integrity = 1.0F;
        }

        this.seed = compoundTag.getLong("seed");
        this.updateBlockState();
    }

    private void updateBlockState() {
        if (this.level != null) {
            BlockPos blockPos = this.getBlockPos();
            BlockState blockState = this.level.getBlockState(blockPos);
            if (blockState.is(BlockModule.MEGA_STRUCTURE_BLOCK.get())) {
                this.level.setBlock(blockPos, blockState.setValue(MegaStructureBlock.MODE, this.mode), 2);
            }
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public boolean usedBy(Player player) {
        if (!player.canUseGameMasterBlocks()) {
            return false;
        } else {
            if (player.getCommandSenderWorld().isClientSide) {
//                player.openStructureBlock(this);
                Minecraft.getInstance().setScreen(new MegaStructureBlockEditScreen(this));
            }

            return true;
        }
    }

    public String getStructureName() {
        return this.structureName == null ? "" : this.structureName.toString();
    }

    public String getStructurePath() {
        return this.structureName == null ? "" : this.structureName.getPath();
    }

    public boolean hasStructureName() {
        return this.structureName != null;
    }

    public void setStructureName(@Nullable String $$0) {
        this.setStructureName(StringUtil.isNullOrEmpty($$0) ? null : ResourceLocation.tryParse($$0));
    }

    public void setStructureName(@Nullable ResourceLocation $$0) {
        this.structureName = $$0;
    }

    public void createdBy(LivingEntity $$0) {
        this.author = $$0.getName().getString();
    }

    public BlockPos getStructurePos() {
        return this.structurePos;
    }

    public void setStructurePos(BlockPos $$0) {
        this.structurePos = $$0;
    }

    public Vec3i getStructureSize() {
        return this.structureSize;
    }

    public void setStructureSize(Vec3i $$0) {
        this.structureSize = $$0;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public void setMirror(Mirror $$0) {
        this.mirror = $$0;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setRotation(Rotation $$0) {
        this.rotation = $$0;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public void setMetaData(String $$0) {
        this.metaData = $$0;
    }

    public StructureMode getMode() {
        return this.mode;
    }

    public void setMode(StructureMode $$0) {
        this.mode = $$0;
        BlockState blockState = this.level.getBlockState(this.getBlockPos());
        if (blockState.is(BlockModule.MEGA_STRUCTURE_BLOCK.get())) {
            this.level.setBlock(this.getBlockPos(), blockState.setValue(MegaStructureBlock.MODE, $$0), 2);
        }

    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    public void setIgnoreEntities(boolean $$0) {
        this.ignoreEntities = $$0;
    }

    public float getIntegrity() {
        return this.integrity;
    }

    public void setIntegrity(float $$0) {
        this.integrity = $$0;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long $$0) {
        this.seed = $$0;
    }

    public boolean detectSize() {
        if (this.mode != StructureMode.SAVE) {
            return false;
        } else {
            BlockPos blockPos = this.getBlockPos();
            BlockPos minCornerPos = new BlockPos(blockPos.getX() - 192, this.level.getMinBuildHeight(), blockPos.getZ() - 192);
            BlockPos maxCornerPos = new BlockPos(blockPos.getX() + 192, this.level.getMaxBuildHeight() - 1, blockPos.getZ() + 192);
            Stream<BlockPos> corners = this.getRelatedCorners(minCornerPos, maxCornerPos);
            return calculateEnclosingBoundingBox(blockPos, corners).filter((boundingBox) -> {
                int xSize = boundingBox.maxX() - boundingBox.minX();
                int ySize = boundingBox.maxY() - boundingBox.minY();
                int zSize = boundingBox.maxZ() - boundingBox.minZ();
                if (xSize > 1 && ySize > 1 && zSize > 1) {
                    this.structurePos = new BlockPos(boundingBox.minX() - blockPos.getX() + 1, boundingBox.minY() - blockPos.getY() + 1, boundingBox.minZ() - blockPos.getZ() + 1);
                    this.structureSize = new Vec3i(xSize - 1, ySize - 1, zSize - 1);
                    this.setChanged();
                    BlockState blockState = this.level.getBlockState(blockPos);
                    this.level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                    return true;
                } else {
                    return false;
                }
            }).isPresent();
        }
    }

    private Stream<BlockPos> getRelatedCorners(BlockPos minCornerPos, BlockPos maxCornerPos) {
        Objects.requireNonNull(this.level);
        Stream<BlockPos> structureBlocks = BlockPos.betweenClosedStream(minCornerPos, maxCornerPos)
                .filter((blockPos) -> this.level.getBlockState(blockPos).is(BlockModule.MEGA_STRUCTURE_BLOCK.get()));
        return structureBlocks.map(this.level::getBlockEntity)
                .filter((blockEntity) -> blockEntity instanceof MegaStructureBlockEntity)
                .map((blockEntity) -> (MegaStructureBlockEntity) blockEntity)
                .filter((blockEntity) -> blockEntity.mode == StructureMode.CORNER && Objects.equals(this.structureName, blockEntity.structureName))
                .map(BlockEntity::getBlockPos);
    }

    private static Optional<BoundingBox> calculateEnclosingBoundingBox(BlockPos blockPos, Stream<BlockPos> corners) {
        Iterator<BlockPos> iterator = corners.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else {
            BlockPos cornerPos = iterator.next();
            BoundingBox boundingBox = new BoundingBox(cornerPos);
            if (iterator.hasNext()) {
                Objects.requireNonNull(boundingBox);
                iterator.forEachRemaining(boundingBox::encapsulate);
            } else {
                boundingBox.encapsulate(blockPos);
            }

            return Optional.of(boundingBox);
        }
    }

    public boolean saveStructure() {
        return this.saveStructure(true);
    }

    public boolean saveStructure(boolean bl) {
        if (this.mode == StructureMode.SAVE && !this.level.isClientSide && this.structureName != null) {
            BlockPos startPos = this.getBlockPos().offset(this.structurePos);
            ServerLevel serverLevel = (ServerLevel)this.level;
            StructureManager structureManager = serverLevel.getStructureManager();

            StructureTemplate structureTemplate;
            try {
                structureTemplate = structureManager.getOrCreate(this.structureName);
            } catch (ResourceLocationException e) {
                return false;
            }

            structureTemplate.fillFromWorld(this.level, startPos, this.structureSize, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
            structureTemplate.setAuthor(this.author);
            if (bl) {
                try {
                    return structureManager.save(this.structureName);
                } catch (ResourceLocationException var7) {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean loadStructure(ServerLevel serverLevel) {
        return this.loadStructure(serverLevel, true);
    }

    private static Random createRandom(long seed) {
        return seed == 0L ? new Random(Util.getMillis()) : new Random(seed);
    }

    public boolean loadStructure(ServerLevel serverLevel, boolean bl) {
        if (this.mode == StructureMode.LOAD && this.structureName != null) {
            StructureManager structureManager = serverLevel.getStructureManager();

            Optional<StructureTemplate> structureTemplate;
            try {
                structureTemplate = structureManager.get(this.structureName);
            } catch (ResourceLocationException var6) {
                return false;
            }

            return structureTemplate.filter(template -> this.loadStructure(serverLevel, bl, template)).isPresent();
        } else {
            return false;
        }
    }

    public boolean loadStructure(ServerLevel serverLevel, boolean bl, StructureTemplate structureTemplate) {
        BlockPos blockPos = this.getBlockPos();
        if (!StringUtil.isNullOrEmpty(structureTemplate.getAuthor())) {
            this.author = structureTemplate.getAuthor();
        }

        Vec3i structureTemplateSize = structureTemplate.getSize();
        boolean isSameSize = this.structureSize.equals(structureTemplateSize);
        if (!isSameSize) {
            this.structureSize = structureTemplateSize;
            this.setChanged();
            BlockState blockState = serverLevel.getBlockState(blockPos);
            serverLevel.sendBlockUpdated(blockPos, blockState, blockState, 3);
        }

        if (bl && !isSameSize) {
            return false;
        } else {
            StructurePlaceSettings structurePlaceSettings = (new StructurePlaceSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities);
            if (this.integrity < 1.0F) {
                structurePlaceSettings.clearProcessors().addProcessor(new BlockRotProcessor(Mth.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.seed));
            }

            BlockPos offsetPos = blockPos.offset(this.structurePos);
            structureTemplate.placeInWorld(serverLevel, offsetPos, offsetPos, structurePlaceSettings, createRandom(this.seed), 2);
            return true;
        }
    }

    public void unloadStructure() {
        if (this.structureName != null) {
            ServerLevel serverLevel = (ServerLevel)this.level;
            StructureManager structureManager = serverLevel.getStructureManager();
            structureManager.remove(this.structureName);
        }
    }

    public boolean isStructureLoadable() {
        if (this.mode == StructureMode.LOAD && !this.level.isClientSide && this.structureName != null) {
            ServerLevel serverLevel = (ServerLevel)this.level;
            StructureManager structureManager = serverLevel.getStructureManager();

            try {
                return structureManager.get(this.structureName).isPresent();
            } catch (ResourceLocationException var4) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public boolean getShowAir() {
        return this.showAir;
    }

    public void setShowAir(boolean showAir) {
        this.showAir = showAir;
    }

    public boolean getShowBoundingBox() {
        return this.showBoundingBox;
    }

    public void setShowBoundingBox(boolean showBoundingBox) {
        this.showBoundingBox = showBoundingBox;
    }

    public enum UpdateType {
        UPDATE_DATA,
        SAVE_AREA,
        LOAD_AREA,
        SCAN_AREA;

        UpdateType() {
        }
    }
}