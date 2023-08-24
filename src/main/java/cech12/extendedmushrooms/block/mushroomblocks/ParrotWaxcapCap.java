package cech12.extendedmushrooms.block.mushroomblocks;

import cech12.extendedmushrooms.item.MushroomType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ParrotWaxcapCap extends AbstractEffectMushroomCap {

    public ParrotWaxcapCap(MushroomType type, Properties properties) {
        super(type, properties.noOcclusion().jumpFactor(1.5F).friction(0.8F));
    }

    @Override
    public boolean isSlimeBlock(BlockState state) {
        return true;
    }

    @Override
    public boolean isStickyBlock(BlockState state) {
        return true;
    }

    @Override
    public boolean canStickTo(BlockState state, BlockState other) {
        if (other.getBlock() == Blocks.HONEY_BLOCK || other.getBlock() == MushroomType.HONEY_WAXCAP.getCapBlock()) return false;
        return state.isStickyBlock() || other.isStickyBlock();
    }

    @Nonnull
    @Override
    protected List<MobEffectInstance> getEffects(@Nonnull RandomSource random) {
        int duration = 200 + random.nextInt(200);
        if (random.nextInt(100) == 0) {
            duration += 1200;
        }
        List<MobEffectInstance> effects = new LinkedList<>();
        effects.add(new MobEffectInstance(MobEffects.JUMP, duration));
        return effects;
    }

    @Override
    @Deprecated
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(@Nonnull BlockState self, BlockState otherBlock, @Nonnull Direction direction) {
        //don't render sides between same blocks
        return otherBlock.is(this) || super.skipRendering(self, otherBlock, direction);
    }

}
