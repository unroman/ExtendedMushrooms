package cech12.extendedmushrooms.blockentity;

import cech12.extendedmushrooms.init.ModRecipeTypes;
import cech12.extendedmushrooms.api.recipe.FairyRingMode;
import cech12.extendedmushrooms.api.recipe.FairyRingRecipe;
import cech12.extendedmushrooms.block.FairyRingBlock;
import cech12.extendedmushrooms.init.ModBlockEntityTypes;
import cech12.extendedmushrooms.init.ModParticles;
import cech12.extendedmushrooms.init.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FairyRingBlockEntity extends BlockEntity implements Container {

    public static final Vec3 CENTER_TRANSLATION_VECTOR = new Vec3(1, 0, 1);
    public static final int INVENTORY_SIZE = 16;

    private static final int EFFECT_EVENT = 0;

    private static final int CRAFTING_SOUND_INTERVAL = 60; //in ticks

    private boolean hasMaster;
    private boolean isMaster;
    private BlockPos masterPos;

    private NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private FairyRingMode mode = FairyRingMode.NORMAL;
    private int recipeTime;
    private int recipeTimeTotal;

    //recipe caching
    protected FairyRingRecipe currentRecipe;

    public FairyRingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.FAIRY_RING.get(), pos, state);
        this.hasMaster = false;
        this.isMaster = false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        //use onLoad only on server (setup of clients happens via nbt sync)
        if (this.getLevel() != null && !this.getLevel().isClientSide) {
            //if master value is already set, do nothing
            if (this.isMaster() || this.hasMaster()) {
                this.loadRecipe();
                return;
            }
            //initial loading of this tile entity.
            //search for a master, when not found, I am the master
            Level world = this.getLevel();
            BlockPos pos = this.getBlockPos();
            if (world != null) {
                for (Direction direction : FairyRingBlock.DIRECTIONS) {
                    BlockEntity tileEntity = world.getBlockEntity(pos.relative(direction));
                    if (tileEntity instanceof FairyRingBlockEntity) {
                        this.setMaster(((FairyRingBlockEntity) tileEntity).getMaster());
                        break;
                    }
                }
            }
            if (!this.hasMaster()) {
                this.setAsMaster();
            }
        }
    }

    /**
     * Should only be called by master!
     */
    public Vec3 getCenter() {
        BlockPos position = this.getBlockPos();
        return new Vec3(position.getX(), position.getY(), position.getZ()).add(CENTER_TRANSLATION_VECTOR);
    }

    public boolean hasMaster() {
        return hasMaster;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(FairyRingBlockEntity tileEntity) {
        this.isMaster = false;
        this.hasMaster = true;
        this.masterPos = tileEntity.getBlockPos();
        this.sendUpdates();
    }

    public void setAsMaster() {
        this.isMaster = true;
        this.hasMaster = false;
        this.masterPos = this.getBlockPos();
        this.sendUpdates();
    }

    public FairyRingBlockEntity getMaster() {
        if (this.isMaster()) {
            return this;
        }
        if (this.hasMaster() && this.getLevel() != null) {
            BlockEntity tileEntity = this.getLevel().getBlockEntity(this.masterPos);
            if (tileEntity instanceof FairyRingBlockEntity) {
                return ((FairyRingBlockEntity) tileEntity);
            }
        }
        return null;
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        this.masterPos = new BlockPos(compound.getInt("MasterX"), compound.getInt("MasterY"), compound.getInt("MasterZ"));
        this.hasMaster = compound.getBoolean("HasMaster");
        this.isMaster = compound.getBoolean("IsMaster");
        if (this.isMaster()) {
            this.items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(compound, this.items);
            this.mode = FairyRingMode.byName(compound.getString("Mode"));
            this.recipeTime = compound.getInt("RecipeTime");
            this.recipeTimeTotal = compound.getInt("RecipeTimeTotal");
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("MasterX", this.masterPos.getX());
        compound.putInt("MasterY", this.masterPos.getY());
        compound.putInt("MasterZ", this.masterPos.getZ());
        compound.putBoolean("HasMaster", hasMaster);
        compound.putBoolean("IsMaster", isMaster);
        if (this.isMaster()) {
            ContainerHelper.saveAllItems(compound, this.items);
            compound.putString("Mode", this.mode.getName());
            compound.putInt("RecipeTime", this.recipeTime);
            compound.putInt("RecipeTimeTotal", this.recipeTimeTotal);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (this.level != null && pkt.getTag() != null) {
            this.load(pkt.getTag());
        }
    }

    /**
     * Informs clients about state changes of this tile entity.
     * Should be called every time when a state value is updated. (in setters)
     */
    private void sendUpdates() {
        if (this.getLevel() != null) {
            BlockState state = this.getLevel().getBlockState(this.getBlockPos());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }

    /**
     * Collect Item Entities.
     */
    public void onEntityCollision(Entity entity) {
        if (!this.isMaster()) {
            FairyRingBlockEntity master = this.getMaster();
            if (master != null) {
                master.onEntityCollision(entity);
            }
        } else {
            if (entity instanceof ItemEntity) {
                this.onItemEntityCollision((ItemEntity) entity);
            } else if (entity instanceof Player playerEntity) {
                //Give entering player all stored items.
                boolean collectedSomething = false;
                for (int i = 0; i < this.getContainerSize(); i++) {
                    ItemStack stack = this.getItem(i);
                    if (playerEntity.getInventory().add(stack)) {
                        collectedSomething = true;
                        this.removeItem(i, stack.getCount());
                    }
                }
                if (collectedSomething) {
                    playerEntity.level().playLocalSound(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 1.4F + 2.0F, false);
                }
                boolean dirty = false;
                // reset mode
                if (this.mode != FairyRingMode.NORMAL) {
                    this.mode = FairyRingMode.NORMAL;
                    dirty = true;
                }
                // check and update recipe
                dirty |= this.updateRecipe();
                if (dirty) {
                    this.sendUpdates();
                }
            }
        }
    }

    private void onItemEntityCollision(ItemEntity itemEntity) {
        //Collect Item Entities.
        ItemStack remainingStack = this.addItemStack(itemEntity.getItem());
        if (remainingStack == ItemStack.EMPTY) {
            //when fully added, remove entity
            itemEntity.remove(Entity.RemovalReason.DISCARDED);
        } else {
            //when not or partly added, set new stack
            itemEntity.setItem(remainingStack);
            //itemEntity shouldn't stay inside of FairyRingTileEntity (performance issue)
            //so, push remaining stack to border.
            Vec3 centerToStack = itemEntity.position().subtract(this.getCenter());
            double scaleFactor = (1.8 - centerToStack.length()) * 0.08; //1.8 is sqrt(3) | 0.08 is speed
            Vec3 calculatedMotion = new Vec3(centerToStack.x, 0, centerToStack.z).normalize().scale(scaleFactor);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(calculatedMotion));
        }

        // check and update recipe
        if (this.updateRecipe()) {
            this.sendUpdates();
        }
    }

    /**
     * Loads recipe. Should only be called in onLoad method.
     * Hint: Sends no updates to client.
     */
    protected void loadRecipe() {
        if (this.isMaster()) {
            this.currentRecipe = this.getRecipe();
            if (this.currentRecipe == null) {
                this.recipeTime = 0;
                this.recipeTimeTotal = 0;
            }
        }
    }

    /**
     * reset cached recipe and recipe times.
     * Hint: Sends no updates to client.
     */
    protected void resetRecipe() {
        this.currentRecipe = null;
        this.recipeTime = 0;
        this.recipeTimeTotal = 0;
    }

    /**
     * Updates the current recipe.
     * Hint: Sends no updates to client.
     * @return true when recipe changed. false otherwise.
     */
    protected boolean updateRecipe() {
        if (this.isMaster()) {
            FairyRingRecipe oldRecipe = this.currentRecipe;
            FairyRingRecipe newRecipe = this.getRecipe();
            if (oldRecipe != newRecipe) {
                this.recipeTime = 0;
                if (newRecipe != null) {
                    this.recipeTimeTotal = newRecipe.getRecipeTime();
                } else {
                    this.recipeTimeTotal = 0;
                }
                this.currentRecipe = newRecipe;
                return true;
            }
        }
        return false;
    }

    /**
     * Get valid recipe related to items in inventory.
     * Cached recipe is used but not set in this method. For updating recipe cache use updateRecipe() method.
     * @return a valid FairyRingRecipe or null when no valid recipe exist.
     */
    protected FairyRingRecipe getRecipe() {
        if (!this.isMaster() || this.getLevel() == null || this.isEmpty()) {
            return null;
        }
        if (this.currentRecipe != null && this.currentRecipe.isValid(this.mode, this)) {
            return this.currentRecipe;
        } else {
            List<FairyRingRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FAIRY_RING.get());
            for (FairyRingRecipe recipe : recipes) {
                if (recipe.isValid(this.mode, this)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    /**
     * Set LIT property of FairyRingBlock.
     * @param light
     */
    protected void setLight(boolean light) {
        if (this.getLevel() != null) {
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(FairyRingBlock.LIT, light), 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FairyRingBlockEntity entity) {
        if (entity.isMaster() && level != null && !level.isClientSide) {
            FairyRingRecipe recipe = entity.getRecipe();
            if (recipe != null) {
                //light up
                if (!state.getValue(FairyRingBlock.LIT)) {
                    entity.setLight(true);
                }
                //play crafting sound every CRAFTING_SOUND_INTERVAL ticks (beginning with tick 1) until a new sound would overlap the finish sound
                if (entity.recipeTime % CRAFTING_SOUND_INTERVAL == 1 && entity.recipeTimeTotal - entity.recipeTime > CRAFTING_SOUND_INTERVAL / 2) {
                    Vec3 center = entity.getCenter();
                    float volume = level.getRandom().nextFloat() * 0.4F + 0.8F;
                    float pitch = level.getRandom().nextFloat() * 0.2F + 0.9F;
                    level.playSound(null, center.x, center.y, center.z, ModSounds.FAIRY_RING_CRAFTING.get(), SoundSource.AMBIENT, volume, pitch);
                }
                //increase recipe time
                if (entity.recipeTime < entity.recipeTimeTotal) {
                    entity.recipeTime++;
                }
                //detect finished recipe
                if (entity.recipeTime >= entity.recipeTimeTotal) {
                    //update fairy ring mode
                    if (entity.mode != entity.currentRecipe.getResultMode()) {
                        entity.mode = entity.currentRecipe.getResultMode();
                    }
                    //clear inventory and pop out result itemStack
                    entity.clearContent();
                    Vec3 center = entity.getCenter();
                    ItemStack resultStack = entity.currentRecipe.getResultItemStack();
                    if (resultStack != null && resultStack != ItemStack.EMPTY) {
                        ItemEntity itemEntity = new ItemEntity(level, center.x, center.y + 1.1, center.z, resultStack);
                        itemEntity.setDeltaMovement(new Vec3(0, 0.2, 0));
                        level.addFreshEntity(itemEntity);
                    }
                    //play sound
                    level.playSound(null, center.x, center.y, center.z, ModSounds.FAIRY_RING_CRAFTING_FINISH.get(), SoundSource.BLOCKS, 1.5F, 1.0F);
                    //reset and update recipe
                    entity.resetRecipe();
                    entity.updateRecipe();
                    entity.setLight(false);
                }
                entity.sendUpdates();
            } else {
                if (state.getValue(FairyRingBlock.LIT)) {
                    entity.setLight(false);
                }
            }
        }
    }

    /**
     * Called periodically client side by FairyRingBlocks near the player to show effects.
     */
    @OnlyIn(Dist.CLIENT)
    public void animateTick(Level worldIn, RandomSource rand) {
        if (this.isMaster()) {
            Vec3 center = this.getCenter();
            boolean runningRecipe = this.recipeTime < this.recipeTimeTotal;
            if (this.mode == FairyRingMode.NORMAL && !runningRecipe) {
                //normal mode without recipe process: some mycelium particles on ground
                for (int i = 0; i < 4; i++) {
                    worldIn.addParticle(ParticleTypes.MYCELIUM, -1.5 + center.x + rand.nextFloat() * 3, center.y + 0.1F, -1.5 + center.z + rand.nextFloat() * 3, 0, 0, 0);
                }
            } else {
                //normal mode with recipe process or fairy/witch mode: white mycelium particles on ground
                for (int i = 0; i < 4; i++) {
                    worldIn.addParticle(ModParticles.FAIRY_RING.get(), -1.5 + center.x + rand.nextFloat() * 3, center.y, -1.5 + center.z + rand.nextFloat() * 3, (rand.nextDouble() - 0.5) * 0.001, 0.0005, (rand.nextDouble() - 0.5) * 0.001);
                }
            }

            if (this.mode == FairyRingMode.NORMAL && runningRecipe) {
                //normal mode with recipe process: in center some sprinkling white particles
                for (int i = 0; i < 4; i++) {
                    worldIn.addParticle(ModParticles.FAIRY_RING.get(), center.x, center.y, center.z, (rand.nextDouble() - 0.5) * 0.05, 0.05D + rand.nextDouble() * 0.05, (rand.nextDouble() - 0.5) * 0.05);
                }
            }

            if (this.mode != FairyRingMode.NORMAL) {
                //fairy/witch mode outer ring with high white particles (with recipe higher)
                double factor = 1;
                if (runningRecipe) {
                    factor = 2;
                }
                int particleCount = 12;
                double anglePerParticle = 360.0D / particleCount;
                double variance = anglePerParticle / 2;
                for (int i = 0; i < particleCount; i++) {
                    double rad = Math.toRadians(anglePerParticle * i + (rand.nextDouble() - variance / 2) * variance);
                    double x = 1.5 * Math.cos(rad) + center.x;
                    double z = 1.5 * Math.sin(rad) + center.z;
                    worldIn.addParticle(ModParticles.FAIRY_RING.get(), x, center.y, z, 0, 0.05D * factor, 0);
                }
            }
        }
    }

    /**
     * Is called by client when server sends a block event via World#addBlockEvent.
     * @return Should return true, when event is correct and has an effect.
     */
    @Override
    public boolean triggerEvent(int id, int param) {
        //in tick() method
        //this.getWorld().addBlockEvent(this.getPos(), ExtendedMushroomsBlocks.FAIRY_RING, EFFECT_EVENT, 0);
        if (id == EFFECT_EVENT) {
            if (this.getLevel() != null && this.getLevel().isClientSide) {
                Vec3 center = this.getCenter();
                //TODO some nice effects!
                this.getLevel().addParticle(ParticleTypes.MYCELIUM, center.x, center.y, center.z, 0.0D, 0.0D, 0.0D);
            }
            return true;
        }
        return super.triggerEvent(id, param);
    }

    public FairyRingMode getMode() {
        return this.mode;
    }

    public int getRecipeTime() {
        return this.recipeTime;
    }

    public int getRecipeTimeTotal() {
        return this.recipeTimeTotal;
    }

    /**
     *
     * @param stack ItemStack which should be added.
     * @return The remaining ItemStack, which cannot be added or ItemStack.EMPTY when fully added
     */
    public ItemStack addItemStack(ItemStack stack) {
        FairyRingBlockEntity master = this.getMaster();
        if (stack != null && master != null && !stack.isEmpty()) {
            boolean dirty = false;
            //each slot has only a stack size of 1
            for (int i = 0; i < master.items.size(); i++) {
                if (master.items.get(i).isEmpty()) {
                    master.setItem(i, stack.split(1));
                    dirty = true;
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
            if (dirty) {
                this.sendUpdates();
            }
        }
        return stack;
    }

    @Override
    public int getContainerSize() {
        FairyRingBlockEntity master = this.getMaster();
        if (master != null) {
            return master.items.size();
        }
        return 0;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        FairyRingBlockEntity master = this.getMaster();
        if (master != null) {
            for (ItemStack itemstack : master.items) {
                if (!itemstack.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int slot) {
        FairyRingBlockEntity master = this.getMaster();
        if (master != null && slot >= 0 && slot < master.items.size()) {
            return master.items.get(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int count) {
        FairyRingBlockEntity master = this.getMaster();
        ItemStack stack = ItemStack.EMPTY;
        if (master != null && count > 0 && slot >= 0 && slot < master.items.size()) {
            stack = ContainerHelper.removeItem(master.items, slot, count);
            this.sendUpdates();
        }
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int slot) {
        FairyRingBlockEntity master = this.getMaster();
        if (master != null && slot < master.items.size()) {
            return ContainerHelper.takeItem(master.items, slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack itemStack) {
        FairyRingBlockEntity master = this.getMaster();
        if (master != null && slot >= 0 && slot < master.items.size()) {
            master.items.set(slot, itemStack);
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player playerEntity) {
        return false;
    }

    @Override
    public void clearContent() {
        FairyRingBlockEntity master = this.getMaster();
        if (master != null) {
            master.items.clear();
        }
    }
}
