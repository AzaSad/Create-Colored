package com.azasad.createcolored.content;

import com.azasad.createcolored.CreateColored;

import com.azasad.createcolored.content.block.ColoredBlocks;
import com.simibubi.create.foundation.utility.Components;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import com.simibubi.create.AllCreativeModeTabs.TabInfo;
import net.minecraft.resource.metadata.BlockEntry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ColoredCreativeTabs {
    public static final RegistryKey<ItemGroup> COLORED_CREATIVE_TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(CreateColored.MOD_ID,"colored_creative_tab"));

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, COLORED_CREATIVE_TAB,
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ColoredBlocks.DYED_PIPES.get(DyeColor.ORANGE)))
                        .displayName(Text.translatable(("itemGroup.create-colored.creative_tab")))
                        .entries((context, entries) -> {
                            for(DyeColor color : DyeColor.values()) {
                                entries.add(ColoredBlocks.DYED_PIPES.get(color).asStack());
                            }
                        })
                        .build());
    }
}

