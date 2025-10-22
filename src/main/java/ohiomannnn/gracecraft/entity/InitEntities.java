package ohiomannnn.gracecraft.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ohiomannnn.gracecraft.GraceCraft;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, GraceCraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Sorrow>> SORROW =
            ENTITY_TYPES.register("sorrow",
                    () -> EntityType.Builder.of(Sorrow::new, MobCategory.MISC)
                            .build("sorrow"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
