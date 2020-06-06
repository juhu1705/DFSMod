package de.noisruker.dfs.world.gen.structures;

import com.mojang.datafixers.Dynamic;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class PlainsStructure extends ScatteredStructure<NoFeatureConfig> {

    public PlainsStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
    }

    @Override
    protected int getSeedModifier() {
        return 145643948;
    }

    @Override
    public IStartFactory getStartFactory() {
        return PlainsStructure.Start::new;
    }

    public String getStructureName() {
        return DfSMod.MOD_ID + ":plains_structures";
    }

    public int getSize() {
        return 3;
    }



    public static class Start extends StructureStart {
        public Start(Structure<?> p_i225817_1_, int p_i225817_2_, int p_i225817_3_, MutableBoundingBox p_i225817_4_, int p_i225817_5_, long p_i225817_6_) {
            super(p_i225817_1_, p_i225817_2_, p_i225817_3_, p_i225817_4_, p_i225817_5_, p_i225817_6_);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            int worldX = chunkX * 16;
            int worldZ = chunkZ * 16;
            BlockPos blockpos = new BlockPos(worldX, 90, worldZ);
            int rand = new Random().nextInt(DfSGenerator.PLAINS_STRUCTURES_RESOURCES.size());
            this.components.add(new PlainsStructuresPiece.Piece(templateManagerIn, blockpos,
                    DfSGenerator.PLAINS_STRUCTURES_RESOURCES.get(rand),
                    DfSGenerator.Y_OFFSETS_OF_PLAINS_STRUCTURES.get(rand).intValue()));
            this.recalculateStructureSize();

        }
    }
}
