package com.yungnickyoung.minecraft.structureblockfix.client;

import com.yungnickyoung.minecraft.structureblockfix.client.render.MegaStructureBlockRenderer;
import com.yungnickyoung.minecraft.structureblockfix.module.BlockEntityTypeModule;
import com.yungnickyoung.minecraft.structureblockfix.module.BlockModule;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class StructureBlockFixForgeClient {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureBlockFixForgeClient::clientSetup);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(BlockEntityTypeModule.MEGA_STRUCTURE_BLOCK_ENTITY.get(), MegaStructureBlockRenderer::new);
            ItemBlockRenderTypes.setRenderLayer(BlockModule.MEGA_STRUCTURE_BLOCK.get(), RenderType.cutout());
        });
    }
}