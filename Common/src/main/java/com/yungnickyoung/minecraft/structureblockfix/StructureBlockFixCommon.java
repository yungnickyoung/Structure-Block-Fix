package com.yungnickyoung.minecraft.structureblockfix;

import com.yungnickyoung.minecraft.structureblockfix.services.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StructureBlockFixCommon {
    public static final String MOD_ID = "structureblockfix";
    public static final String MOD_NAME = "Structure Block Fix";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        Services.MODULES.loadModules();
    }
}
