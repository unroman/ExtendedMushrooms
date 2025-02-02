package cech12.extendedmushrooms.world.level.levelgen.feature;

import cech12.extendedmushrooms.world.level.levelgen.feature.configurations.ExtendedMushroomFeatureConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

public abstract class MegaMushroomFeature extends SingleBigMushroomFeature {

    public MegaMushroomFeature(Codec<ExtendedMushroomFeatureConfiguration> config) {
        super(config);
    }

    @Override
    protected int getSize(RandomSource random) {
        int i = random.nextInt(5) + 10;
        if (random.nextInt(12) == 0) {
            i = random.nextInt(7) + 15;
        }
        return i;
    }

    @Override
    protected boolean hasValidGround(LevelAccessor level, BlockPos mushroomPos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(mushroomPos.getX(), mushroomPos.getY(), mushroomPos.getZ());
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                if (!super.hasValidGround(level, mutableBlockPos.set(mushroomPos).move(x, 0, z))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected boolean canPlaceTrunk(LevelAccessor level, BlockPos blockPos, int size, BlockPos.MutableBlockPos mutableBlockPos, ExtendedMushroomFeatureConfiguration config) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                mutableBlockPos.set(blockPos).move(x, 0, z);
                if (!super.canPlaceTrunk(level, new BlockPos(mutableBlockPos), size, mutableBlockPos, config)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void placeTrunk(LevelAccessor level, RandomSource random, BlockPos blockPos, ExtendedMushroomFeatureConfiguration config, int size, BlockPos.MutableBlockPos mutableBlockPos) {
        for(int y = 0; y < size; ++y) {
            for (int x = 0; x < 2; x++) {
                for (int z = 0; z < 2; z++) {
                    mutableBlockPos.set(blockPos).move(x, y, z);
                    if (isReplaceable(level, mutableBlockPos)) {
                        this.setBlock(level, mutableBlockPos, config.stemProvider().getState(random, blockPos));
                    }
                }
            }
        }
    }

}
