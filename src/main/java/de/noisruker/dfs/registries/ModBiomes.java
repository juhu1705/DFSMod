package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, DfSMod.MOD_ID);

    /*public static final RegistryObject<Biome> DUNGEON_BIOME = BIOMES.register("dungeon_biome", DungeonBiome::new);*/
}
