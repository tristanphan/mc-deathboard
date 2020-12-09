package com.tristanphan.DeathBoard;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.io.IOException;

public class Fetch {
    public static PlayerProfile[] fetchPlayers() throws IOException {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        String[] playerUUIDs = new String[onlinePlayers.length + offlinePlayers.length];
        for (int i = 0; i < onlinePlayers.length; i++) {
            playerUUIDs[i] = onlinePlayers[i].getUniqueId().toString();
        }
        for (int i = onlinePlayers.length; i < onlinePlayers.length + offlinePlayers.length; i++) {
            playerUUIDs[i] = offlinePlayers[i - onlinePlayers.length].getUniqueId().toString();
        }
        PlayerProfile[] players = new PlayerProfile[playerUUIDs.length];
        for (int i = 0; i < playerUUIDs.length; i++) {
            players[i] = new PlayerProfile(playerUUIDs[i]);
        }
        return players;
    }
}
