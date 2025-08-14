package ohiomannnn.gracecraft.sounds;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import ohiomannnn.gracecraft.GraceCraft;

import java.util.function.Supplier;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, GraceCraft.MOD_ID);

    public static final Supplier<SoundEvent> FLASH_SOUND = registerSoundEvent("flash_sound");
    public static final Supplier<SoundEvent> DOZY_ATTACK = registerSoundEvent("dozy_attack");
    public static final Supplier<SoundEvent> DOZY_ATTACK_KILL = registerSoundEvent("dozy_attack_kill");
    public static final Supplier<SoundEvent> LITANY_ATTACK = registerSoundEvent("litany_attack");
    public static final Supplier<SoundEvent> LITANY_ATTACK_FAST = registerSoundEvent("litany_attack_fast");
    public static final Supplier<SoundEvent> LITANY_ATTACK_JUMPSCARE = registerSoundEvent("litany_attack_jumpscare");
    public static final Supplier<SoundEvent> LITANY_ATTACK_KILL = registerSoundEvent("litany_attack_kill");


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}