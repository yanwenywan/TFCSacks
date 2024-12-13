package uk.yanwenyuan.tfcsacks.common.container;

import uk.yanwenyuan.tfcsacks.common.capabilities.SackLike;
import uk.yanwenyuan.tfcsacks.common.items.ToolSackItem;
import net.dries007.tfc.common.container.CallbackSlot;
import net.dries007.tfc.common.container.ItemStackContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ToolSackContainer extends ItemStackContainer {

    public static ToolSackContainer create(ItemStack stack, InteractionHand hand, int slot, Inventory playerInv, int windowId) {
        return new ToolSackContainer(stack, hand, slot, playerInv, windowId).init(playerInv);
    }

    private final SackLike sackLike;

    private ToolSackContainer(ItemStack stack, InteractionHand hand, int slot, Inventory playerInv, int windowId) {
        super(TFCSacksContainerTypes.TOOL_SACK_CONTAINER.get(), windowId, playerInv, stack, hand, slot);

        sackLike = SackLike.getInstance(stack);
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex) {
        return switch (typeOf(slotIndex)) {
            case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, 0, ToolSackItem.SLOTS, false);
            case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
        };
    }

    @Override
    protected void addContainerSlots() {
        addSlot(new CallbackSlot(sackLike, sackLike, 0, 61, 23));
        addSlot(new CallbackSlot(sackLike, sackLike, 1, 79, 23));
        addSlot(new CallbackSlot(sackLike, sackLike, 2, 97, 23));

        addSlot(new CallbackSlot(sackLike, sackLike, 3, 61, 41));
        addSlot(new CallbackSlot(sackLike, sackLike, 4, 79, 41));
        addSlot(new CallbackSlot(sackLike, sackLike, 5, 97, 41));
    }

}
