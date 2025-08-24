package ohiomannnn.gracecraft.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.items.InitItems;

import java.util.function.Supplier;

public class InitCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GraceCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> GRACE_ITEMS_TAB = CREATIVE_MOD_TAB.register("grace_items_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(InitItems.FLASHLIGHT.get()))
            .title(Component.translatable("creative." + GraceCraft.MOD_ID + ".grace_items_tab"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(InitItems.FLASHLIGHT);
                output.accept(InitItems.DOOMBRINGER);
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TAB.register(eventBus);
    }
}
