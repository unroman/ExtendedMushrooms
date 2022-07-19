package cech12.extendedmushrooms.block;

import cech12.extendedmushrooms.blockentity.VariantTrappedChestBlockEntity;
import cech12.extendedmushrooms.init.ModBlockEntityTypes;
import cech12.extendedmushrooms.item.MushroomWoodType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;

public class VariantTrappedChestBlock extends ChestBlock {

    private final MushroomWoodType woodType;

    public VariantTrappedChestBlock(MushroomWoodType woodType, Properties properties) {
        super(properties, () -> ModBlockEntityTypes.VARIANT_TRAPPED_CHEST.get());
        this.woodType = woodType;
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new VariantTrappedChestBlockEntity(pos, state);
    }

    public MushroomWoodType getWoodType() {
        return this.woodType;
    }

    @Override
    @Nonnull
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getSignal(@Nonnull BlockState blockState, @Nonnull BlockGetter blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(blockAccess, pos), 0, 15);
    }

    @Override
    public int getDirectSignal(@Nonnull BlockState blockState, @Nonnull BlockGetter blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return side == Direction.UP ? blockState.getSignal(blockAccess, pos, side) : 0;
    }
}
