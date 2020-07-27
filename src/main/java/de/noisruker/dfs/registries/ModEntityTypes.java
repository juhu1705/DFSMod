package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.entities.MagicProjectileEntity;
import de.noisruker.dfs.objects.entities.SoulEntity;
import de.noisruker.dfs.objects.entities.renderers.MagicProjectileEntityRenderer;
import de.noisruker.dfs.objects.entities.renderers.SoulEntityRenderer;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, DfSMod.MOD_ID);

    public static final RegistryObject<EntityType<SoulEntity>> ENTITY_SOUL = ENTITIES
            .register("soul_entity", () -> EntityType.Builder
                    .create(SoulEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.5F, 0.5F)
                    .build(new ResourceLocation(DfSMod.MOD_ID, "soul_entity").toString()));

    public static final RegistryObject<EntityType<MagicProjectileEntity>> ENTITY_MAGIC_PROJECTILE = ENTITIES
            .register("magic_projectile_entity", () -> EntityType.Builder
                    .<MagicProjectileEntity>create(MagicProjectileEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(0.2F, 0.2F)
                    .build(new ResourceLocation(DfSMod.MOD_ID, "magic_projectile_entity").toString()));





    public static void bindRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(ENTITY_MAGIC_PROJECTILE.get(), new MagicProjectileEntityRenderer.Factory());
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
