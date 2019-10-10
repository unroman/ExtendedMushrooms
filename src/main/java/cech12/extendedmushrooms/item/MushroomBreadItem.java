package cech12.extendedmushrooms.item;

import cech12.extendedmushrooms.ExtendedMushrooms;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class MushroomBreadItem extends Item {

    private static final ResourceLocation REGISTRY_NAME = new ResourceLocation(ExtendedMushrooms.MOD_ID, "mushroom_bread");

    private static final Food FOOD = (new Food.Builder()).hunger(5).saturation(0.3F).build();

    public MushroomBreadItem() {
        super((new Properties()).group(ItemGroup.FOOD).food(FOOD));
        ComposterBlock.CHANCES.put(this, 0.85F);
        this.setRegistryName(REGISTRY_NAME);
    }

}
