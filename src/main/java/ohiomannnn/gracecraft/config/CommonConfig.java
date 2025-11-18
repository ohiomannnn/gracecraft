package ohiomannnn.gracecraft.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {

    public final ModConfigSpec.BooleanValue SPAWN_ENTITY_RANDOM;

    public final ModConfigSpec.IntValue DOZER_SPAWN;
    public final ModConfigSpec.IntValue LITANY_SPAWN;

    public final ModConfigSpec.IntValue RAND_VALUE;

    CommonConfig(ModConfigSpec.Builder builder) {
        SPAWN_ENTITY_RANDOM = builder
                .comment("Toggle entity randomly spawning.")
                .translation("gracecraft.configuration.spawn_entity_random")
                .define("spawn_entity_random", false);
        DOZER_SPAWN = builder
                .comment("How many world ticks need to pass for a Dozer spawn to be performed.")
                .translation("gracecraft.configuration.dozer_spawn")
                .defineInRange("dozer_spawn", 1200, 0, Integer.MAX_VALUE);
        LITANY_SPAWN = builder
                .comment("How many world ticks need to pass for a Litany spawn to be performed.")
                .translation("gracecraft.configuration.litany_spawn")
                .defineInRange("litany_spawn", 800, 0, Integer.MAX_VALUE);
        RAND_VALUE = builder
                .comment("How much random ticks will be added to the spawn countdown")
                .translation("gracecraft.configuration.rand_value")
                .defineInRange("rand_value", 150, 0, Integer.MAX_VALUE);
    }
}
