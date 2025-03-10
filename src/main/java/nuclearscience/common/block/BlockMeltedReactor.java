package nuclearscience.common.block;

import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import nuclearscience.common.tile.TileMeltedReactor;

public class BlockMeltedReactor extends GenericEntityBlockWaterloggable {

	public BlockMeltedReactor() {
		super(Properties.of(Material.METAL).strength(250.0f, 999.0f).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileMeltedReactor(pos, state);
	}
}
