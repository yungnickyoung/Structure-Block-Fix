package com.yungnickyoung.minecraft.structureblockfix.services;

import com.yungnickyoung.minecraft.structureblockfix.module.NetworkModuleForge;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        NetworkModuleForge.init();
    }
}
