package me.karltroid.essentialpets;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class EssentialPets extends JavaPlugin implements Listener {

    EntityType[] allowedKillers = {EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER};

    @Override
    public void onEnable()
    {
        System.out.println("[EssentialPets] Started!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event)
    {
        Entity killer = event.getDamager();
        Entity damaged = event.getEntity();

        // exit if the entity does not have a custom name
        if (damaged.customName() == null)
            return;

        // exit if the killer is allowed/whitelisted
        for (EntityType allowedKiller : allowedKillers)
        {
            if (killer.getType() == allowedKiller)
                return;
        }

        // if the killer has permission then exit and allow the entity to be hurt as normal
        if (killer instanceof Player)
        {
            Player player = (Player) killer;
            if (player.hasPermission("EssentialPets.kill"))
                return;
            else
                killer.sendMessage("You can not hurt " + event.getEntity().getName());
        }

        // ignore the event (entity does not get damaged)
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event)
    {
        // exit if the damage was caused by an entity, as that will be handled by onEntityDamagedByEntity()
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;

        // ignore the event (entity does not get damaged)
        Entity damaged = event.getEntity();
        if (damaged.customName() != null)
            event.setCancelled(true);
    }
}
