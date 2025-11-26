package ohiomannnn.gracecraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Mime extends Entity {

    private static final EntityDataAccessor<Boolean> FRIENDLY;

    public Mime(EntityType<?> entityType, Level level) {
        super(entityType, level);

        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();

        Player nearestPlayer = this.level().getNearestPlayer(
                this.getX(),
                this.getY(),
                this.getZ(),
                64.0,
                false
        );

        if (nearestPlayer == null) return;

        double dx = nearestPlayer.getX() - this.getX();
        double dy = nearestPlayer.getY() - this.getEyeY();
        double dz = nearestPlayer.getZ() - this.getZ();

        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);

        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));

        this.setYRot(yaw);
        this.setXRot(pitch);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FRIENDLY, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Friendly", this.isFriendly());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.setFriendly(tag.getBoolean("Friendly"));
    }

    public boolean isFriendly() { return this.entityData.get(FRIENDLY); }
    public void setFriendly(boolean friendly) { this.entityData.set(FRIENDLY, friendly); }

    static {
        FRIENDLY = SynchedEntityData.defineId(Mime.class, EntityDataSerializers.BOOLEAN);
    }
}
