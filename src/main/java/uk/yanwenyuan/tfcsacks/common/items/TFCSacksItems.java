package uk.yanwenyuan.tfcsacks.common.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

import static uk.yanwenyuan.tfcsacks.TFCSacks.MOD_ID;

public final class TFCSacksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> BURLAP_SACK = register("burlap_sack", () -> new BurlapSackItem(new Item.Properties()));
    public static final RegistryObject<Item> TOOL_SACK = register("tool_sack", () -> new ToolSackItem(new Item.Properties()));


    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
