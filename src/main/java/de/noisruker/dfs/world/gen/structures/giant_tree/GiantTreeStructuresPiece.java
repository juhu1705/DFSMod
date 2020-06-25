package de.noisruker.dfs.world.gen.structures.giant_tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.util.Pair;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.RegistryHandler;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tileentity.JigsawTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.*;
import net.minecraft.world.storage.loot.LootTables;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class GiantTreeStructuresPiece {

    public static void addPieces(ChunkGenerator<?> chunkGeneratorIn, TemplateManager templateManagerIn, BlockPos posIn, List<StructurePiece> structurePieces, SharedSeedRandom p_215139_4_) {
        GiantTreePools.init();
        JigsawManager.addPieces(new ResourceLocation(DfSMod.MOD_ID, "tree_bottom"), 5, GiantTreeStructuresPiece.GiantTree::new, chunkGeneratorIn, templateManagerIn, posIn, structurePieces, p_215139_4_);
    }

    public static class GiantTree extends AbstractVillagePiece {
        public GiantTree(TemplateManager templateManagerIn, JigsawPiece jigsawPieceIn, BlockPos posIn, int p_i50560_4_, Rotation rotationIn, MutableBoundingBox boundsIn) {
            super(DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE, templateManagerIn, jigsawPieceIn, posIn, p_i50560_4_, Rotation.NONE, boundsIn);
        }

        public GiantTree(TemplateManager templateManagerIn, CompoundNBT nbt) {
            super(templateManagerIn, nbt, DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE);
        }
    }

}
