package ohiomannnn.gracecraft.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import ohiomannnn.gracecraft.GraceCraft;

public class InitDamageTypes {
    public static final ResourceKey<DamageType> DOZER_ATTACK =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "dozer_attack"));
    public static final ResourceKey<DamageType> LITANY_ATTACK =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "litany_attack"));
    public static final ResourceKey<DamageType> KOOKOO_ATTACK =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kookoo_attack"));
}
