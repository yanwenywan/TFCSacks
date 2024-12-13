package uk.yanwenyuan.tfcsacks.common.container;

import net.dries007.tfc.common.container.ItemStackContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CustomItemStackContainer extends ItemStackContainer {

    protected CustomItemStackContainer(MenuType<?> type, int windowId, Inventory playerInv, ItemStack stack, InteractionHand hand, int slot) {
        super(type, windowId, playerInv, stack, hand, slot);
    }

    @Override
    public void clicked(int slot, int dragType, ClickType clickType, Player player) {

        // I want double click functionality in the container, which TFC explicitly disables
        // (because it may pick up the opened container and screw it up)
        if (clickType == ClickType.PICKUP_ALL && slot >= 0) {
            customDoubleClickPickupAction(slot, dragType, player);
            return;
        }

        super.clicked(slot, dragType, clickType, player);
    }

    private void customDoubleClickPickupAction(int slotIndex, int dragType, Player player) {
        Slot currentSlot = slots.get(slotIndex);
        ItemStack currentStack = this.getCarried();

        // copied and "cleaned up" from minecraft code (AbstractContainerMenu)
        if (currentStack.isEmpty() || (currentSlot.hasItem() && currentSlot.mayPickup(player))) {
            return;
        }

        int from = dragType == 0 ? 0 : slots.size() - 1;
        int direction = dragType == 0 ? 1 : -1;

        // no, I don't know what k does, it only goes 0, 1
        for (int k = 0; k < 2; k++) {
            for (int i = from; i >= 0 && i < slots.size() && currentStack.getCount() < currentStack.getMaxStackSize(); i += direction) {

                if (i == itemIndex) continue;

                Slot invSlot = slots.get(i);

                if (invSlot.hasItem()
                        && canItemQuickReplace(invSlot, currentStack, true)
                        && invSlot.mayPickup(player) && canTakeItemForPickAll(currentStack, invSlot)
                ) {
                    ItemStack otherStack = invSlot.getItem();
                    if (k != 0 || otherStack.getCount() != otherStack.getMaxStackSize()) {
                        ItemStack stackDifference = invSlot.safeTake(
                                otherStack.getCount(),
                                currentStack.getMaxStackSize() - currentStack.getCount(),
                                player);
                        currentStack.grow(stackDifference.getCount());
                    }
                }
            }
        }
    }

}
