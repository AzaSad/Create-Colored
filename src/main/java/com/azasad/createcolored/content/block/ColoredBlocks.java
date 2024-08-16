package com.azasad.createcolored.content.block;

import com.azasad.createcolored.ColoredHelpers;
import com.azasad.createcolored.CreateColored;
import com.azasad.createcolored.content.ColoredCreativeTabs;
import com.azasad.createcolored.content.ColoredTags;
import com.azasad.createcolored.content.models.ColoredPipeAttachmentModel;
import com.azasad.createcolored.datagen.ColoredBlockStateGen;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

//TODO: Adjust pipe logic so they don't connect like normal pipes
//TODO: Adjust textures for each pipe
//DONE: Adjust render logic to use shaders
public class ColoredBlocks {
    private static final CreateRegistrate REGISTRATE = CreateColored.REGISTRATE;

    public static final DyedBlockList<ColoredFluidPipeBlock> DYED_PIPES = new DyedBlockList<ColoredFluidPipeBlock>(dyeColor -> {
        String colorName = dyeColor.getName();
        return REGISTRATE.block(colorName + "_fluid_pipe", settings -> new ColoredFluidPipeBlock(settings, dyeColor))
                .initialProperties(SharedProperties::copperMetal)
                .properties(p -> p.mapColor(dyeColor.getMapColor()).solid())
                .transform(pickaxeOnly())
                .onRegister(CreateRegistrate.blockModel(() -> (model) -> new ColoredPipeAttachmentModel(model, dyeColor)))
                .blockstate(ColoredBlockStateGen.coloredPipe(dyeColor))
                .item()
                    .model((c,p) -> { p.withExistingParent(c.getName(), Create.asResource("item/fluid_pipe")).texture("1", "block/pipes/" + colorName); })
                    .recipe((c,p) -> {
                        ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
                                .input(ColoredHelpers.getDyeItem(dyeColor))
                                .input(AllBlocks.FLUID_PIPE.asItem())
                                .criterion("has_pipe", InventoryChangedCriterion.Conditions.items(AllBlocks.FLUID_PIPE));

                        builder.offerTo(p, new Identifier(CreateColored.MOD_ID, c.getName()));
                    })
                    .tag(ColoredTags.ColoredItemTags.PIPES.tag)
                    .build()
                .register();
    });

    public static final DyedBlockList<ColoredGlassFluidPipeBlock> DYED_GLASS_PIPES = new DyedBlockList<ColoredGlassFluidPipeBlock>(dyeColor -> {
        String colorName = dyeColor.getName();
        return REGISTRATE.block(colorName + "_glass_fluid_pipe", settings -> new ColoredGlassFluidPipeBlock(settings, dyeColor))
                .initialProperties(SharedProperties::copperMetal)
                .properties(AbstractBlock.Settings::solid)
                .addLayer(() -> RenderLayer::getCutoutMipped)
                .transform(pickaxeOnly())
                .blockstate((c,p) -> {
                    p.getVariantBuilder(c.getEntry())
                            .forAllStatesExcept(state -> {
                                Direction.Axis axis = state.get(Properties.AXIS);
                                return ConfiguredModel.builder()
                                        .modelFile(p.models().
                                                withExistingParent("block/" + colorName + "_fluid_pipe/window", Create.asResource("block/fluid_pipe/window"))
                                                .texture("0", "block/glass_fluid_pipe/" + colorName))
                                        .uvLock(false)
                                        .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                        .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                        .build();
                            }, Properties.WATERLOGGED);
                })
                .onRegister(CreateRegistrate.blockModel(() -> (model) -> new ColoredPipeAttachmentModel(model, dyeColor)))
                .loot((p, b) -> p.addDrop(b, DYED_PIPES.get(dyeColor).get()))
                .register();
    });

    public static final DyedBlockList<ColoredEncasedPipeBlock> DYED_ENCASED_PIPES = new DyedBlockList<ColoredEncasedPipeBlock>(dyeColor -> {
        String colorName = dyeColor.getName();
        return REGISTRATE.block(colorName + "_encased_fluid_pipe", p -> new ColoredEncasedPipeBlock(p, AllBlocks.COPPER_CASING::get, dyeColor))
                .initialProperties(SharedProperties::copperMetal)
                .properties(p -> p.nonOpaque().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                .transform(axeOrPickaxe())
                .blockstate(ColoredBlockStateGen.encasedPipe(dyeColor))
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING,
                        (s,f) -> !s.get(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
                .loot((p, b) -> p.addDrop(b, DYED_PIPES.get(dyeColor).get()))
                .transform(EncasingRegistry.addVariantTo(DYED_PIPES.get(dyeColor)))
                .register();
    });

    public static void initialize() {

    }
}
