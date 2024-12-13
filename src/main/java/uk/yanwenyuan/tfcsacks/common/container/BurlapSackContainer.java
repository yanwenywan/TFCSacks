package uk.yanwenyuan.tfcsacks.common.container;

import uk.yanwenyuan.tfcsacks.common.capabilities.SackLike;
import uk.yanwenyuan.tfcsacks.common.items.BurlapSackItem;
import net.dries007.tfc.common.container.CallbackSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BurlapSackContainer extends CustomItemStackContainer {

    public static BurlapSackContainer create(ItemStack stack, InteractionHand hand, int slot, Inventory playerInv, int windowId)
    {
        return new BurlapSackContainer(stack, hand, slot, playerInv, windowId).init(playerInv);
    }

    private final SackLike sackLike;

    private BurlapSackContainer(ItemStack stack, InteractionHand hand, int slot, Inventory playerInv, int windowId) {
        super(TFCSacksContainerTypes.BURLAP_SACK_CONTAINER.get(), windowId, playerInv, stack, hand, slot);

        sackLike = SackLike.getInstance(stack);
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex) {
        return switch (typeOf(slotIndex)) {
            case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, 0, BurlapSackItem.SLOTS, false);
            case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
        };
    }

    @Override
    protected void addContainerSlots()
    {
        addSlot(new CallbackSlot(sackLike, sackLike, 0, 53, 23));
        addSlot(new CallbackSlot(sackLike, sackLike, 1, 71, 23));
        addSlot(new CallbackSlot(sackLike, sackLike, 2, 89, 23));
        addSlot(new CallbackSlot(sackLike, sackLike, 3, 107, 23));

        addSlot(new CallbackSlot(sackLike, sackLike, 4, 53, 41));
        addSlot(new CallbackSlot(sackLike, sackLike, 5, 71, 41));
        addSlot(new CallbackSlot(sackLike, sackLike, 6, 89, 41));
        addSlot(new CallbackSlot(sackLike, sackLike, 7, 107, 41));
    }
}
