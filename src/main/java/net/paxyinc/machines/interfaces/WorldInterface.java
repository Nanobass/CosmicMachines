package net.paxyinc.machines.interfaces;

import finalforeach.cosmicreach.entities.Player;

import java.util.Map;
import java.util.UUID;

public interface WorldInterface {

    Player loadPlayer(UUID uuid);
    void unloadPlayer(UUID uuid);
    Player getPlayer(UUID uuid);
    Map<UUID, Player> getPlayers();

}
