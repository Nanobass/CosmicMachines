package net.paxyinc.machines.interfaces;

import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;

import java.util.List;
import java.util.UUID;

public interface WorldInterface {

    Player loadPlayer(UUID uuid);
    void unloadPlayer(UUID uuid);
    Player getPlayer(UUID uuid);
    List<Player> getPlayers();

}
