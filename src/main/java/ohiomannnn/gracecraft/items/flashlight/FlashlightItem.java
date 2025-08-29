package ohiomannnn.gracecraft.items.flashlight;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import ohiomannnn.gracecraft.items.doombringer.DoombringerRenderer;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FlashlightItem extends Item implements GeoItem {
    private static final RawAnimation ANIMATION_NONE = RawAnimation.begin().thenPlay("animation.model.none");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlashlightItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private FlashlightRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new FlashlightRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 0, state -> PlayState.STOP)
                .triggerableAnim("anim_none", ANIMATION_NONE));
    }
    private static final Map<UUID, BlockPos> LAST_POS = new ConcurrentHashMap<>();

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slot, boolean selected) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;
        if (level instanceof ServerLevel serverLevel) {
            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel),"main_controller","anim_none");
        }

        UUID uuid = player.getUUID();

        if (selected && !player.isSpectator()) {
            BlockPos targetPos;

            HitResult hit = player.pick(8.0D, 0.0F, true);

            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos hitPos = ((BlockHitResult) hit).getBlockPos();
                Direction face = ((BlockHitResult) hit).getDirection();

                BlockPos abovePos = hitPos.above();

                VoxelShape shape = level.getBlockState(hitPos).getShape(level, hitPos);
                VoxelShape aboveShape = level.getBlockState(abovePos).getShape(level, abovePos);

                if (!aboveShape.isEmpty() && aboveShape.bounds().getYsize() <= 0.125) {
                    targetPos = hitPos.above(2);
                } else if (!aboveShape.isEmpty() && aboveShape.bounds().getYsize() < 1.0) {
                    targetPos = hitPos.relative(face,2);
                } else if (!shape.isEmpty() && shape.bounds().getYsize() < 1.0) {
                    targetPos = hitPos.above();
                } else {
                    targetPos = hitPos.relative(face);
                }
            } else {
                Vec3 eyePos = player.getEyePosition();
                Vec3 lookVec = player.getLookAngle().normalize().scale(8.0D);
                targetPos = BlockPos.containing(eyePos.add(lookVec));
            }

            BlockPos prev = LAST_POS.get(uuid);

            if (prev != null && !prev.equals(targetPos) && level.getBlockState(prev).is(Blocks.LIGHT)) {
                level.setBlock(prev, Blocks.AIR.defaultBlockState(), 3);
            }
            BlockState lightState = Blocks.LIGHT.defaultBlockState()
                    .setValue(LightBlock.LEVEL, 12);

            if (level.getBlockState(targetPos).isAir() || level.getBlockState(targetPos).is(Blocks.LIGHT)) {
                level.setBlock(targetPos, lightState, 3);
                LAST_POS.put(uuid, targetPos);
            }

        } else {
            BlockPos prev = LAST_POS.remove(uuid);
            if (prev != null && level.getBlockState(prev).is(Blocks.LIGHT)) {
                level.setBlock(prev, Blocks.AIR.defaultBlockState(), 3);
            }
        }

        super.inventoryTick(stack, level, entity, slot, selected);
    }
    public static float randFloat(Float rangeMin, Float rangeMax) {
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextFloat();
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && !player.isUnderWater()) {
            level.playSound(null,
                    player.getX(), player.getY(), player.getZ(),
                    InitSounds.FLASH_SOUND.get(),
                    SoundSource.PLAYERS,
                    1.0f, randFloat(1.0f, 1.5f));

            BlockPos pos = LAST_POS.get(player.getUUID());
            if (pos != null && level.getBlockState(pos).is(Blocks.LIGHT)) {
                level.setBlock(pos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 13), 3);
                level.scheduleTick(pos, Blocks.LIGHT, 20);
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
