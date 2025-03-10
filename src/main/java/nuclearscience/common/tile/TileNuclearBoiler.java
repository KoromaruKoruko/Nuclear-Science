package nuclearscience.common.tile;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.InventoryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nuclearscience.DeferredRegisters;
import nuclearscience.SoundRegister;
import nuclearscience.common.inventory.container.ContainerNuclearBoiler;
import nuclearscience.common.recipe.NuclearScienceRecipeInit;
import nuclearscience.common.settings.Constants;

public class TileNuclearBoiler extends GenericTile {

	public static final int MAX_TANK_CAPACITY = 5000;

	public TileNuclearBoiler(BlockPos pos, BlockState state) {
		super(DeferredRegisters.TILE_CHEMICALBOILER.get(), pos, state);
		addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentElectrodynamic(this).input(Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2).maxJoules(Constants.CHEMICALBOILER_USAGE_PER_TICK * 10));
		addComponent(new ComponentFluidHandlerMulti(this).setAddFluidsValues(NuclearScienceRecipeInit.NUCLEAR_BOILER_TYPE.get(), MAX_TANK_CAPACITY, true, true).relativeInput(Direction.EAST).relativeOutput(Direction.WEST));
		addComponent(new ComponentInventory(this).size(6).relativeSlotFaces(0, Direction.EAST, Direction.UP).relativeSlotFaces(1, Direction.DOWN).inputs(1).bucketInputs(1).bucketOutputs(1).upgrades(3).processors(1).processorInputs(1).validUpgrades(ContainerNuclearBoiler.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentProcessor(this).setProcessorNumber(0).canProcess(component -> component.outputToPipe().consumeBucket().dispenseBucket().canProcessFluidItem2FluidRecipe(component, NuclearScienceRecipeInit.NUCLEAR_BOILER_TYPE.get())).process(component -> component.processFluidItem2FluidRecipe(component)).usage(Constants.CHEMICALBOILER_USAGE_PER_TICK).requiredTicks(Constants.CHEMICALBOILER_REQUIRED_TICKS));
		addComponent(new ComponentContainerProvider("container.nuclearboiler").createMenu((id, player) -> new ContainerNuclearBoiler(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(1);
	}

	protected void tickServer(ComponentTickable tickable) {
		Level world = getLevel();
		ComponentDirection boilerComponentDir = getComponent(ComponentType.Direction);
		Direction centrifugeDir = boilerComponentDir.getDirection().getCounterClockWise();
		BlockEntity tile = world.getBlockEntity(getBlockPos().relative(centrifugeDir));
		if (tile != null && tile instanceof TileGasCentrifuge centrifuge) {
			ComponentFluidHandlerMulti centrifugeHandler = centrifuge.getComponent(ComponentType.FluidHandler);
			if (centrifugeHandler != null) {
				ComponentDirection centrifugeComponentDir = centrifuge.getComponent(ComponentType.Direction);
				if (centrifugeComponentDir.getDirection() == centrifugeDir) {
					ComponentFluidHandlerMulti boilerHandler = getComponent(ComponentType.FluidHandler);
					FluidTank boilerTank = boilerHandler.getOutputTanks()[0];
					FluidTank centrifugeTank = centrifugeHandler.getInputTanks()[0];
					int accepted = centrifugeTank.fill(boilerTank.getFluid(), FluidAction.SIMULATE);
					centrifugeTank.fill(new FluidStack(boilerTank.getFluid().getFluid(), accepted), FluidAction.EXECUTE);
					boilerTank.drain(accepted, FluidAction.EXECUTE);
				}
			}
		}
		InventoryUtils.handleExperienceUpgrade(this);
	}

	protected void tickClient(ComponentTickable tickable) {
		boolean running = this.<ComponentProcessor>getComponent(ComponentType.Processor).operatingTicks > 0;
		if (running && level.random.nextDouble() < 0.15) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.4 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
		if (running && tickable.getTicks() % 100 == 0) {
			SoundAPI.playSound(SoundRegister.SOUND_NUCLEARBOILER.get(), SoundSource.BLOCKS, 1, 1, worldPosition);
		}
	}

}
