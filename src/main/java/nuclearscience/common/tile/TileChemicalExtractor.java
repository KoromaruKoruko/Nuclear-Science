package nuclearscience.common.tile;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
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
import net.minecraft.world.level.block.state.BlockState;
import nuclearscience.DeferredRegisters;
import nuclearscience.common.inventory.container.ContainerChemicalExtractor;
import nuclearscience.common.recipe.NuclearScienceRecipeInit;
import nuclearscience.common.settings.Constants;

public class TileChemicalExtractor extends GenericTile {

	public static final int DEFAULT_RAD_STRENGTH = 300;
	public static final int RAD_RADIUS = 2;
	public static final int MAX_TANK_CAPACITY = 5000;

	public TileChemicalExtractor(BlockPos pos, BlockState state) {
		super(DeferredRegisters.TILE_CHEMICALEXTRACTOR.get(), pos, state);
		addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentElectrodynamic(this).universalInput().voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2).maxJoules(Constants.CHEMICALEXTRACTOR_USAGE_PER_TICK * 10));
		addComponent(new ComponentFluidHandlerMulti(this).setAddFluidsValues(NuclearScienceRecipeInit.CHEMICAL_EXTRACTOR_TYPE.get(), MAX_TANK_CAPACITY, true, false).universalInput());
		addComponent(new ComponentInventory(this).size(6).faceSlots(Direction.UP, 0).faceSlots(Direction.DOWN, 1).slotFaces(2, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST).inputs(1).outputs(1).bucketInputs(1).upgrades(3).processors(1).processorInputs(1).validUpgrades(ContainerChemicalExtractor.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentProcessor(this).setProcessorNumber(0).usage(Constants.CHEMICALEXTRACTOR_USAGE_PER_TICK).requiredTicks(Constants.CHEMICALEXTRACTOR_REQUIRED_TICKS).canProcess(component -> component.consumeBucket().canProcessFluidItem2ItemRecipe(component, NuclearScienceRecipeInit.CHEMICAL_EXTRACTOR_TYPE.get())).process(component -> component.processFluidItem2ItemRecipe(component)));
		addComponent(new ComponentContainerProvider("container.chemicalextractor").createMenu((id, player) -> new ContainerChemicalExtractor(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	protected void tickServer(ComponentTickable tick) {
		InventoryUtils.handleExperienceUpgrade(this);
	}

	protected void tickClient(ComponentTickable tickable) {
		if (this.<ComponentProcessor>getComponent(ComponentType.Processor).operatingTicks > 0 && level.random.nextDouble() < 0.15) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.8 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}
}
