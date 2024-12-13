package uk.yanwenyuan.tfcsacks.common.container;

import net.dries007.tfc.common.container.ItemStackContainer;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static uk.yanwenyuan.tfcsacks.TFCSacks.MOD_ID;

public final class TFCSacksContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public static final RegistryObject<MenuType<BurlapSackContainer>> BURLAP_SACK_CONTAINER = registerItem("burlap_sack_container", BurlapSackContainer::create);
    public static final RegistryObject<MenuType<ToolSackContainer>> TOOL_SACK_CONTAINER = registerItem("tool_pack_container", ToolSackContainer::create);

    private static <C extends ItemStackContainer> RegistryObject<MenuType<C>> registerItem(String name, ItemStackContainer.Factory<C> factory)
    {
        return RegistrationHelpers.registerItemStackContainer(CONTAINERS, name, factory);
    }
}
