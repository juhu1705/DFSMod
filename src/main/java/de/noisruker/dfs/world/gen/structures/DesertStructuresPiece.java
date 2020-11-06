package de.noisruker.dfs.world.gen.structures;

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

import javax.annotation.Nonnull;
import java.util.Random;

public class DesertStructuresPiece {

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final int y_offset;

        public Piece(TemplateManager templateMgr, CompoundNBT nbt) {
            super(DfSGenerator.DESERT_STRUCTURE_PIECE_TYPE, nbt);
            this.resourceLocation = new ResourceLocation(nbt.getString("Template"));
            this.y_offset = nbt.getInt("yOffset");
            this.setupTemplate(templateMgr);
        }

        public Piece(TemplateManager templateMgr, BlockPos blockPos, ResourceLocation resourceLocation, int y_offset) {
            super(DfSGenerator.DESERT_STRUCTURE_PIECE_TYPE, 0);
            this.templatePosition = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.resourceLocation = resourceLocation;
            this.y_offset = y_offset;
            this.setupTemplate(templateMgr);
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putInt("yOffset", this.y_offset);
        }

        private void setupTemplate(TemplateManager templateMgr) {
            Template template = templateMgr.getTemplateDefaulted(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings())
                    .setRotation(Rotation.NONE)
                    .setMirror(Mirror.NONE)
                    .setCenterOffset(BlockPos.ZERO)
                    .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        public boolean func_230383_a_(ISeedReader seedReader, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox mutableBB, ChunkPos chunkPos, BlockPos blockPos) {
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(Rotation.randomRotation(rand)).setMirror(Mirror.NONE).setCenterOffset(BlockPos.ZERO).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            BlockPos blockpos = this.template.getSize();

            int i = 256;
            int j = 0;

            int k = blockpos.getX() * blockpos.getZ();

            if(k == 0)
                j = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());
            else {
                BlockPos blockpos1 = this.templatePosition.add(blockpos.getX() - 1, 0, blockpos.getZ() - 1);

                for(BlockPos blockpos2 : BlockPos.getAllInBoxMutable(this.templatePosition, blockpos1)) {
                    int l = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos2.getX(), blockpos2.getZ());
                    j += l;
                    i = Math.min(i, l);
                }

                j = j / k;
            }

            j -= y_offset;

            this.templatePosition = new BlockPos(this.templatePosition.getX(), j, this.templatePosition.getZ());
            return super.func_230383_a_(seedReader, manager, generator, rand, mutableBB, chunkPos, blockPos);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if ("chest".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.down(), RegistryHandler.DESERT_CHEST_LOOT);
            } else if ("chest_to_lava".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.down(), LootTables.CHESTS_SHIPWRECK_TREASURE);
            } else if ("chest_to_stone".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.down(), LootTables.CHESTS_SHIPWRECK_SUPPLY);
            }
        }
    }

}
