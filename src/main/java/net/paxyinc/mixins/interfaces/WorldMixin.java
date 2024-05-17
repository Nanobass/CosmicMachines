package net.paxyinc.mixins.interfaces;

import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.interfaces.WorldInterface;
import net.paxyinc.interfaces.ZoneInterface;
import net.paxyinc.io.PlayerSaveSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
        Player player = PlayerSaveSystem.loadPlayer(world, uuid);
        if(player == null) {
            player = new Player();
            player.zoneId = defaultZoneId;
            BetterEntity entity = new BetterEntity();
            entity.savable = false;
            player.setEntity(entity);
            player.setPosition(0, 300, 0);

        }
        ZoneInterface zi = (ZoneInterface) player.getZone(world);
        zi.addEntity((BetterEntity) player.getEntity());
        allPlayers.put(uuid, player);
        return player;
    }

    @Override
    public void unloadPlayer(UUID uuid) {
        World world = (World) (Object) this;
        Player player = getPlayer(uuid);
        if(player != null) allPlayers.remove(uuid);
        else return;
        PlayerSaveSystem.savePlayer(world, uuid, player);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return allPlayers.get(uuid);
    }

    @Override
    public Map<UUID, Player> getPlayers() {
        return allPlayers;
    }

}
