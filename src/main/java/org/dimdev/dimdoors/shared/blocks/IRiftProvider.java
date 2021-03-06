package org.dimdev.dimdoors.shared.blocks;

import org.dimdev.dimdoors.shared.rifts.TileEntityRift;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRiftProvider<T extends TileEntityRift> extends ITileEntityProvider {

    // This returns whether that block is the block containg the rift. If the rift entity does not exist, it must be created
    public T getRift(World world, BlockPos pos, IBlockState state); // TODO: split this to superinterface IHasRiftEntity?

    public void setupRift(T rift);

    public boolean hasTileEntity(IBlockState state);

    @Override
    public T createNewTileEntity(World world, int meta);

    // Call only once per structure (on item place)!
    public default void handleRiftSetup(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return;
        T rift = getRift(world, pos, state);

        // Configure the rift to its default functionality
        setupRift(rift);

        // Set the tile entity and register it
        //world.setTileEntity(pos, rift);
        rift.markDirty();
        rift.register();
    }
}
