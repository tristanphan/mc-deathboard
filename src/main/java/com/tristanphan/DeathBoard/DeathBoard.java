package com.tristanphan.DeathBoard;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class DeathBoard extends JavaPlugin implements Listener {
    static Scoreboard scoreboard;
    static List<UUID> disabledPlayers = new ArrayList<>();
    static Objective deaths;
    static Objective health1;
    static Objective health2;
    static PlayerProfile[] players;

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            players = Fetch.fetchPlayers();
        } catch (IOException e) {
            players = new PlayerProfile[0];
        }
        loadScoreboard();

        Bukkit.getPluginManager().registerEvents(this, this);

        Objects.requireNonNull(getCommand("dc")).setTabCompleter(new ConstructTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dc")) {
            if (args.length == 0) {
                return false;
            } else if (args[0].equals("disable") && sender.hasPermission("deathboard.disable")) {
                Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
                sender.sendMessage("[" + ChatColor.RED + "DeathCount" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Disabling Scoreboard");
                disabledPlayers.add(Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).getUniqueId());
                return true;
            } else if (args[0].equals("enable") && sender.hasPermission("deathboard.disable")) {
                Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).setScoreboard(scoreboard);
                sender.sendMessage("[" + ChatColor.RED + "DeathCount" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Enabling Scoreboard");
                disabledPlayers.remove(Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).getUniqueId());
                return true;
            } else if (args[0].equals("reload") && sender.hasPermission("deathboard.reload")) {
                fetchDeaths();
                loadScoreboard();
                sender.sendMessage("[" + ChatColor.RED + "DeathCount" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Reloading...");
                return true;
            } else if (args[0].equals("help")) {
                sender.sendMessage("[" + ChatColor.RED + "DeathCount" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Possible Arguments: enable disable help");
            } else { return false; }
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(scoreboard);
        fetchDeaths();
        loadScoreboard();
        player.setPlayerListName(PlayerProfile.ess.getUser(player.getUniqueId()).getDisplayName());
        health1.getScore(player.getName()).setScore((int) player.getHealth());
        health2.getScore(player.getName()).setScore((int) player.getHealth());

        event.setJoinMessage(player.getDisplayName() + "§r§f has joined the game!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        disabledPlayers.remove(uuid);

        double seconds = 0.2;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            fetchDeaths();
            loadScoreboard();
        }, ((int)(seconds * 20)));
    }

    @EventHandler
    public void onAFK(AfkStatusChangeEvent event) {
        double seconds = 0.2;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            fetchDeaths();
            loadScoreboard();
        }, ((int)(seconds * 20)));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        double seconds = 0.2;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            fetchDeaths();
            loadScoreboard();
        }, ((int)(seconds * 20)));

    }

    public static void loadScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) return;
        scoreboard = scoreboardManager.getNewScoreboard();

        deaths = scoreboard.registerNewObjective("death","","Deaths", RenderType.INTEGER);
        deaths.setDisplaySlot(DisplaySlot.SIDEBAR);
        deaths.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Deaths");


        health1 = scoreboard.registerNewObjective("health1","health","Health", RenderType.HEARTS);
        health1.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        health1.setDisplayName("Health");

        health2 = scoreboard.registerNewObjective("health2","health","Health", RenderType.HEARTS);
        health2.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health2.setDisplayName("Health");

        for (PlayerProfile i : players) {
            if (!i.username.isEmpty()) {
                deaths.getScore(i.nickname).setScore(i.deaths);
            }
        }
        for (Player i : Bukkit.getOnlinePlayers()) {
            if (!disabledPlayers.contains(i.getUniqueId())) {
                i.setScoreboard(scoreboardManager.getNewScoreboard());
                i.setScoreboard(scoreboard);
                health1.getScore(i.getName()).setScore((int) i.getHealth());
                health2.getScore(i.getName()).setScore((int) i.getHealth());
            }
        }
    }

    public static void fetchDeaths() {
        // Attempt 1 to fetch deaths
        try {
            players = Fetch.fetchPlayers();
        } catch (IOException e) {
            players = new PlayerProfile[0];
        }

        // Attempt 2 to fetch deaths
        for (PlayerProfile i : players) {
            if (Bukkit.getPlayer(i.uuid) != null) {
                i.deaths = Objects.requireNonNull(Bukkit.getPlayer(i.uuid)).getStatistic(Statistic.DEATHS);
            }
        }
    }
}
