package ohiomannnn.gracecraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import ohiomannnn.gracecraft.misc.InitDamageTypes;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.List;

public class Sorrow extends Entity {

    public boolean didShake = false;
    public boolean sound = true;
    public boolean sound2 = true;

    public Sorrow(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos center = new BlockPos(getBlockX(), getBlockY(), getBlockZ());
        AABB area = new AABB(center).inflate(200);
        List<Entity> entities = level().getEntities(null, area);

        for (Entity entity : entities) {
            if (sound) {
                level().playSound(null, getX(), getY(), getZ(), InitSounds.SORROW_RAIN.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                sound = false;
            }
            if (tickCount > 160 && entity.isAlive() && sound2) {
                level().playSound(null, getX(), getY(), getZ(), InitSounds.SORROW_GONE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                sound2 = false;
            }
            if (entity instanceof LivingEntity living && tickCount > 155) {
                BlockPos posAbove = living.blockPosition().above();
                if (level().canSeeSky(posAbove)) {
                    living.hurt(level().damageSources().source(InitDamageTypes.SORROW_ATTACK), 250F);
                }
            }
            if (entity instanceof ItemEntity itemEntity) {
                itemEntity.discard();
            }
        }

        if (tickCount > 160) {
            this.discard();
            sound = true;
            sound2 = true;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {}
}
