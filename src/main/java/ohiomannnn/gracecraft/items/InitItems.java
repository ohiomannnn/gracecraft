package ohiomannnn.gracecraft.items;


import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.items.doombringer.DoombringerItem;
import ohiomannnn.gracecraft.items.flashlight.FlashlightItem;

public class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GraceCraft.MOD_ID);

    public static final DeferredItem<Item> FLASHLIGHT = ITEMS.register("flashlight",
            () -> new FlashlightItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOOMBRINGER = ITEMS.register("doombringer",
            () -> new DoombringerItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
