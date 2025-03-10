package nuclearscience.client.screen;

import java.text.DecimalFormat;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.prefab.screen.GenericScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuclearscience.common.inventory.container.ContainerMSRReactorCore;
import nuclearscience.common.tile.TileFreezePlug;
import nuclearscience.common.tile.TileMSRReactorCore;

@OnlyIn(Dist.CLIENT)
public class ScreenMSRReactorCore extends GenericScreen<ContainerMSRReactorCore> {

	public ScreenMSRReactorCore(ContainerMSRReactorCore container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		super.renderLabels(matrixStack, mouseX, mouseY);
		TileMSRReactorCore core = menu.getHostFromIntArray();
		if (core != null) {
			font.draw(matrixStack, new TranslatableComponent("gui.reactorcore.temperature", (int) core.temperature + " C"), titleLabelX, (float) titleLabelY + 14 * 1, 4210752);
			if (core.temperature > TileMSRReactorCore.MELTDOWN_TEMPERATURE && System.currentTimeMillis() % 1000 < 500) {
				font.draw(matrixStack, new TranslatableComponent("gui.reactorcore.warning"), titleLabelX, (float) titleLabelY + 55, 16711680);
			}
			font.draw(matrixStack, new TranslatableComponent("gui.msrreactorcore.fuel", new DecimalFormat("#.##").format(core.currentFuel)), titleLabelX, (float) titleLabelY + 14 * 2, 4210752);
			if (!(core.plugCache.getSafe() instanceof TileFreezePlug)) {
				font.draw(matrixStack, new TranslatableComponent("gui.msrreactorcore.nofreezeplug"), titleLabelX, (float) titleLabelY + 14 * 3, 0);
			}
		}
	}
}