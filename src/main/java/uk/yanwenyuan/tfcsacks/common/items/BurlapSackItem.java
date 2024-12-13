package uk.yanwenyuan.tfcsacks.common.items;

import uk.yanwenyuan.tfcsacks.common.capabilities.SackLike;
import uk.yanwenyuan.tfcsacks.common.container.TFCSacksContainerProviders;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.DelegateItemHandler;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.recipes.inventory.EmptyInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BurlapSackItem extends Item implements IItemSize {

    public static final int SLOTS = 8;

    public BurlapSackItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown() && !level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            TFCSacksContainerProviders.BURLAP_SACK.openScreen(serverPlayer, hand);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BurlapSackCapability(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (TFCConfig.CLIENT.displayItemContentsAsImages.get()) {
            SackLike sack = SackLike.getInstance(stack);
            if (sack != null) {
                return Helpers.getTooltipImage(sack, 4, 2, 0, SLOTS-1);
            }
        }
        return super.getTooltipImage(stack);
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.LARGE;
    }

    @Override
    public Weight getWeight(ItemStack stack) {
        return Weight.HEAVY;
    }

    public static class BurlapSackCapability implements ICapabilityProvider, DelegateItemHandler, SackLike, EmptyInventory {
        private final ItemStack stack;
        private final ItemStackHandler inventory;

        private final LazyOptional<BurlapSackCapability> capability;
        private boolean initialised = false;

        BurlapSackCapability(ItemStack stack) {
            this.stack = stack;
            this.inventory = new InventoryItemHandler(this, SLOTS);

            this.capability = LazyOptional.of(() -> this);
        }

        @Override
        public int getSlotStackLimit(int slot) {
            return 32;
        }

        @Override
        public void setAndUpdateSlots(int slot) {
            final CompoundTag tag = stack.getOrCreateTag();
            tag.put("inventory", inventory.serializeNBT());
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return ItemSizeManager.get(stack).getSize(stack).isEqualOrSmallerThan(Size.SMALL);
        }

        @Override
        public IItemHandlerModifiable getItemHandler() {
            return inventory;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
            if (cap == Capabilities.ITEM) {
                loadCapability();
                return capability.cast();
            }

            return LazyOptional.empty();
        }

        private void loadCapability() {
            if (initialised) {
                return;
            }
            initialised = true;

            final CompoundTag tag = stack.getOrCreateTag();
            inventory.deserializeNBT(tag.getCompound("inventory"));
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            inventory.setStackInSlot(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return inventory.insertItem(slot, stack.copy(), simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return inventory.extractItem(slot, amount, simulate);
        }
    }
}
