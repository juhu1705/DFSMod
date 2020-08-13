package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.recipies.AncientFurnaceRecipe;
import de.noisruker.dfs.objects.recipies.serializers.AncientFurnaceRecipeSerializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DfSMod.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<AncientFurnaceRecipe>> ANCIENT_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZER_TYPES.register("ancient_smelting", () -> new AncientFurnaceRecipeSerializer(AncientFurnaceRecipe::new, 200));

    public static final IRecipeType<AncientFurnaceRecipe> ANCIENT_FURNACE_RECIPE = ModRecipeTypes.registerRecipeType("ancient_smelting");

    public static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }

}
