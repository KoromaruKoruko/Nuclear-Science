package nuclearscience.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import nuclearscience.client.ClientRegister;
import nuclearscience.common.tile.TileFuelReprocessor;

public class RenderFuelReprocessor implements BlockEntityRenderer<TileFuelReprocessor> {
	public RenderFuelReprocessor(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(TileFuelReprocessor tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.5, 0.5);
		Direction dir = tile.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		switch (dir) {
		case EAST:
			matrixStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), 180, true));
			break;
		case NORTH:
			matrixStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), -90, true));
			break;
		case SOUTH:
			matrixStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), 90, true));
			break;
		default:
			break;
		}
		if (tile.getProcessor(0).operatingTicks > 0) {
			BakedModel on = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_FUELREPROCESSOR_ON);
			RenderingUtils.renderModel(on, tile, RenderType.solid(), matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		} else {
			BakedModel off = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_FUELREPROCESSOR);
			RenderingUtils.renderModel(off, tile, RenderType.solid(), matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		}
		matrixStack.popPose();
	}

}
