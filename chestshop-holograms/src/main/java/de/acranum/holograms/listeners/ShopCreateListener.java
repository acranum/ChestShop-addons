package de.acranum.holograms.listeners;

import de.acranum.chestshop.api.ChestShopAPI;
import de.acranum.chestshop.api.ChestShopProvider;
import de.acranum.chestshop.api.events.PostShopCreateEvent;
import de.acranum.chestshop.api.shop.Shop;
import de.acranum.holograms.Holograms;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
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
        Vector offset = getSignOffset(shop.getSign());
        BlockFace facing = getSignFacing(shop.getSign());

        Location newLocation = new Location(shop.getLocation().getWorld(), shop.getLocation().getBlockX() + 0.5 + offset.getBlockX(), shop.getSign().getY() + 1.2 + offset.getY(), shop.getLocation().getBlockZ() + 0.5 + offset.getBlockZ());

        Vector faceVec = facing.getDirection();
        float yaw = (float) Math.toDegrees(Math.atan2(-faceVec.getX(), faceVec.getZ()));

        ItemDisplay item = shop.getLocation().getWorld().spawn(newLocation, ItemDisplay.class);
        if (Holograms.getHologramKey() != null) {
            item.getPersistentDataContainer().set(Holograms.getHologramKey(), PersistentDataType.BYTE, (byte) 1);
        }

        if (shop.getItem() == null) item.setItemStack(new ItemStack(Material.BARRIER));
        else {
            ItemStack itemStack = shop.getItem().clone();
            itemStack.setAmount(1);
            item.setItemStack(itemStack);
        }

        Quaternionf rotation = new Quaternionf().rotateY((float) Math.toRadians(-yaw));
        item.setTransformation(new Transformation(new Vector3f(0,0,0), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
    }


    public static Vector getSignOffset(Block sign) {
        if (sign.getBlockData() instanceof HangingSign) {
            return new Vector(0, 0.2, 0);
        } else if (sign.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            return new Vector(0, 0.2, 0);
        } else if (sign.getBlockData() instanceof WallHangingSign) {
            return new Vector(0, 0.2, 0);
        }
        //Wallsign
        BlockFace facing = getSignFacing(sign);
        return new Vector(facing.getModX(), 0, facing.getModZ());
    }

    public static BlockFace getSignFacing(Block sign) {
        if (sign.getBlockData() instanceof WallSign wallSign) {
            return wallSign.getFacing().getOppositeFace();
        } else if (sign.getBlockData() instanceof HangingSign hangingSign) {
            return hangingSign.getRotation().getOppositeFace();
        } else if (sign.getBlockData() instanceof WallHangingSign wallHangingSign) {
            return wallHangingSign.getFacing().getOppositeFace();
        } else if (sign.getBlockData() instanceof org.bukkit.block.data.type.Sign standingSign) {
            return standingSign.getRotation().getOppositeFace();
        }
        return BlockFace.NORTH;
    }

}
