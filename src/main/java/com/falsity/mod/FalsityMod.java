package com.falsity.mod;

import net.minecraftforge.fml.common.Mod;

@Mod(FalsityMod.MODID)
public class FalsityMod {
    public static final String MODID = "falsitymod";
    public static final String FAKE_NAME = "Falsity";

    public FalsityMod() {
        // Nada que registrar aqui: ChatHandler se registra solo (solo cliente).
    }
}
