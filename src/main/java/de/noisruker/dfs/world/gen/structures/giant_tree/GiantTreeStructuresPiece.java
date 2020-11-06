package de.noisruker.dfs.world.gen.structures.giant_tree;

import de.noisruker.dfs.registries.RegistryHandler;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Random;

public class GiantTreeStructuresPiece {

    public static class GiantTree extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final int y_offset;
        private final BlockPos checkingArea;

        public GiantTree(TemplateManager templateMgr, CompoundNBT nbt) {
            super(DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE, nbt);
            this.resourceLocation = new ResourceLocation(nbt.getString("Template"));
            this.y_offset = nbt.getInt("yOffset");
            this.checkingArea = new BlockPos(nbt.getInt("checking_area_x"), nbt.getInt("checking_area_y"), nbt.getInt("checking_area_z"));
            this.setup(templateMgr.getTemplateDefaulted(resourceLocation), templatePosition, placeSettings);
        }

        public GiantTree(TemplateManager templateMgr, BlockPos blockPos, BlockPos checkingArea, ResourceLocation resourceLocation, int y_offset) {
            super(DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE, 0);
            this.templatePosition = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.checkingArea = new BlockPos(checkingArea.getX(), checkingArea.getY(), checkingArea.getZ());
            this.resourceLocation = resourceLocation;
            this.y_offset = y_offset;
            this.setup(templateMgr.getTemplateDefaulted(resourceLocation), templatePosition, placeSettings);
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putInt("yOffset", this.y_offset);
            tagCompound.putInt("checking_area_x", this.checkingArea.getX());
            tagCompound.putInt("checking_area_y", this.checkingArea.getY());
            tagCompound.putInt("checking_area_z", this.checkingArea.getZ());
        }

        protected void setup(@NotNull Template template, @NotNull BlockPos pos, @NotNull PlacementSettings settings) {
            PlacementSettings placementsettings = (new PlacementSettings())
                    .setRotation(Rotation.NONE)
                    .setMirror(Mirror.NONE)
                    .setCenterOffset(BlockPos.ZERO)
                    .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            super.setup(template, pos, placementsettings);
        }

        @Override
        public boolean func_230383_a_(ISeedReader seedReader, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox mutableBB, ChunkPos chunkPos, BlockPos blockPos) {
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(Rotation.randomRotation(rand)).setMirror(Mirror.NONE).setCenterOffset(BlockPos.ZERO).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            BlockPos blockpos = this.template.getSize();

            int i = 256;
            int j = 0;

            //int k = blockpos.getX() * blockpos.getZ();

            //if(k == 0)
            j = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.checkingArea.getX(), this.checkingArea.getZ());
            //else {
                //BlockPos blockpos1 = this.checkingArea.add(19, 0, 19);


                //for (BlockPos blockpos2 : BlockPos.getAllInBoxMutable(this.checkingArea, blockpos1)) {
                    //int l = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos2.getX(), blockpos2.getZ());
                   // l = Math.min(i, l);
                    //j += l;

                //}


                //j = j / (19 * 19);
            //}

            j -= 2;
            j += y_offset;

            this.templatePosition = new BlockPos(this.templatePosition.getX(), j, this.templatePosition.getZ());
            return super.func_230383_a_(seedReader, manager, generator, rand, mutableBB, chunkPos, blockPos);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if ("chest".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.down(), LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
            } else if ("chest_armor".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.down(), RegistryHandler.ARMOR_CHEST_LOOT);
            } else if ("chest_north".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.north(), LootTables.CHESTS_PILLAGER_OUTPOST);
            } else if ("chest_t".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.north(), RegistryHandler.TREASURE_CHEST_LOOT);
            }
        }
    }

    /**public static void addPieces(ChunkGenerator<?> chunkGeneratorIn, TemplateManager templateManagerIn, BlockPos posIn, List<StructurePiece> structurePieces, SharedSeedRandom p_215139_4_) {
        GiantTreePools.init();
        JigsawManager.addPieces(new ResourceLocation(DfSMod.MOD_ID, "tree_bottom"), Integer.MAX_VALUE, GiantTreeStructuresPiece.GiantTree::new, chunkGeneratorIn, templateManagerIn, posIn, structurePieces, p_215139_4_);
    }

    public static class GiantTree extends AbstractVillagePiece {
        public GiantTree(TemplateManager templateManagerIn, JigsawPiece jigsawPieceIn, BlockPos posIn, int p_i50560_4_, Rotation rotationIn, MutableBoundingBox boundsIn) {
            super(DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE, templateManagerIn, jigsawPieceIn, posIn, p_i50560_4_, Rotation.NONE, boundsIn);
        }

        public GiantTree(TemplateManager templateManagerIn, CompoundNBT nbt) {
            super(templateManagerIn, nbt, DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE);
        }
    }*/

}
