package ohiomannnn.gracecraft.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemCheck {

    public static boolean isHoldingItem(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        return !mainHand.isEmpty() || !offHand.isEmpty();
    }
}