package net.paxyinc.machines.mixins;

import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.interfaces.WorldInterface;
import net.paxyinc.machines.interfaces.ZoneInterface;
import net.paxyinc.machines.io.AdvancedEntitySaveSystem;
import net.paxyinc.machines.interfaces.PlayerInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(World.class)
public class WorldMixin implements WorldInterface {

    @Shadow public String defaultZoneId;
    @Shadow private transient HashMap<String, Zone> zoneMap;
    @Shadow public transient String worldFolderName;
    @Shadow private String worldDisplayName;
    @Shadow public long worldSeed = (new Random()).nextLong();

    private final Map<UUID, Player> allPlayers = new HashMap<>();

    @Override
    public Player loadPlayer(UUID uuid) {
        World world = (World) (Object) this;
        Player player = AdvancedEntitySaveSystem.loadPlayer(world, uuid);
        if(player == null) {
            player = new Player();
            player.zoneId = defaultZoneId;
        }
        allPlayers.put(uuid, player);
        PlayerInterface pi = (PlayerInterface) player;
        Zone playerZone = player.getZone(world);
        ZoneInterface zi = (ZoneInterface) playerZone;
        UUID controlledEntityUUID = pi.getControlledEntityUUID();
        BetterEntity controlledEntity = zi.loadEntity(BetterEntity.class, controlledEntityUUID);
        pi.initialize(world, controlledEntity);
        return player;
    }

    @Override
    public void unloadPlayer(UUID uuid) {
        World world = (World) (Object) this;
        Player player = getPlayer(uuid);
        if(player != null) allPlayers.remove(uuid);
        else return;
        PlayerInterface pi = (PlayerInterface) player;
        Zone playerZone = player.getZone(world);
        ZoneInterface zi = (ZoneInterface) playerZone;
        zi.unloadEntity(pi.getControlledEntityUUID());
        AdvancedEntitySaveSystem.savePlayer(world, uuid, player);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return allPlayers.get(uuid);
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(allPlayers.values());
    }

}
