package com.tristanphan.DeathBoard;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class PlayerProfile {
    static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    UUID uuid;
    String username = "";
    String nickname = "";
    int deaths = 0;
    public PlayerProfile (String uuid1) throws IOException {

        this.uuid = UUID.fromString(uuid1);

        // User Info
        String userFile = "";
        try {
            userFile = new String(Files.readAllBytes(Paths.get(String.format("plugins/Essentials/userdata/%s.yml", this.uuid))));
        } catch (Exception ignored) { }
        String[] userData = userFile.split("\n");
        for (String i : userData) {
            if (i.contains("lastAccountName")) { this.username = i.substring(17); }
            if (i.contains("nickname")) { this.nickname = i.substring(10); }
        }
        if (this.username.isEmpty()) {
            this.username = "";
        }
        if (this.nickname.isEmpty()) {
            this.nickname = this.username;
        }

        // Stats
        JsonParser parser = new JsonParser();
        JsonObject stat = (JsonObject) parser.parse(new FileReader(String.format("world/stats/%s.json", this.uuid)));
        try {
            deaths = Integer.parseInt(stat.getAsJsonObject("stats").getAsJsonObject("minecraft:custom").get("minecraft:deaths").toString());
        } catch (Exception ignored) { }

        // AFK Status
        try {
            User user = ess.getUser(this.uuid);
            if(user != null && Bukkit.getPlayer(this.uuid) != null) {
                String add = " §a⬤§r";
                if(user.isAfk()) {
                    add = " §7⬤§r";
                }
                this.nickname += add;
            }
        } catch (Exception ignored) {}

    }
}
