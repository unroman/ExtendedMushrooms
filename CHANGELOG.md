# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.16.5-2.0.0.0] - ????-??-??
### Added
- Fairy Ring crafting
- Mushroom Trapdoors are now climbable for ladders of other mods 

### Changed
- update to Forge 1.16.5-36.2.26 (fix Log4J security issue)
- update to official mappings
- changed versioning to fit Forge [Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/)

## [1.6.1_1.16] - 2021-06-10
### Fixed
- Bugfix: Huge mushrooms didn't spawn in dark forests. #49 (thanks to jahirtrap fopr the report)

## [1.6.0_1.16] - 2021-06-09
### Added
- Signs for different mushroom wood types added #2

### Changed
- Needs Minecraft Forge 1.16.5-36.1.1 or later.
- Entity Attributes of Baashrooms are registered differently now

### Fixed
- Bugfix: Variant mushroom ladders were not climbable #48 (thanks to Aphetoros for the report)

## [1.5.3_1.16] - 2021-06-05
### Changed
- readd enable/disable config options (thanks to WenXin2 for your feedback)
- change the big & mega mushroom spawn features that they can be disabled individually

### Removed
- removed unusable weight and spawn factor config options (they are not available at feature registration)

## [1.5.2_1.16] - 2021-06-04
### Changed
- add correct registering of configured features #44 (thanks to TelepathicGrunt for the report)

### Fixed
- removed enable/disable config options for features (small/big/mega mushroom spawning, infested flower/grass) If someone needed these options, let me know ;D

## [1.5.1_1.16] - 2020-11-09
### Added
- ladders support Quarks enhanced ladders feature

## [1.5.0] - 2020-10-28
### Changed
- Port to 1.16
- reduce chance to spawn special mushroom sheeps to 5%
- Update Korean language file (thanks to mindy15963)
- still compatible with Botany Pots, I Like Wood, Quark, Vanilla Things, Wool Buttons, Wool Pressure Plates
- Botany Pots - Glowshrooms emit light inside of a pot
- Corail Woodcutter mod no longer exists -> port to other Woodcutter mod
- mushrooms are added via the vanilla biome categories. If the mods support that, then all is fine.
- changed some config values for mushroom generation

### Fixed
- fix loot table of stripped mushroom stems (now they are influenced by config)

### Removed
- removed mod support for: Bonsai Trees 2, Extended Slabs, Mubble, Traverse, Voyage
- removed specific mod support for: Atmospheric, Autumnity, BiomesOPlenty, OhTheBiomesYoullGo
- removed "extendedmushrooms:mushroom_growing_blocks" tag ("minecraft:mushroom_grow_block" tag is used now)
- removed unused "mushroom_valid_blocks" tag
- Mixin was included to Forge itself, so MixinBootstrap dependency was removed

### [1.4.1] - 2020-05-08
### Changed
- New name for Mushroom Sheep - Baashroom (thanks to dabomb6405)
- New texture for Baashrooms (thanks to dabomb6405)

### Fixed
- Bugfix: Server could not start

## [1.4.0] - 2020-05-07
### Added
- Variant Bookshelves (ILikeWood, Quark)
- Variant (Trapped) Chests (ILikeWood, Quark) > crafting recipes are a bit different to avoid recipe overlapping
- Variant Ladders (ILikeWood, Quark, VanillaThings)
- Vertical Slabs (ExtendedSlabs, Mubble, Quark, VanillaThings)
- Vertical Planks (ILikeWood, Quark)
- These new blocks are enabled when one of the listed mod is installed or can be enabled via config file
- Mushrooms are spawning in biomes of other mods (Atmospheric, Autumnity, BiomesOPlenty, Oh The Biomes You'll Go, Traverse, Voyage)
- Mod support for Botany Pots
- Bonsai Trees 2: Glowstone Crumbs are dropped more rarely
- Glowshroom of Quark can be used to craft a tipped arrow (nightvision)

### Changed
- Glowstone Crumbs are dropped more rarely (chance 0.5) (with Fortune up to 0.9)
- Texture changes of Glowshroom and Poisonous Mushrooms blocks (thanks to dabomb6405)
- Most of the assets and data files are now generated
- many tags have been changed

### Fixed
- Mushroom Caps had no effective tool (now axe)
- Recipes with sticks are now using related forge tag for better compatibility
- Poisonous Mushroom Sheep droped nothing

## [1.3.0] - 2020-04-17
### Added
- Mushroom Boats for all three mushroom wood types
- Option for dis-/enabling harvesting mushroom caps with shears (default enabled)
- Option for dis-/enabling harvesting mushroom stems without silk touch (default enabled)
- Bonsai Trees 2 (all mushrooms can be planted inside a bonsai pot)
- Shears of other mods are also working now (forge:shears tag)
- Mushroom Bread can also be used by other (food) mods now (forge:bread tag)

### Changed
- needs Forge 31.0.18 or above
- all Mushroom caps drop their loot like vanilla mushroom caps
- 1:9 chance for 1 mushroom, 1:9 chance for 2 mushrooms, else no mushroom
- fortune does not working any longer

### Fixed
- Mushroom Sheep fed with not supported mushroom turned brown
- Mushroom Sheep AI added tasks every chunk entering not on entering world
- Glowshroom Carpet had brown color on llamas

## [1.2.0] - 2020-04-01
### Added
- Poisonous Mushroom added
- added a new wood type (a green variant)
- poisonous mushroom and its cap generate poison effect clouds at contact
- added related configs
- poisonous mushroom is useful: brewing to poison potion / craftable to tipped arrow
- new tags for poisonous and edible mushrooms (food items only craftable from edible mushrooms)
- added flammable and compostable values for poisonous mushroom
- Infested Flower added
- generated only in mushroom biomes (like infested grass)
- can be crafted to brown dye
- its suspicious stew has slowness effect (9 seconds)
- added tipped arrow recipe for glowshrooms
- added flammable and compostable values for glowshroom
- Sheeps can now eat placed mushrooms from the ground and convert to mushroom sheeps (2 more configs)
- Sheeps get poison effect when eating a poisonous mushroom
- Pressure Plates - negate some fall damage
- Korean language support (thanks to mindy15963)
- Support for Corail Woodcutter mod (added recipes for all wood types, oak sawmill is craftable with all mushroom wood types)
- Mushroom Sheeps are more compatible with other mods (Dye it yourself)

### Changed
- Glowshroom Cap block drops night vision clouds in dark areas or at night (only blocks that are placed at growing)
- Glowshroom wood texture changed (it is a bit brighter now)
- natural generation of Glowshrooms is a bit lower than before (config change: from 0.25 to 0.15)
- 3% chance of spawning Mushroom Sheeps with glowshroom type
- 3% chance of spawning Mushroom Sheeps with poisonous mushroom type
- improved conversion of Mushroom Sheeps and Mooshrooms: a sound is played (no explosion any longer), custom name stays
- Cap Buttons and Pressure Plates act more like wool variants in mods "Wool Buttons" & "Wool Pressure Plates"
- Cap Buttons and Pressure Plates have no sound (configurable)

### Fixed
- Sheared Mushroom Sheeps droping nothing
- Mushroom Sheeps fed with the last mushroom in hand do not convert its type
- bounding box of Infested Grass is not correct
- rabbit stew is not craftable with edible mushrooms
- orientation of mushroom stems do not stay after stripping
- orientation problems with generated mushroom cap blocks

## [1.1.1] - 2020-03-12
### Added
- added Count Factor option for Glowshroom generation

### Fixed
- Bugfix: Too many Mushrooms!
- Bugfix: Config value of Big and Mega Glowshroom generation chance not used
- changed them to weight options and add weight options for mega red and mega brown mushroom

## [1.1.0] - 2020-03-12
### Added
- Mushroom Sheep added! (3 variants: Red Mushroom, Brown Mushroom, Glowshroom)
- Mushroom Sheeps can be fed with mushrooms (instead of wheat). This changes its "color" when it is different.
- Mushroom Sheeps eat Mycelium instead of Grass
- color of Mushroom Sheeps cannot be changes by using dye items (use mushrooms!)
- Glowshroom added!
- own Glowshroom Stem with all wood block variants
- own Glowshroom Cap with carpet, pressure plate, button (compatible with Wool Buttons & Wool Pressure Plates mod)
- all Glowshroom blocks have a light value of 8
- rework of mushroom wood textures
- own Red and Brown Mushroom Carpets
- Red and Brown Mushroom Pressure Plates (compatible with Wool Buttons mod)
- Red and Brown Mushroom Buttons (compatible with Wool Pressure Plates mod)
- Infested Grass (only generated in mushroom biomes)
- Mushroom Sheep Spawn Egg
- Glowstone Crumbs (can be optained by breaking Glowshroom Cap blocks, craftable to Glowstone Dust)
- Mega Mushrooms can be grown (2x2 same mushrooms generate a mega mushroom)
- Config file added with useful config options
- Mushroom Sheeps are generated in mushroom biomes
- Mega Mushrooms (Red, Brown, Glowshroom) are generated in mushroom biomes
- Glowshrooms are generated everywhere where normal mushrooms are generated but with a lower chance

## [1.0.0] - 2020-03-04
### Added
- Huge Mushrooms can grow at random tick on Mycelium and Podzol. (Dirt and Grass at lightlevel below 13)
- Mushroom Stem Block, Brown and Red Mushroom Blocks have changes drop behaviour
- Mushroom Spores (turn Dirt into Mycelium, turn Cows into Mooshrooms)
- Grilled Mushroom (food)
- Mushroom Bread (food)
- Mushroom Button
- Mushroom Door
- Mushroom Fence
- Mushroom Fence Gate
- Mushroom Pressure Plate
- Mushroom Slab
- Mushroom Stairs
- Mushroom Trapdoor
- Stripped Mushroom Stem
- To use all features, it is recommended to install MixinBootstrap.