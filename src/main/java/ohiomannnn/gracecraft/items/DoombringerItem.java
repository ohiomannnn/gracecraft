package ohiomannnn.gracecraft.items;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.jetbrains.annotations.NotNull;

public class DoombringerItem extends Item {

    public DoombringerItem(Item.Properties properties) {
        super(properties);
    }
    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player)) return;

        if (selected && !player.isSpectator()) {
            if (level.isClientSide) {
                if (level.random.nextFloat() < 0.01f) {
                    level.playSound(
                            player,
                            player.getX(), player.getY(), player.getZ(),
                            InitSounds.DOOM_SQUEAK.get(),
                            SoundSource.PLAYERS,
                            1.0f, 1.0f
                    );
                }
            }
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.neoforged.neoforge.event.EventHooks.onArrowNock(itemstack, level, player, hand, flag);
        if (ret != null) return ret;

        if (!player.hasInfiniteMaterials() && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH;
    }
}

