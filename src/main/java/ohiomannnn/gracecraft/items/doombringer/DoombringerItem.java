package ohiomannnn.gracecraft.items.doombringer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import ohiomannnn.gracecraft.GraceCraft;
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

import java.util.List;
import java.util.function.Consumer;

public class DoombringerItem extends Item implements GeoItem {
    private static final RawAnimation ANIMATION_PAT   = RawAnimation.begin().thenPlay("animation.model.pat");
    private static final RawAnimation ANIMATION_SCREAM = RawAnimation.begin().thenPlay("animation.model.scream");
    private static final RawAnimation ANIMATION_SHUT  = RawAnimation.begin().thenPlay("animation.model.shut");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final int TO_SCREAM = 250;
    private static final int TO_EXPLOSION = 365;

    private static long StartTick = -1;
    private static boolean canShut = false;
    private static boolean soundPlayed = false;

    public DoombringerItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DoombringerRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new DoombringerRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 0, state -> PlayState.STOP)
                .triggerableAnim("anim_pat", ANIMATION_PAT)
                .triggerableAnim("anim_scream", ANIMATION_SCREAM)
                .triggerableAnim("anim_shut", ANIMATION_SHUT));
    }

    private boolean isFriendly(ItemStack stack) {
        CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
        if (custom != null) {
            CompoundTag tag = custom.copyTag();
            if (tag.contains("Friendly")) {
                return tag.getBoolean("Friendly");
            }
        }
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack stack = player.getItemInHand(hand);

            Long id = GeoItem.getId(stack);

            if (id == null) {
                id = GeoItem.getOrAssignId(stack, serverLevel);
            }

            if (canShut) {
                if (!isFriendly(stack)) {
                    triggerAnim(player, id, "main_controller", "anim_shut");
                    canShut = false;
                    StartTick = -1;

                    for (ServerPlayer sp : serverLevel.players()) {
                        sp.connection.send(new ClientboundStopSoundPacket(
                                ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "joey_scream"),
                                SoundSource.PLAYERS
                        ));
                    }
                } else {
                    triggerAnim(player, id, "main_controller", "anim_pat");
                }
            } else {
                triggerAnim(player, id, "main_controller", "anim_pat");
                if (!isFriendly(stack)) {
                    StartTick = -1;
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player)) return;
        if (level instanceof ClientLevel) return;
        if (isFriendly(stack)) return;

        if (StartTick == -1) {
            StartTick = level.getGameTime();
            soundPlayed = false;
        }

        long gameTicks = level.getGameTime() - StartTick;

        if (gameTicks == TO_SCREAM) {
            if (level instanceof ServerLevel serverLevel) {
                if (!soundPlayed) {
                    if (selected) {
                        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                                InitSounds.JOEY_SCREAM.get(), SoundSource.PLAYERS);
                        soundPlayed = true;
                    } else {
                        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                                InitSounds.JOEY_SCREAM.get(), SoundSource.PLAYERS,
                                0.5f, 1.0f);
                        soundPlayed = true;
                    }
                }

                Long id = GeoItem.getId(stack);
                if (id != null) {
                    triggerAnim(player, id, "main_controller", "anim_scream");
                    canShut = true;
                }
            }
        }

        if (gameTicks >= TO_EXPLOSION) {
            StartTick = -1;

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stackToDel = player.getInventory().getItem(i);
                if (stackToDel.getItem() instanceof DoombringerItem && !isFriendly(stackToDel)) {
                    DoombringerItem.StartTick = -1;
                    DoombringerItem.canShut = false;
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }

            level.explode(
                    null,
                    player.getX(), player.getY() + 2, player.getZ(),
                    3.0f,
                    false,
                    Level.ExplosionInteraction.NONE
            );

            DamageSources dsProvider = new DamageSources(level.registryAccess());
            DamageSource suicideSource = dsProvider.explosion(player, player);

            player.hurt(suicideSource, Float.MAX_VALUE);
        }

        super.inventoryTick(stack, level, entity, slot, selected);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (isFriendly(stack)) {
            tooltip.add(Component.translatable("tooltip.gracecraft.doombringer.friendly")
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("tooltip.gracecraft.doombringer.notFriendly")
                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}


