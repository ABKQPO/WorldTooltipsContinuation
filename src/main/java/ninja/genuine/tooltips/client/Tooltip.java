package ninja.genuine.tooltips.client;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import net.minecraft.client.renderer.ActiveRenderInfo;
import ninja.genuine.tooltips.WorldTooltips;
import ninja.genuine.tooltips.client.compat.BetterTooltipsCompat;
import ninja.genuine.tooltips.config.Config;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class Tooltip {

	private static final Map<String, String> itemIdToModName = new HashMap<>();
	private static Method info;
	private static boolean useNei = false;

	public static void init() {
		Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
		
		for (Map.Entry<String, ModContainer> modEntry : modMap.entrySet()) {
			String lowercaseId = modEntry.getKey().toLowerCase(Locale.ENGLISH);
			String modName = modEntry.getValue().getName();
			itemIdToModName.put(lowercaseId, modName);
		}
		
		try {
			Class<?> nei = Class.forName("codechicken.nei.guihook.GuiContainerManager");
			info = nei.getDeclaredMethod("itemDisplayNameMultiline", ItemStack.class, GuiContainer.class, boolean.class);
			useNei = true;
		} catch (final Exception ignored) {}
	}

	int outlineUpper, outlineLower;
	int width, height;
	EntityItem entity;
	List<String> text = new ArrayList<>();

	public Tooltip(EntityPlayer player, EntityItem entity) {
		this.entity = entity;
		generateTooltip(player, entity.getEntityItem());
	}
	
	public void renderTooltip3D(Minecraft mc, double partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		outlineUpper = Config.ENABLE_RARITY_OUTLINE_COLOR ? 
						(RenderHelper.formattingToColorCode.get(getRarityColor())) :
						Config.UPPER_OUTLINE_COLOR;
		
		outlineLower = Config.ENABLE_RARITY_OUTLINE_COLOR ?
						((outlineUpper & 0xFEFEFE) >> 1 | outlineUpper & 0xFF000000) :
						Config.LOWER_OUTLINE_COLOR;
		
		final double interpX = RenderManager.renderPosX - (entity.posX - (entity.prevPosX - entity.posX) * partialTicks);
		final double interpY = RenderManager.renderPosY - (entity.posY - (entity.prevPosY - entity.posY) * partialTicks);
		final double interpZ = RenderManager.renderPosZ - (entity.posZ - (entity.prevPosZ - entity.posZ) * partialTicks);
		final double interpDistance = Math.sqrt(interpX * interpX + interpY * interpY + interpZ * interpZ);
		
		RenderHelper.start();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadMatrix(ActiveRenderInfo.projection);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadMatrix(ActiveRenderInfo.modelview);
		
		GL11.glTranslated(-interpX, -(interpY - 0.55), -interpZ);
		GL11.glRotatef(-RenderManager.instance.playerViewY + 180, 0, 1, 0);
		GL11.glRotatef(-RenderManager.instance.playerViewX, 1, 0, 0);
		
		double scale = interpDistance;
		scale /= sr.getScaleFactor() * 160;
		if (scale <= 0.01)
			scale = 0.01;
		GL11.glScaled(scale, -scale, scale);
		
		int x = -getWidth() / 2;
		int y = -getHeight();
		
        if(WorldTooltips.isBetterTooltipsLoaded) {
			BetterTooltipsCompat.DrawTooltip(x, y, getWidth(), getHeight());
        } else {
			RenderHelper.renderTooltipTile(x, y, getWidth(), getHeight(), Config.UPPER_BACKGROUND_COLOR, Config.LOWER_BACKGROUND_COLOR, outlineUpper, outlineLower);
		}
		
		RenderHelper.renderTooltipText(this, x, y, 0xFF);
		
		GL11.glScaled(1F / scale, 1F / -scale, 1F / scale);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1, 0, 0);
		GL11.glRotatef(RenderManager.instance.playerViewY - 180, 0, 1, 0);
		GL11.glTranslated(interpX, interpY - entity.height - 0.5, interpZ);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		RenderHelper.end();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public EntityItem getEntity() {
		return entity;
	}

	public int size() {
		return text.size();
	}

	public String getLine(int line) {
		return text.get(line);
	}

	public EnumChatFormatting getRarityColor() {
		return entity.getEntityItem().getRarity().rarityColor;
	}

	@SuppressWarnings("unchecked")
	private void generateTooltip(EntityPlayer player, ItemStack item) {
		
		if (useNei)
			try {
				text = (List<String>) info.invoke(null, entity.getEntityItem(), null, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
			} catch (final Exception ignored) {}
		
		if (Objects.isNull(text) || text.isEmpty())
			text = (List<String>) item.getTooltip(player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
		
		if (!modsAreLoaded() && !Config.HIDE_MOD_NAME)
			text.add(EnumChatFormatting.BLUE.toString() + EnumChatFormatting.ITALIC + getModName(item.getItem()) + EnumChatFormatting.RESET);
		
		if (item.stackSize > 1)
			text.set(0, item.stackSize + " x " + text.get(0));
		
		int maxwidth = 0;
		for (int line = 0; line < text.size(); line++) {
			final int swidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(getLine(line));
			if (swidth > maxwidth)
				maxwidth = swidth;
		}
		
		width = maxwidth;
		height = 8;
		if (size() > 1)
			height += 2 + (size() - 1) * 10;
	}

	private boolean modsAreLoaded() {
		return Loader.isModLoaded("waila") | Loader.isModLoaded("nei") | Loader.isModLoaded("hwyla");
	}

	private String getModName(Item item) {
		String fullName = Item.itemRegistry.getNameForObject(item);
		String modId = fullName.substring(0, fullName.indexOf(":"));
		String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
		String modName = itemIdToModName.get(lowercaseModId);
		if (modName == null) {
			modName = WordUtils.capitalize(modId);
			itemIdToModName.put(lowercaseModId, modName);
		}
		return modName;
	}
	
}
