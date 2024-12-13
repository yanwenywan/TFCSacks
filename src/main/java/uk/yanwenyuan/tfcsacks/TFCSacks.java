package uk.yanwenyuan.tfcsacks;

import uk.yanwenyuan.tfcsacks.client.screen.BurlapSackInventoryScreen;
import uk.yanwenyuan.tfcsacks.client.screen.ToolSackInventoryScreen;
import uk.yanwenyuan.tfcsacks.common.container.TFCSacksContainerTypes;
import uk.yanwenyuan.tfcsacks.common.items.TFCSacksItems;
import uk.yanwenyuan.tfcsacks.datagen.TFCSacksItemModels;
import uk.yanwenyuan.tfcsacks.datagen.TFCSacksRecipes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TFCSacks.MOD_ID)
public class TFCSacks {
    public static final String MOD_ID = "tfcsacks";

    public TFCSacks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);

        TFCSacksItems.ITEMS.register(bus);
        TFCSacksContainerTypes.CONTAINERS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::addCreative);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(TFCSacksItems.BURLAP_SACK);
            event.accept(TFCSacksItems.TOOL_SACK);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(TFCSacksContainerTypes.BURLAP_SACK_CONTAINER.get(), BurlapSackInventoryScreen::new);
            MenuScreens.register(TFCSacksContainerTypes.TOOL_SACK_CONTAINER.get(), ToolSackInventoryScreen::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DataGen {

        @SubscribeEvent
        public static void makeDatagens(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            PackOutput packOutput = generator.getPackOutput();
            ExistingFileHelper fileHelper = event.getExistingFileHelper();
            //CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            generator.addProvider(event.includeServer(), new TFCSacksRecipes(packOutput));

            generator.addProvider(event.includeClient(), new TFCSacksItemModels(packOutput, fileHelper));

        }
    }
}
