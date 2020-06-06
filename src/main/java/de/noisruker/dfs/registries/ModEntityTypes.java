package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.entities.SoulEntity;
import de.noisruker.dfs.entities.renderers.SoulEntityRenderer;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jline.builtins.Source;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, DfSMod.MOD_ID);
    public static final DeferredRegister<Item> SPAWN_EGGS = new DeferredRegister<>(ForgeRegistries.ITEMS, DfSMod.MOD_ID);

    public static final RegistryObject<EntityType<SoulEntity>> ENTITY_SOUL = ENTITIES
            .register("soul_entity", () -> EntityType.Builder
                    .create(SoulEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.5F, 0.5F)
                    .build(new ResourceLocation(DfSMod.MOD_ID, "soul_entity").toString()));


    //Spawn Eggs
    public static final RegistryObject<Item> SPAWN_SOUL = SPAWN_EGGS.register("soul_entity_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.ENTITY_SOUL.get(), 0xfcfcfc, 0x17cfff, new Item.Properties().group(DfSMod.TAB_ITEMS)));

    public static void bindRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(ENTITY_SOUL.get(), SoulEntityRenderer::new);
    }

    public static void registerEntityWorldSpawns() {
        ModEntityTypes.registerEntitySpawning(ENTITY_SOUL.get(), EntityClassification.MONSTER, 5, 3, 13, (Biome[])DfSGenerator.DESERT.toArray());
    }

    public static void registerEntitySpawning(EntityType<?> entity, EntityClassification classification, int weight, int minGroupCount, int maxGroupCount, Biome... biomes) {
        for(Biome b: biomes)
            if(b != null)
                b.getSpawns(classification).add(new Biome.SpawnListEntry(entity, weight, minGroupCount, maxGroupCount));
    }
}
