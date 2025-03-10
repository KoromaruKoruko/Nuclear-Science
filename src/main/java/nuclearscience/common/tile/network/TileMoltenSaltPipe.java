package nuclearscience.common.tile.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import nuclearscience.DeferredRegisters;
import nuclearscience.common.block.connect.BlockMoltenSaltPipe;
import nuclearscience.common.block.subtype.SubtypeMoltenSaltPipe;
import nuclearscience.common.tile.generic.GenericTileMoltenSaltPipe;

public class TileMoltenSaltPipe extends GenericTileMoltenSaltPipe {
	public double transmit = 0;

	public TileMoltenSaltPipe(BlockPos pos, BlockState state) {
		super(DeferredRegisters.TILE_MOLTENSALTPIPE.get(), pos, state);
	}

	public SubtypeMoltenSaltPipe pipe = null;

	@Override
	public SubtypeMoltenSaltPipe getPipeType() {
		if (pipe == null) {
			pipe = ((BlockMoltenSaltPipe) getBlockState().getBlock()).pipe;
		}
		return pipe;
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		compound.putInt("ord", getPipeType().ordinal());
		super.saveAdditional(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		pipe = SubtypeMoltenSaltPipe.values()[compound.getInt("ord")];
	}

	@Override
	protected void writeCustomPacket(CompoundTag nbt) {
		nbt.putDouble("transmit", transmit);
	}

	@Override
	protected void readCustomPacket(CompoundTag nbt) {
		transmit = nbt.getDouble("transmit");
	}
}
