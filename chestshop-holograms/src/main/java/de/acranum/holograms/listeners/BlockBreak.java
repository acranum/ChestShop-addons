package de.acranum.holograms.listeners;

import de.acranum.chestshop.api.events.ShopDestroyEvent;
import de.acranum.chestshop.api.shop.Shop;
import de.acranum.holograms.Holograms;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(ShopDestroyEvent e) {
        Location location = e.getShop().getLocation();
        removeDisplayAtLocation(location, e.getShop());
    }

    public void removeDisplayAtLocation(Location location, Shop shop) {
        Vector facing = ShopCreateListener.getSignOffset(shop.getSign());
        Location newLocation = new Location(location.getWorld(), location.getBlockX() + 0.5 + facing.getBlockX(), location.getBlockY() + 1.2, location.getBlockZ() + 0.5 + facing.getBlockZ()).setDirection(facing);

        World world = location.getWorld();
        if (world == null) return;

        for (Entity entity : world.getNearbyEntities(newLocation, 1, 1, 1)) {
            if (Holograms.isChestShopHologram(entity)) {
                entity.remove();
            }
        }
    }

    /*private Vector getSignFacing(Block sign) {
        if (sign.getState() instanceof Sign) {
            BlockFace attachedFace = ((WallSign) sign.getState().getBlock().getBlockData()).getFacing().getOppositeFace();
            Vector direction;

            switch (attachedFace) {
                case NORTH -> direction = new Vector(0, 0, -1);
                case SOUTH -> direction = new Vector(0, 0, 1);
                case EAST -> direction = new Vector(1, 0, 0);
                case WEST -> direction = new Vector(-1, 0, 0);
                default -> direction = new Vector(0, 0, -1);
            }
            return direction;
        }
        return new Vector(0,0,-1);
    }*/
}
