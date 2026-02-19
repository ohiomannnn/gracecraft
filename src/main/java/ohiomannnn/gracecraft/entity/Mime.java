package ohiomannnn.gracecraft.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Mime extends Entity {

    private static final EntityDataAccessor<Boolean> FRIENDLY;
    private static final EntityDataAccessor<String> TARGET_USER;

    public Mime(EntityType<?> entityType, Level level) {
        super(entityType, level);

        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();

        Player nearestPlayer = this.level().getNearestPlayer(this, 128);

        if (nearestPlayer != null) {

            double dx = nearestPlayer.getX() - this.getX();
            double dy = nearestPlayer.getY() - this.getY();
            double dz = nearestPlayer.getZ() - this.getZ();

            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

            double stopDistance = 2.0;
            double slowDistance = 5.0;

            if (dist > stopDistance) {
                double slowFactor = Math.min(1.0, (dist - stopDistance) / (slowDistance - stopDistance));
                double speed = Math.min(0.1, Math.sqrt(dist) * 5) * slowFactor;

                double nx = dx / dist;
                double ny = dy / dist;
                double nz = dz / dist;

                this.move(MoverType.SELF, new Vec3(nx * speed * 2, ny * speed * 2, nz * speed * 2));
            }

            this.lookAt(EntityAnchorArgument.Anchor.EYES, nearestPlayer.position());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FRIENDLY, false);
        builder.define(TARGET_USER, "Dev");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Friendly", this.isFriendly());
        tag.putString("TargetUser", this.getTargetUser());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.setFriendly(tag.getBoolean("Friendly"));
        this.setTargetUser(tag.getString("TargetUser"));
    }

    public boolean isFriendly() { return this.entityData.get(FRIENDLY); }
    public void setFriendly(boolean friendly) { this.entityData.set(FRIENDLY, friendly); }

    public String getTargetUser() { return this.entityData.get(TARGET_USER); }
    public void setTargetUser(String targetUser) { this.entityData.set(TARGET_USER, targetUser); }

    static {
        FRIENDLY = SynchedEntityData.defineId(Mime.class, EntityDataSerializers.BOOLEAN);
        TARGET_USER = SynchedEntityData.defineId(Mime.class, EntityDataSerializers.STRING);
    }
}
