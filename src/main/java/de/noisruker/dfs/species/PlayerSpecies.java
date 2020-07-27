package de.noisruker.dfs.species;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.entities.IEntityMagic;
import de.noisruker.dfs.network.PacketActiveAbility;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.registries.ModKeyBindings;
import de.noisruker.dfs.registries.ModSpecies;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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
    private ServerBossInfo bossInfo = new ServerBossInfo(new TranslationTextComponent("Power"), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);
    private Species species;
    private PlayerEntity player;
    //private boolean dataRegistered = false;

    public static PlayerSpecies getOrCreatePlayer(PlayerEntity player) {
        // DfSMod.LOGGER.debug("Containing Player: " + PlayerSpecies.PLAYER_SPECIES.containsKey(player));


        if (player.getGameProfile() == null)
            return null;

        //DfSMod.LOGGER.debug("Erfolgreich");

        for (Map.Entry<UUID, PlayerSpecies> entry : PlayerSpecies.PLAYER_SPECIES.entrySet()) {
            //DfSMod.LOGGER.debug("Hi " + entry.getKey().equals(player.getGameProfile().getId()) + " " + entry.getValue().updatePlayer(player).getSpecies() == null);
            if (entry.getKey().equals(player.getGameProfile().getId()))
                return entry.getValue().updatePlayer(player);

        }

        if (PlayerSpecies.PLAYER_SPECIES.containsKey(player.getGameProfile().getId()))
            return PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId());

        return PlayerSpecies.putPlayer(player, new PlayerSpecies(player)) == null ? PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId()) : PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId());
    }

    public static void changePlayerBoundig(PlayerEntity old, PlayerEntity _new) {
        PlayerSpecies species = PlayerSpecies.PLAYER_SPECIES.remove(old.getGameProfile().getId());

        species.updatePlayer(_new);

        putPlayer(_new, species);
        species.setPower(0.0f);
        species.setMaxPower(old.getDataManager().get(PlayerSpecies.MAX_POWER_VALUE));
        species.setPowerRegenerationAmount(old.getDataManager().get(PlayerSpecies.REGENERATION_VALUE));
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
        } catch (NullPointerException exception) {
            DfSMod.LOGGER.debug("Can't load species to players data manager!");
        }
        species.applyBaseValues(player);
        this.onResize();

        if (!player.world.isRemote() && player instanceof ServerPlayerEntity)
            this.bossInfo.addPlayer((ServerPlayerEntity) player);

        //this.registerDataParameters();
    }

    public PlayerSpecies updatePlayer(PlayerEntity player) {
        this.player = player;
        species.applyBaseValues(player);
        this.onResize();

        //this.registerDataParameters();

        if (!player.world.isRemote() && player instanceof ServerPlayerEntity)
            this.bossInfo.addPlayer((ServerPlayerEntity) player);

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

        this.setPowerRegenerationAmount(species.getPowerRegeneration());
        this.setMaxPower(species.getInitMaxPower());
        this.setPower(0.0f);

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
        if (this.player.getPersistentData().contains(PlayerSpecies.SPECIES))
            this.species = Species.SPECIES.get(this.player.getPersistentData().getString(PlayerSpecies.SPECIES));
        else
            this.species = ModSpecies.HUMAN;

        DfSMod.LOGGER.debug("Load Species Playerdata: " + Species.getKeyForSpecies(this.species));

        this.setNewSpecies(this.species);

        DfSMod.LOGGER.debug("Loaded Max Power: " + this.player.getPersistentData().getFloat(PlayerSpecies.MAX_POWER));

        this.setMaxPower(this.player.getPersistentData().getFloat(PlayerSpecies.MAX_POWER));
        this.setPower(this.player.getPersistentData().getFloat(PlayerSpecies.POWER));
        this.setPowerRegenerationAmount(this.player.getPersistentData().getFloat(PlayerSpecies.REGENERATION));
        this.species.applyBaseValues(this.player);
        this.bossInfo.removeAllPlayers();
        this.onResize();
    }

    @Override
    public float getPower() {
        try {
            return this.player.getDataManager().get(PlayerSpecies.POWER_VALUE);
        } catch (NullPointerException ignore) {

        }
        return 0;
    }

    @Override
    public float getMaxPower() {
        try {
            return this.player.getDataManager().get(PlayerSpecies.MAX_POWER_VALUE);
        } catch (NullPointerException ignore) {

        }
        return -1;
    }

    public float getPowerRegeneration() {
        try {
            return this.player.getDataManager().get(PlayerSpecies.REGENERATION_VALUE);
        } catch (NullPointerException ignore) {

        }
        return 0;
    }

    @Override
    public IEntityMagic setPower(float power) {
        try {
        if(power <= this.getMaxPower())
            this.player.getDataManager().set(PlayerSpecies.POWER_VALUE, power);
        else
            this.player.getDataManager().set(PlayerSpecies.POWER_VALUE, this.getMaxPower());
        } catch (NullPointerException ignore) {

        }
        return this;
    }

    @Override
    public IEntityMagic setMaxPower(float maxPower) {
        try {
        this.player.getDataManager().set(PlayerSpecies.MAX_POWER_VALUE, maxPower);
        } catch (NullPointerException ignore) {

        }
        if(this.getPower() > this.getMaxPower())
            this.setPower(this.getMaxPower());
        return this;
    }

    @Override
    public IEntityMagic usePower(float amount) {
        if(this.getPower() < amount) {
            amount -= this.getPower();
            this.setPower(0);
            amount *= 0.1;
            this.player.attackEntityFrom(DamageSource.MAGIC, amount);
            return this;
        }
        this.setPower(this.getPower() - amount);
        return this;
    }

    @Override
    public IEntityMagic setPowerRegenerationAmount(float powerRegeneration) {
        try {
            this.player.getDataManager().set(PlayerSpecies.REGENERATION_VALUE, powerRegeneration);
        } catch (NullPointerException ignore) {

        }
        return this;
    }

    @Override
    public IEntityMagic regeneratePower() {
        return this;
    }

    public IEntityMagic regeneratePower(PlayerEntity serverPlayer) {
        try {
            if(this.getMaxPower() == 0) {
                if(this.bossInfo.isVisible())
                    this.bossInfo.setVisible(false);
                return this;
            } else if(this.getMaxPower() == -1) {
                return this;
            }

            if(!this.bossInfo.isVisible())
                this.bossInfo.setVisible(true);

            if (serverPlayer.world.rand.nextInt(100) == 1 && !serverPlayer.isSprinting() && serverPlayer.getHealth() == serverPlayer.getMaxHealth()) {
                this.setPower(this.getPower() + this.getPowerRegeneration() * serverPlayer.world.rand.nextFloat());
                //DfSMod.LOGGER.debug("Regenerate to " + this.getPower() + " / " + this.getMaxPower());
            }

            if(serverPlayer instanceof ServerPlayerEntity && !serverPlayer.world.isRemote()) {

                try {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) serverPlayer;
                    if(!this.bossInfo.getPlayers().contains(serverPlayer)) {
                        this.bossInfo.removeAllPlayers();
                        this.bossInfo.addPlayer(playerEntity);
                    }
                } catch (ClassCastException exception) {
                    return this;
                }

            }

            this.bossInfo.setPercent(this.getPower() / this.getMaxPower());

        } catch (NullPointerException ignored) {

        }
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

    @SubscribeEvent
    public static void onPlayerServerTick(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);

            if(species != null)
                species.regeneratePower(player);
        }
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
    }*/

    /**
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

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if(ModKeyBindings.USE_ACTIVE_ABILITY.isPressed()) {
            SpeciesMessages.INSTANCE.sendToServer(new PacketActiveAbility());
        }
    }

    @Override
    public IEntityMagic addPower(float power) {
        this.setPower(this.getPower() + power);
        return this;
    }
}
