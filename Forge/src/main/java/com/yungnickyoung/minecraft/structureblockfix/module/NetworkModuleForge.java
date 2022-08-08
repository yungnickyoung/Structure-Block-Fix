package com.yungnickyoung.minecraft.structureblockfix.module;

import com.yungnickyoung.minecraft.structureblockfix.StructureBlockFixCommon;
import com.yungnickyoung.minecraft.structureblockfix.network.packet.SetMegaStructureBlockC2SPacketForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.concurrent.atomic.AtomicInteger;

public class NetworkModuleForge {
    private static final String PROTOCOL_VERSION = "1.0";
    private static SimpleChannel INSTANCE;
    private static final AtomicInteger packetId = new AtomicInteger(0);

    private static int id() {
        return packetId.getAndIncrement();
    }

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NetworkModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            INSTANCE = NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(StructureBlockFixCommon.MOD_ID, "main"),
                    () -> PROTOCOL_VERSION,
                    s -> true,
                    s -> true);

            INSTANCE.messageBuilder(SetMegaStructureBlockC2SPacketForge.class, id(), NetworkDirection.PLAY_TO_SERVER)
                    .decoder(SetMegaStructureBlockC2SPacketForge::new)
                    .encoder(SetMegaStructureBlockC2SPacketForge::encode)
                    .consumer(SetMegaStructureBlockC2SPacketForge::handle)
                    .add();
        });
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}