package ohiomannnn.gracecraft.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.function.Supplier;

public class GuiLoopingSoundInstance extends AbstractTickableSoundInstance {

    private final Supplier<Boolean> stopCondition;

    public GuiLoopingSoundInstance(SoundEvent soundEvent, float volume, float pitch,
                                   Supplier<Boolean> stopCondition) {
        super(soundEvent, SoundSource.MASTER, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.pitch = pitch;
        this.relative = true;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.stopCondition = stopCondition;
    }

    @Override
    public void tick() {
        if (stopCondition.get()) {
            this.stop();
        }
    }
}