package com.zixiken.dimdoors.shared.blocks;

import com.zixiken.dimdoors.DimDoors;
import com.zixiken.dimdoors.shared.items.ModItems;
import com.zixiken.dimdoors.shared.tileentities.TileEntityFloatingRift;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockRift extends Block implements ITileEntityProvider {

    public static final String ID = "rift";

    private final ArrayList<Block> blocksImmuneToRift;	// List of Vanilla blocks immune to rifts
    private final ArrayList<Block> modBlocksImmuneToRift; // List of DD blocks immune to rifts

    public BlockRift() {
        super(Material.LEAVES); //Fire is replacable. We do not want this block to be replacable. We do want to walk through it though...
        setTickRandomly(true);
        setHardness(1.0F);
        setUnlocalizedName(ID);
        setRegistryName(new ResourceLocation(DimDoors.MODID, ID));

        modBlocksImmuneToRift = new ArrayList<>();
        modBlocksImmuneToRift.add(ModBlocks.FABRIC);
        modBlocksImmuneToRift.add(ModBlocks.DIMENSIONAL_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.WARP_DIMENSIONAL_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.DIMENSIONAL_TRAPDOOR);
        modBlocksImmuneToRift.add(ModBlocks.UNSTABLE_DIMENSIONAL_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.RIFT);
        modBlocksImmuneToRift.add(ModBlocks.TRANSIENT_DIMENSIONAL_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.GOLD_DIMENSIONAL_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.GOLD_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.PERSONAL_DIMENSIONAL_DOOR);
        modBlocksImmuneToRift.add(ModBlocks.QUARTZ_DOOR);

        blocksImmuneToRift = new ArrayList<>();
        blocksImmuneToRift.add(Blocks.LAPIS_BLOCK);
        blocksImmuneToRift.add(Blocks.IRON_BLOCK);
        blocksImmuneToRift.add(Blocks.GOLD_BLOCK);
        blocksImmuneToRift.add(Blocks.DIAMOND_BLOCK);
        blocksImmuneToRift.add(Blocks.EMERALD_BLOCK);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    /**
     * Returns whether this block is collideable based on the arguments passed
     * in Args: blockMetaData, unknownFlag
     */
    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return hitIfLiquid;
    }

    /**
     * Returns true if the given side of this block type should be
     * rendered (if it's solid or not), if the adjacent block is at the given
     * coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE; //Tile Entity Special Renderer
    }

    public void dropWorldThread(World world, BlockPos pos, Random random) {
        Block block = world.getBlockState(pos).getBlock();

        if (!world.getBlockState(pos).equals(Blocks.AIR) && !(block instanceof BlockLiquid || block instanceof IFluidBlock)) {
            ItemStack thread = new ItemStack(ModItems.WORLD_THREAD, 1);
            world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), thread));
        }
    }

    /**
     * Lets pistons push through rifts, destroying them
     */
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    /**
     * regulates the renderDoorRift effect, especially when multiple rifts start to link
     * up. Has 3 main parts- Grows toward and away from nearest rift, bends
     * toward it, and a randomization function
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        //ArrayList<BlockPos> targets = findReachableBlocks(worldIn, pos, 2, false);
        //TODO: implement the parts specified in the method comment?
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();

        TileEntityFloatingRift tile = (TileEntityFloatingRift) worldIn.getTileEntity(pos);
        //renders an extra little blob on top of the actual rift location so its easier to find.
        // Eventually will only renderDoorRift if the player has the goggles.
        /*FMLClientHandler.instance().getClient().effectRenderer.addEffect(new GoggleRiftFX(
                worldIn,
                x + .5, y + .5, z + .5,
                rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D));
         */
        //if (tile.shouldClose) {//renders an opposite color effect if it is being closed by the rift remover
        //    FMLClientHandler.instance().getClient().effectRenderer.addEffect(new ClosingRiftFX(
        //            worldIn,
        //            x + .5, y + .5, z + .5,
        //            rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D));
        //} // TODO
    }

    public boolean tryPlacingRift(World world, BlockPos pos) {
        return world != null && !isBlockImmune(world, pos)
                && world.setBlockState(pos, getDefaultState()); //@todo This returns false, because this block does not have blockstates configured correctly. !isBlockImmune doesn't seem to be true either though...
    }

    public boolean isBlockImmune(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        // SenseiKiwi: I've switched to using the block's blast resistance instead of its
        // hardness since most defensive blocks are meant to defend against explosions and
        // may have low hardness to make them easier to build with. However, block.getExplosionResistance()
        // is designed to receive an entity, the source of the blast. We have no entity so
        // I've set this to access blockResistance directly. Might need changing later.
        return block != null /* && block >= MIN_IMMUNE_RESISTANCE */ || modBlocksImmuneToRift.contains(block) || blocksImmuneToRift.contains(block);
    }

    public boolean isModBlockImmune(World world, BlockPos pos) {
        // Check whether the block at the specified location is one of the
        // rift-resistant blocks from DD.
        Block block = world.getBlockState(pos).getBlock();
        return block != null && modBlocksImmuneToRift.contains(block);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return null;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFloatingRift();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityFloatingRift riftTile = (TileEntityFloatingRift) worldIn.getTileEntity(pos);

    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    public TileEntityFloatingRift getRiftTile(World world, BlockPos pos, IBlockState state) {
        return (TileEntityFloatingRift) world.getTileEntity(pos);
    }
}
