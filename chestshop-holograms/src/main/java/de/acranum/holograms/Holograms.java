package de.acranum.holograms;

import de.acranum.holograms.listeners.BlockBreak;
import de.acranum.holograms.listeners.ShopCreateListener;
import de.acranum.chestshop.api.ChestShopAPI;
import de.acranum.chestshop.api.ChestShopProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Holograms extends JavaPlugin {
    public static ChestShopAPI api = ChestShopProvider.get();
    public static Holograms inctance;
    private static NamespacedKey hologramKey;

    @Override
    public void onEnable() {
        inctance = this;
        hologramKey = new NamespacedKey(this, "chestshop_hologram");
        Bukkit.getPluginManager().registerEvents(new ShopCreateListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);

        killHolograms();
        reloadHolograms();
        //kill and redo all holograms
    }

    @Override
    public void onDisable() {
        killHolograms();
    }
    public static Holograms getInstance() {
        return inctance;
    }

    public static ChestShopAPI getChestShopAPI() {
        return api;
    }

    public static NamespacedKey getHologramKey() {
        return hologramKey;
    }

    public static boolean isChestShopHologram(Entity entity) {
        return entity instanceof Display
                && hologramKey != null
                && entity.getPersistentDataContainer().has(hologramKey, PersistentDataType.BYTE);
    }

    private void reloadHolograms() {
        spawnDisplaysFromConfig(api.getShopconfig());
    }
    private void killHolograms() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (isChestShopHologram(entity)) {
                    entity.remove();
                }
            }
        }
    }
    public void spawnDisplaysFromConfig(FileConfiguration config) {
        ConfigurationSection shops = config.getConfigurationSection("shops");
        if (shops == null) return;

        for (String key : shops.getKeys(false)) {
            String itemName = shops.getString(key);
            if (itemName == null || itemName == "" || itemName.equalsIgnoreCase("Air")) continue;

            String[] parts = key.split("§");
            if (parts.length != 4) continue;

            World world = Bukkit.getWorld(parts[0]);
            if (world == null) continue;

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int z = Integer.parseInt(parts[3]);
            Location location = new Location(world, x, y, z);

            if (api.getShop(location) == null) continue;

            ShopCreateListener.createhologramm(getChestShopAPI().getShop(location));
        }
    }
}
