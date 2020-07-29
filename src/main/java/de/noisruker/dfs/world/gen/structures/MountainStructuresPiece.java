package de.noisruker.dfs.world.gen.structures;

import com.google.common.collect.ImmutableList;
import de.noisruker.dfs.registries.RegistryHandler;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class MountainStructuresPiece {

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
                    .addProcessor(new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.STRUCTURE_BLOCK)) {
                        @Override
                        protected IStructureProcessorType getType() {
                            return IStructureProcessorType.BLOCK_IGNORE;
                        }

                        @Nullable
                        @Override
                        public Template.BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, Template.BlockInfo p_215194_3_, Template.BlockInfo blockInfo, PlacementSettings placementSettingsIn) {
                            if(ImmutableList.of(Blocks.STRUCTURE_BLOCK).contains(blockInfo.state.getBlock()))
                                blockInfo = new Template.BlockInfo(blockInfo.pos, Blocks.AIR.getDefaultState(), null);
                            return blockInfo;
                        }
                    });
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGenIn, Random rand, MutableBoundingBox mutableBB, ChunkPos chunkPos) {
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(Rotation.randomRotation(rand)).setMirror(Mirror.NONE).setCenterOffset(BlockPos.ZERO).addProcessor(new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.STRUCTURE_BLOCK)) {
                @Override
                protected IStructureProcessorType getType() {
                    return IStructureProcessorType.BLOCK_IGNORE;
                }

                @Nullable
                @Override
                public Template.BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, Template.BlockInfo p_215194_3_, Template.BlockInfo blockInfo, PlacementSettings placementSettingsIn) {
                    if(ImmutableList.of(Blocks.STRUCTURE_BLOCK).contains(blockInfo.state.getBlock()))
                        blockInfo = new Template.BlockInfo(blockInfo.pos, Blocks.AIR.getDefaultState(), null);
                    return blockInfo;
                }
            });
            BlockPos blockpos = this.template.getSize();

            int i = 256;
            int j = 0;

            int k = blockpos.getX() * blockpos.getZ();

            if(k == 0)
                j = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());
            else {
                BlockPos blockpos1 = this.templatePosition.add(blockpos.getX() - 1, 0, blockpos.getZ() - 1);

                for(BlockPos blockpos2 : BlockPos.getAllInBoxMutable(this.templatePosition, blockpos1)) {
                    int l = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos2.getX(), blockpos2.getZ());
                    j += l;
                    i = Math.min(i, l);
                }

                j = j / k;
            }

            j -= y_offset;

            this.templatePosition = new BlockPos(this.templatePosition.getX(), j, this.templatePosition.getZ());
            return super.create(worldIn, chunkGenIn, rand, mutableBB, chunkPos);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
            switch (function) {
                case "chest_treasure":
                    LockableLootTileEntity.setLootTable(worldIn, rand, pos.north(), RegistryHandler.MOUNTAIN_TRESURE_CHEST_LOOT);
                    break;
                case "chest_east":
                    LockableLootTileEntity.setLootTable(worldIn, rand, pos.east(), RegistryHandler.BARREL_MOUNTAIN_LOOT);
                    break;
                case "chest_south":
                    LockableLootTileEntity.setLootTable(worldIn, rand, pos.south(), RegistryHandler.BARREL_MOUNTAIN_LOOT);
                    break;
                case "chest_west":
                    LockableLootTileEntity.setLootTable(worldIn, rand, pos.west(), RegistryHandler.BARREL_MOUNTAIN_LOOT);
                    break;
            }
        }
    }

}
