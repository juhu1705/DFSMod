package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.containers.AncientFurnaceContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, DfSMod.MOD_ID);

    public static final RegistryObject<ContainerType<AncientFurnaceContainer>> ANCIENT_FURNACE_CONTAINER = CONTAINER_TYPES
            .register("ancient_furnace", () -> IForgeContainerType.create(AncientFurnaceContainer::new));

}
