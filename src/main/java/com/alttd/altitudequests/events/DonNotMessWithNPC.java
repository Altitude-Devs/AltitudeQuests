package com.alttd.altitudequests.events;

import com.alttd.altitudequests.config.LocalConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class DonNotMessWithNPC implements Listener {

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entered = event.getEntered();
        if (!(entered instanceof Wolf))
            return;
        if (LocalConfig.activeNPC.equals(entered.getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onVillagerPotioned(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Wolf))
            return;
        if (LocalConfig.activeNPC.equals(entity.getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onVillagerEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Wolf))
            return;
        if (LocalConfig.activeNPC.equals(entity.getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onVillagerBlockDamage(EntityDamageByBlockEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Wolf))
            return;
        if (LocalConfig.activeNPC.equals(entity.getUniqueId()))
            event.setCancelled(true);
    }

}
