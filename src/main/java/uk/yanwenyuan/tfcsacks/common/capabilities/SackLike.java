package uk.yanwenyuan.tfcsacks.common.capabilities;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.container.ISlotCallback;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public interface SackLike extends IItemHandler, ISlotCallback {

    static SackLike getInstance(@NotNull final ItemStack stack) {
        return Helpers.getCapability(stack, Capabilities.ITEM) instanceof SackLike sack ? sack : null;
    }

    @Override
    boolean isItemValid(int i, @NotNull ItemStack itemStack);


}
