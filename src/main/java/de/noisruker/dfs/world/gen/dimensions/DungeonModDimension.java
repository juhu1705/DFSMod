package de.noisruker.dfs.world.gen.dimensions;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

import java.util.function.BiFunction;

public class DungeonModDimension extends ModDimension {


    @Override
    public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
        return DungeonDimension::new;
    }
}
