package de.noisruker.dfs.species;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.entities.IEntityMagic;
import de.noisruker.dfs.registries.ModSpecies;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSpecies implements IEntityMagic {

    private static final HashMap<UUID, PlayerSpecies> PLAYER_SPECIES = new HashMap<>();


    private static final String POWER = "power", REGENERATION = "power_regeneration", MAX_POWER = "max_power", SPECIES = "species";
    private static final DataParameter<Float> POWER_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.FLOAT), MAX_POWER_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.FLOAT), REGENERATION_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<String> SPECIES_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.STRING);
    private Species species;
    private PlayerEntity player;
    //private boolean dataRegistered = false;

    public static PlayerSpecies getOrCreatePlayer(PlayerEntity player) {
        // DfSMod.LOGGER.debug("Containing Player: " + PlayerSpecies.PLAYER_SPECIES.containsKey(player));


        if(player.getGameProfile() == null)
            return null;

        //DfSMod.LOGGER.debug("Erfolgreich");

        for(Map.Entry<UUID, PlayerSpecies> entry: PlayerSpecies.PLAYER_SPECIES.entrySet()) {
            //DfSMod.LOGGER.debug("Hi " + entry.getKey().equals(player.getGameProfile().getId()) + " " + entry.getValue().updatePlayer(player).getSpecies() == null);
            if(entry.getKey().equals(player.getGameProfile().getId()))
                return entry.getValue().updatePlayer(player);

        }

        if(PlayerSpecies.PLAYER_SPECIES.containsKey(player.getGameProfile().getId()))
            return PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId());

        return PlayerSpecies.putPlayer(player, new PlayerSpecies(player)) == null ? PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId()) : PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId());
    }

    public static void changePlayerBoundig(PlayerEntity old, PlayerEntity _new) {
        PlayerSpecies species = PlayerSpecies.PLAYER_SPECIES.remove(old.getGameProfile().getId());

        species.updatePlayer(_new);

        putPlayer(_new, species);
        species.setMaxPower(old.getGameProfile().get(PlayerSpecies.MAX_POWER_VALUE));
        species. setPowerRegenerationAmount(old.getGameProfile().get(PlayerSpecies.REGENERATION_VALUE));
    }

    public static PlayerSpecies putPlayer(PlayerEntity player, PlayerSpecies species) {
        return PlayerSpecies.PLAYER_SPECIES.put(player.getGameProfile().getId(), species);
    }

    protected PlayerSpecies(PlayerEntity player) {
        this.player = player;

        Species s;

        try {
            s = Species.SPECIES.get(this.player.getDataManager().get(PlayerSpecies.SPECIES_VALUE));
        } catch (NullPointerException exception) {
            s = ModSpecies.HUMAN;
        }

        this.species = s;

        try {
            this.setSpecies(s);
        } catch (NullPointerException exception){
            DfSMod.LOGGER.debug("Can't load species to players data manager!");
        }
        species.applyBaseValues(player);
        this.onResize();

        //this.registerDataParameters();
    }

    public PlayerSpecies updatePlayer(PlayerEntity player) {
        this.player = player;
        species.applyBaseValues(player);
        this.onResize();

        //this.registerDataParameters();

        return this;
    }

    public static Map<UUID, PlayerSpecies> getMap() {
        return PlayerSpecies.PLAYER_SPECIES;
    }

    public PlayerSpecies setNewSpecies(Species species) {
        this.species = species;
        this.setSpecies(species);
        species.applyBaseValues(player);
        this.onResize();

        this.player.setHealth(this.player.getMaxHealth() - (10 - this.player.getHealth()));

        this.player.setSneaking(false);

        return this;
    }

    public void update() {
        this.setSpecies(species);
        species.applyBaseValues(player);
        this.onResize();

        this.player.setHealth(this.player.getMaxHealth());
    }

    private void onRegenerate() {

    }

    private static void registerDataParameters(PlayerEntity player, Species species) {
        DfSMod.LOGGER.debug("Data Parameter are registered! ");

            player.getDataManager().register(PlayerSpecies.POWER_VALUE, 0.0f);
            player.getDataManager().register(PlayerSpecies.MAX_POWER_VALUE, species.getInitMaxPower());
            player.getDataManager().register(PlayerSpecies.REGENERATION_VALUE, species.getPowerRegeneration());
            player.getDataManager().register(PlayerSpecies.SPECIES_VALUE, Species.getKeyForSpecies(species));


        DfSMod.LOGGER.debug("Data Parameter are registered! ");
    }

    private void registerDataParameters() {
        registerDataParameters(player, this.species);

    }

    public Species getSpecies() {
        return Species.SPECIES.get(this.player.getDataManager().get(PlayerSpecies.SPECIES_VALUE));
    }

    private void setSpecies(Species species) {
        this.player.getDataManager().set(PlayerSpecies.SPECIES_VALUE, Species.getKeyForSpecies(species));
    }

    private void onResize() {
        this.species.applyBoundings(this.player);
    }

    private void onSave() {
        DfSMod.LOGGER.debug("Save Species Playerdata: " + Species.getKeyForSpecies(this.getSpecies()));

        this.player.getPersistentData().putFloat(PlayerSpecies.POWER, this.getPower());
        this.player.getPersistentData().putFloat(PlayerSpecies.MAX_POWER, this.getMaxPower());
        this.player.getPersistentData().putFloat(PlayerSpecies.REGENERATION, this.getPowerRegeneration());
        this.player.getPersistentData().putString(PlayerSpecies.SPECIES, Species.getKeyForSpecies(this.getSpecies()));
    }

    private void onLoad() {
        DfSMod.LOGGER.debug("Contains species: " + this.player.getPersistentData().contains(PlayerSpecies.SPECIES));
        if(this.player.getPersistentData().contains(PlayerSpecies.SPECIES))
            this.species = Species.SPECIES.get(this.player.getPersistentData().getString(PlayerSpecies.SPECIES));
        else
            this.species = ModSpecies.HUMAN;

        DfSMod.LOGGER.debug("Load Species Playerdata: " + Species.getKeyForSpecies(this.species));

        this.setNewSpecies(this.species);

        this.setMaxPower(this.player.getPersistentData().getFloat("maxPower"));
        this.setPower(this.player.getPersistentData().getFloat("power"));
        this.setPowerRegenerationAmount(this.player.getPersistentData().getFloat("powerRegeneration"));
        this.species.applyBaseValues(this.player);
        this.onResize();
    }

    @Override
    public float getPower() {
        return this.player.getDataManager().get(PlayerSpecies.POWER_VALUE);
    }

    @Override
    public float getMaxPower() {
        return this.player.getDataManager().get(PlayerSpecies.MAX_POWER_VALUE);
    }

    public float getPowerRegeneration() {
        return this.player.getDataManager().get(PlayerSpecies.REGENERATION_VALUE);
    }

    @Override
    public IEntityMagic setPower(float power) {
        if(power <= this.getMaxPower())
            this.player.getDataManager().set(PlayerSpecies.POWER_VALUE, power);
        return this;
    }

    @Override
    public IEntityMagic setMaxPower(float maxPower) {
        this.player.getDataManager().set(PlayerSpecies.MAX_POWER_VALUE, maxPower);

        if(this.getPower() < this.getMaxPower())
            this.setPower(this.getMaxPower());

        return this;
    }

    @Override
    public IEntityMagic usePower(float amount) {
        return this;
    }

    @Override
    public IEntityMagic setPowerRegenerationAmount(float powerRegeneration) {
        this.player.getDataManager().set(PlayerSpecies.REGENERATION_VALUE, powerRegeneration);
        return this;
    }

    @Override
    public IEntityMagic regeneratePower() {
        return this;
    }

    @SubscribeEvent
    public static void onPlayerEyeHeightEvent(EntityEvent.EyeHeight event) {

        if(event.getEntity() instanceof PlayerEntity) {

            PlayerSpecies player = PlayerSpecies.getOrCreatePlayer((PlayerEntity) event.getEntity());

            if(player == null)
                return;

            try {
                switch (event.getPose()) {
                    case SPIN_ATTACK:
                    case FALL_FLYING:
                    case SWIMMING:
                        //event.setNewHeight(player.species.getEyeHeight() - 1.22f);
                        event.setNewHeight(0.4f);
                        break;
                    case CROUCHING:
                        event.setNewHeight(player.species.getEyeHeight() - 0.35f);
                        break;
                    default:
                        event.setNewHeight(player.species.getEyeHeight());
                }
            } catch (NullPointerException exception) {
                DfSMod.LOGGER.debug("Can't load eye height!");
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        registerDataParameters(event.getPlayer(), ModSpecies.HUMAN);

        PlayerEntity player = (PlayerEntity) event.getEntity();

        //registerDataParameters(player, ModSpecies.HUMAN);


        PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(player);

        if (playerSpecies == null)
            return;

        playerSpecies.onLoad();
        playerSpecies.species.setHearts(player);

    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        registerDataParameters(event.getPlayer(), Species.SPECIES.get(event.getOriginal().getDataManager().get(PlayerSpecies.SPECIES_VALUE)));

        changePlayerBoundig(event.getOriginal(), event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerResize(RenderPlayerEvent event) {
        if(event instanceof RenderPlayerEvent.Pre) {
            PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

            event.getMatrixStack().push();

            if(playerSpecies == null)
                return;


            float width = playerSpecies.species.getWidth() / 0.6f,
                    height = playerSpecies.species.getHeight() / 1.8f;
            //DfSMod.LOGGER.debug("Width: " + width + "; Height: " + height);

            if(playerSpecies.player.getPose().equals(Pose.SWIMMING) ||playerSpecies.player.getPose().equals(Pose.FALL_FLYING) ||playerSpecies.player.getPose().equals(Pose.SPIN_ATTACK)) {


                event.getMatrixStack().scale(height, height, height);
            } else
                event.getMatrixStack().scale(height, height, height);
        } else if(event instanceof RenderPlayerEvent.Post) {
            event.getMatrixStack().pop();
        }
    }

    public static void onPlayer(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) event.getEntity();

            //registerDataParameters(player, ModSpecies.HUMAN);


            PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(player);

            if (playerSpecies == null)
                return;

            playerSpecies.onLoad();
            playerSpecies.species.setHearts(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

        if (playerSpecies == null)
            return;

        playerSpecies.onSave();
    }

    /**@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderManaBarEvent(RenderGameOverlayEvent event)	{
        if(!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)	{
            Minecraft mc = Minecraft.getInstance();
            if(!mc.player.isCreative() && !mc.player.isSpectator())	{
                int posX = event.getWindow().getScaledWidth() - 15;
                int posY = event.getWindow().getScaledHeight() / 2 - 55;
                mc.getTextureManager().bindTexture(new ResourceLocation(DfSMod.MOD_ID, "textures/gui/manabar.png"));
                if(Util.ClientMaxPower != 0)	{
                    mc.ingameGUI.blit(posX + 2, posY + 4, 11, 0, 7, 100);
                    double newPowerToDraw = (1.0 - ((double) Util.ClientPower / (double) Util.ClientMaxPower)) * 100.0;
                    if(waiting >= 2)	{
                        if(powerToDraw < (int) newPowerToDraw)	{
                            powerToDraw++;
                        }	else if(powerToDraw > (int) newPowerToDraw)	{
                            powerToDraw--;
                        }
                        waiting = 0;
                    }	else	{
                        waiting++;
                    }
                    mc.ingameGUI.blit(posX + 2, posY + 4, 18, 0, 7, (int) powerToDraw);
                    mc.ingameGUI.blit(posX, posY, 0, 0, 11, 110);
                }
            }
        }
    }

    public static void onPlayerHarvest(PlayerEvent.HarvestCheck event) {
        PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

        if (playerSpecies == null)
            return;

        if(playerSpecies.getSpecies().equals(ModSpecies.DWARFS)) {
            if(event.getTargetBlock().getMaterial().equals(Material.ROCK))
                event.setCanHarvest(true);
        }

    }*/

    public PlayerEntity getPlayer() {
        return this.player;
    }
}
