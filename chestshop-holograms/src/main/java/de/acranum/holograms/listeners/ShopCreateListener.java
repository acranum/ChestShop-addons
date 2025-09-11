package de.acranum.holograms.listeners;

import de.acranum.chestshop.api.ChestShopAPI;
import de.acranum.chestshop.api.ChestShopProvider;
import de.acranum.chestshop.api.events.PostShopCreateEvent;
import de.acranum.chestshop.api.shop.Shop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShopCreateListener implements Listener {
    static ChestShopAPI api = ChestShopProvider.get();
    @EventHandler
    public void onChestCreate(PostShopCreateEvent e) {
        Shop shop = e.getShop();
        createhologramm(shop);
    }


    public static void createhologramm(Shop shop) {
        Vector facing = getSignFacing(shop.getSign());
        Location newLocation = new Location(shop.getLocation().getWorld(), shop.getLocation().getBlockX() + 0.5 + facing.getBlockX(), shop.getLocation().getBlockY() + 1.2, shop.getLocation().getBlockZ() + 0.5 + facing.getBlockZ()).setDirection(facing);
        float yaw = newLocation.getYaw();
        float pitch = newLocation.getPitch();

        ItemDisplay item = shop.getLocation().getWorld().spawn(newLocation, ItemDisplay.class);

        if (shop.getItem() == null) item.setItemStack(new ItemStack(Material.BARRIER));
        else item.setItemStack(shop.getItem());

        Quaternionf rotation = new Quaternionf().rotateYXZ(
                (float) Math.toRadians(-yaw),     // Yaw (horizontal)
                (float) Math.toRadians(-pitch),   // Pitch (vertikal)
                0                                  // Roll ist 0
        );

        item.setTransformation(new Transformation(new Vector3f(0,0,0), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
    }


    public static Vector getSignFacing(Block sign) {
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
    }

}
