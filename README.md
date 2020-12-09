# DeathBoard Plugin for Minecraft/Bukkit
A scoreboard plugin that display total death count of each player on the side

## Screenshots
![Scoreboard](https://user-images.githubusercontent.com/10486660/101570128-a287a200-398a-11eb-9965-9953b0bf64cc.png "Scoreboard")

## Features
- Replaces vanilla sidebar and tab menu scoreboard display
- Death count of the players on the server using Essentials nicknames (sidebar)
- Online/AFK indicator on the sidebar scoreboard
    - Green Dot = Online
    - Grey Dot = AFK
- Heath indicators in tab menu (another scoreboard)

## Commands
- Reload scoreboard: /deathboard reload
    - The scoreboard should automatically reload whenever someone logs in, logs out, goes AFK, or dies.
- Turn off scoreboard (per player): /deathboard disable
- Turn on scoreboard (per player): /deathboard enable
- Help menu: /deathboard help

## Permissions
- deathboard.*
- deathboard.disable
- deathboard.reload

## Requirements
- Bukkit/Spigot/PaperSpigot
- Minecraft version 1.13+ (only tested on 1.15+)
- [EssentialsX Core](https://github.com/EssentialsX/Essentials "EssentialsX Core")

## Installation
- Install Essentials
    - Start the server at least once with Essentials before proceeding
- Put the JAR file in the plugins folder

## How it works
This plugin looks at the files that Essentials and Minecraft store to find:
- Essentials display name
- Achievements-based death count

The Essentials API is used to determine whether players are online and AFK