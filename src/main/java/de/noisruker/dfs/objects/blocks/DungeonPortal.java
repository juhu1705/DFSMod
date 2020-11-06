package de.noisruker.dfs.objects.blocks;

import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.material.Material;

public class DungeonPortal extends NetherPortalBlock {

    public DungeonPortal() {
        super(Properties.create(Material.GLASS));
    }

    /*@Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && !worldIn.isRemote && DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) != null) {
            entityIn.changeDimension(worldIn.func_230315_m_().func_242714_a(DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE)) ? DimensionType.field_242710_a : DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE));
        }

    }



    @Override
    public boolean trySpawnPortal(IWorld worldIn, BlockPos pos) {
        NetherPortalBlock.Size netherportalblock$size = this.isPortal(worldIn, pos);
        if (netherportalblock$size != null && !net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(worldIn, pos, netherportalblock$size)) {
*/

            /*if (this.mode == StructureMode.LOAD && !this.world.isRemote && this.name != null) {
                ServerWorld serverworld = (ServerWorld)this.world;
                TemplateManager templatemanager = serverworld.getStructureTemplateManager();

                Template template;
                try {
                    template = templatemanager.getTemplate(this.name);
                } catch (ResourceLocationException var6) {
                    return false;
                }

                return template == null ? false : this.load(requireMatchingSize, template);
            } else {
                return false;
            }


            BlockPos blockpos = this.getPos();
            if (!StringUtils.isNullOrEmpty(templateIn.getAuthor())) {
                this.author = templateIn.getAuthor();
            }

            BlockPos blockpos1 = templateIn.getSize();
            boolean flag = this.size.equals(blockpos1);
            if (!flag) {
                this.size = blockpos1;
                this.markDirty();
                BlockState blockstate = this.world.getBlockState(blockpos);
                this.world.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
            }

            if (requireMatchingSize && !flag) {
                return false;
            } else {
                PlacementSettings placementsettings = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk((ChunkPos)null);
                if (this.integrity < 1.0F) {
                    placementsettings.clearProcessors().addProcessor(new IntegrityProcessor(MathHelper.clamp(this.integrity, 0.0F, 1.0F))).setRandom(func_214074_b(this.seed));
                }

                BlockPos blockpos2 = blockpos.add(this.position);
                templateIn.addBlocksToWorldChunk(this.world, blockpos2, placementsettings);
                return true;
            }
        }*/

/*

            return true;
        } else {
            return false;
        }
    }
*/

}
