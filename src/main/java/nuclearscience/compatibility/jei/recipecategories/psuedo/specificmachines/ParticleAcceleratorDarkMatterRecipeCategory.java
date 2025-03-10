package nuclearscience.compatibility.jei.recipecategories.psuedo.specificmachines;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.compatibility.jei.recipecategories.psuedo.PsuedoItem2ItemRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import nuclearscience.References;

public class ParticleAcceleratorDarkMatterRecipeCategory implements IRecipeCategory<PsuedoItem2ItemRecipe> {

	public static final int OUTPUT_SLOT = 0;

	private static int[] GUI_BACKGROUND = { 0, 0, 132, 132 };

	public static int ANIM_TIME = 50;
	private static int DESC_Y_HEIGHT = 122;

	private static String MOD_ID = References.ID;
	private static String RECIPE_GROUP = "particalaccelerator.darkmatter";
	private static String GUI_TEXTURE = "textures/gui/jei/particle_accelerator_dark_matter_gui.png";

	public static ItemStack INPUT_MACHINE = new ItemStack(nuclearscience.DeferredRegisters.blockParticleInjector);

	private LoadingCache<Integer, ArrayList<IDrawableAnimated>> cachedArrows;

	public static ResourceLocation UID = new ResourceLocation(MOD_ID, RECIPE_GROUP);

	private IDrawable BACKGROUND;
	private IDrawable ICON;

	public static final RecipeType<PsuedoItem2ItemRecipe> RECIPE_TYPE = RecipeType.create(References.ID, "dark_matter_recipe", PsuedoItem2ItemRecipe.class);

	public ParticleAcceleratorDarkMatterRecipeCategory(IGuiHelper guiHelper) {

		ICON = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, INPUT_MACHINE);
		BACKGROUND = guiHelper.createDrawable(new ResourceLocation(MOD_ID, GUI_TEXTURE), GUI_BACKGROUND[0], GUI_BACKGROUND[1], GUI_BACKGROUND[2], GUI_BACKGROUND[3]);

		ResourceLocation guiTexture = new ResourceLocation(MOD_ID, GUI_TEXTURE);

		cachedArrows = CacheBuilder.newBuilder().maximumSize(2).build(new CacheLoader<Integer, ArrayList<IDrawableAnimated>>() {
			@Override
			public ArrayList<IDrawableAnimated> load(Integer cookTime) {

				IDrawableAnimated majorArrowBottom = guiHelper.drawableBuilder(guiTexture, 179, 17, 37, 75).buildAnimated(cookTime, IDrawableAnimated.StartDirection.BOTTOM, false);

				IDrawableAnimated majorArrowTop = guiHelper.drawableBuilder(guiTexture, 132, 0, 37, 75).buildAnimated(cookTime, IDrawableAnimated.StartDirection.TOP, false);

				IDrawableAnimated[] arrows = { majorArrowBottom, majorArrowTop };
				return new ArrayList<>(Arrays.asList(arrows));
			}
		});

	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public RecipeType<PsuedoItem2ItemRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Class<? extends PsuedoItem2ItemRecipe> getRecipeClass() {
		return PsuedoItem2ItemRecipe.class;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("jei.gui." + RECIPE_GROUP);
	}

	@Override
	public IDrawable getBackground() {
		return BACKGROUND;
	}

	@Override
	public IDrawable getIcon() {
		return ICON;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, PsuedoItem2ItemRecipe recipe, IFocusGroup focuses) {
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(ParticleAcceleratorAntiMatterRecipeCategory.filterItems(recipe));
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(Arrays.asList(recipe.INPUTS.get(0).getItems()));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 58).addItemStack(recipe.OUTPUT);
	}

	@Override
	public void draw(PsuedoItem2ItemRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
		ArrayList<IDrawableAnimated> arrow = cachedArrows.getUnchecked(ANIM_TIME);
		arrow.get(0).draw(matrixStack, 70, 38);
		arrow.get(1).draw(matrixStack, 23, 21);

		int animTimeSeconds = ANIM_TIME / 20;
		TranslatableComponent timeString = new TranslatableComponent("jei.gui." + RECIPE_GROUP + ".info.power", animTimeSeconds);
		Minecraft minecraft = Minecraft.getInstance();
		Font fontRenderer = minecraft.font;
		float stringWidth = fontRenderer.width(timeString);
		fontRenderer.draw(matrixStack, timeString, getBackground().getWidth() - stringWidth, DESC_Y_HEIGHT, 0xFF808080);
	}

}
