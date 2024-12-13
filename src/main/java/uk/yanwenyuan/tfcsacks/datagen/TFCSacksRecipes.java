package uk.yanwenyuan.tfcsacks.datagen;

import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static uk.yanwenyuan.tfcsacks.common.items.TFCSacksItems.BURLAP_SACK;
import static uk.yanwenyuan.tfcsacks.common.items.TFCSacksItems.TOOL_SACK;

@ParametersAreNonnullByDefault
public class TFCSacksRecipes extends RecipeProvider implements IConditionBuilder {

    public TFCSacksRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    // hack because tfc doesn't seem to expose the high qual cloth tag
    private static final TagKey<Item> HIGH_QUAL_CLOTH = TagKey.create(Registries.ITEM, new ResourceLocation("tfc", "high_quality_cloth"));

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BURLAP_SACK.get())
                .pattern(" S ")
                .pattern("B|B")
                .pattern("BBB")
                .define('S', Tags.Items.STRING)
                .define('B', TFCItems.BURLAP_CLOTH.get())
                .define('|', Tags.Items.RODS_WOODEN)
                .unlockedBy(getHasName(TFCItems.BURLAP_CLOTH.get()), has(TFCItems.BURLAP_CLOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TOOL_SACK.get())
                .pattern(" C ")
                .pattern("LCL")
                .pattern("HC ")
                .define('C', HIGH_QUAL_CLOTH)
                .define('L', Items.LEATHER)
                .define('H', TFCItems.METAL_ITEMS.get(Metal.Default.WROUGHT_IRON).get(Metal.ItemType.FISH_HOOK).get())
                .unlockedBy(getHasName(TFCItems.WOOL_CLOTH.get()), has(TFCItems.WOOL_CLOTH.get()))
                .unlockedBy(getHasName(TFCItems.SILK_CLOTH.get()), has(TFCItems.SILK_CLOTH.get()))
                .save(pWriter);
    }
}
