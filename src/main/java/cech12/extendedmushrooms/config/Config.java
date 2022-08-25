package cech12.extendedmushrooms.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON;

    public static final ForgeConfigSpec.BooleanValue MUSHROOM_CAPS_WITH_SHEARS_ENABLED;
    public static final ForgeConfigSpec.BooleanValue MUSHROOM_STEMS_WITHOUT_SILK_TOUCH_ENABLED;
    public static final ForgeConfigSpec.BooleanValue SHEEP_EAT_MUSHROOM_FROM_GROUND_ENABLED;
    public static final ForgeConfigSpec.BooleanValue SHEEP_ABSORB_MUSHROOM_TYPE_ENABLED;

    public static final ForgeConfigSpec.BooleanValue MUSHROOM_CAP_BUTTON_PLAY_SOUND;
    public static final ForgeConfigSpec.BooleanValue MUSHROOM_CAP_PRESSURE_PLATE_PLAY_SOUND;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Options that affect mod behavior.").push("behavior");

        MUSHROOM_CAPS_WITH_SHEARS_ENABLED = builder
                .comment("Whether or not mushroom caps can be harvested with shears.")
                .define("mushroomCapsWithShearsEnabled", true);
        MUSHROOM_STEMS_WITHOUT_SILK_TOUCH_ENABLED = builder
                .comment("Whether or not mushroom stems can be harvested without silk touch enchantment.")
                .define("mushroomStemsWithoutSilkTouchEnabled", true);

        builder.pop();

        builder.comment("Options that affect blocks and entities.").push("balancing");

        SHEEP_EAT_MUSHROOM_FROM_GROUND_ENABLED = builder
                .comment("Whether or not sheeps and mushroom sheeps can eat mushrooms from ground.")
                .define("sheepEatMushroomFromGroundEnabled", true);
        SHEEP_ABSORB_MUSHROOM_TYPE_ENABLED = builder
                .comment("Whether or not sheeps and mushroom sheeps change their fleece to the mushroom type when eating a mushroom from ground or while feeding.")
                .define("sheepAbsorbMushroomTypeEnabled", true);

        MUSHROOM_CAP_BUTTON_PLAY_SOUND = builder
                .comment("Whether or not mushroom cap buttons should play a sound when activated.")
                .define("mushroomButtonPressurePlatePlaySound", false);
        MUSHROOM_CAP_PRESSURE_PLATE_PLAY_SOUND = builder
                .comment("Whether or not mushroom cap pressure plates should play a sound when stepped on or off.")
                .define("mushroomCapPressurePlatePlaySound", false);

        builder.pop();

        COMMON = builder.build();
    }

}
