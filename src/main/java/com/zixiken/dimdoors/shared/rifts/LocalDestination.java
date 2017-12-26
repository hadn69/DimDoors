package com.zixiken.dimdoors.shared.rifts;

import ddutils.Location;
import ddutils.nbt.NBTUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

@Getter @AllArgsConstructor @Builder(toBuilder = true)
public class LocalDestination extends RiftDestination { // TODO: use BlockPos
    private BlockPos pos;

    public LocalDestination() {}

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        pos = new BlockPos(NBTUtils.readVec3i(nbt.getCompoundTag("pos")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setTag("pos", NBTUtils.writeVec3i(pos));
        return nbt;
    }

    @Override
    public boolean teleport(TileEntityRift rift, Entity entity) {
        ((TileEntityRift) rift.getWorld().getTileEntity(pos)).teleportTo(entity);
        return true;
    }

    @Override
    public Location getReferencedRift(Location rift) {
        return new Location(rift.getDim(), pos);
    }
}