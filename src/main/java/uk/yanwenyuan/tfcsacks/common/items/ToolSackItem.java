package uk.yanwenyuan.tfcsacks.common.items;

import uk.yanwenyuan.tfcsacks.common.capabilities.SackLike;
import uk.yanwenyuan.tfcsacks.common.container.TFCSacksContainerProviders;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.DelegateItemHandler;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.recipes.inventory.EmptyInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ToolSackItem extends Item implements IItemSize {

    public static final int SLOTS = 6;

    public ToolSackItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown() && !level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            TFCSacksContainerProviders.TOOL_SACK.openScreen(serverPlayer, hand);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (TFCConfig.CLIENT.displayItemContentsAsImages.get()) {
            SackLike sack = SackLike.getInstance(stack);
            if (sack != null) {
                return Helpers.getTooltipImage(sack, 3, 2, 0, SLOTS - 1);
            }
        }
        return super.getTooltipImage(stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ToolSackCapability(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.LARGE;
    }

    @Override
    public Weight getWeight(ItemStack stack) {
        return Weight.HEAVY;
    }

    public static class ToolSackCapability implements ICapabilityProvider, DelegateItemHandler, SackLike, EmptyInventory {
        private static final List<TagKey<Item>> ACCEPTED_TAGS = List.of(
                TFCTags.Items.USABLE_ON_TOOL_RACK, ItemTags.TOOLS, Tags.Items.TOOLS
        );

        private final ItemStack stack;
        private final ItemStackHandler inventory;

        private final LazyOptional<ToolSackCapability> capability;
        private boolean initialised = false;

        ToolSackCapability(ItemStack stack) {
            this.stack = stack;
            this.inventory = new InventoryItemHandler(this, SLOTS);

            this.capability = LazyOptional.of(() -> this);
        }

        @Override
        public int getSlotStackLimit(int slot) {
            return 1;
        }

        @Override
        public void setAndUpdateSlots(int slot) {
            final CompoundTag tag = stack.getOrCreateTag();
            tag.put("inventory", inventory.serializeNBT());
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getTags().anyMatch(ACCEPTED_TAGS::contains);
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
