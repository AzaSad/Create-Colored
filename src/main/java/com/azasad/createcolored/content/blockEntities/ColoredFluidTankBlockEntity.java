package com.azasad.createcolored.content.blockEntities;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ColoredFluidTankBlockEntity extends FluidTankBlockEntity {
    public ColoredFluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}