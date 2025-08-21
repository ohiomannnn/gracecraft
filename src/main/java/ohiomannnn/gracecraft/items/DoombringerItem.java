package ohiomannnn.gracecraft.items;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import ohiomannnn.gracecraft.sounds.InitSounds;

public class DoombringerItem extends Item {

    public DoombringerItem(Item.Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.random.nextFloat() < 0.10f) {
            level.playSound(
                    player,
                    player.getX(), player.getY(), player.getZ(),
                    InitSounds.DOOM_SQUEAK.get(),
                    SoundSource.PLAYERS,
                    1.0f, 1.0f
            );
        }
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 30;
    }
}

