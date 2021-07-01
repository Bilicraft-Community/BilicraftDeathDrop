package com.bilicraft.bilicraftdeathdrop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BilicraftDeathDrop extends JavaPlugin implements Listener {
    private final Map<Player, Location> deathLocations = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event){
       deathLocations.put(event.getEntity(),event.getEntity().getLocation());
    }
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event){
       if(fastMatch(event.getLocation())){
           event.getEntity().setMetadata("BDD.DeathDrop",new FixedMetadataValue(this,true));
       }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().hasMetadata("BDD.DeathDrop")){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event){
        deathLocations.remove(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event){
        deathLocations.remove(event.getPlayer());
    }

    private boolean fastMatch(Location o1){
        for (Location o2 : deathLocations.values()) {
            if(!Objects.equals(o1.getWorld(), o2.getWorld())){
                continue;
            }
            if(o1.getX() != o2.getX()){
                continue;
            }
            if(o1.getY() != o2.getY()){
                continue;
            }
            if(o1.getZ() != o2.getZ()){
                continue;
            }
            return true;
        }
        return false;
    }

}
