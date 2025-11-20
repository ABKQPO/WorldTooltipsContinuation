package ninja.genuine.tooltips.client;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import ninja.genuine.tooltips.config.Config;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import static ninja.genuine.tooltips.WorldTooltips.MODID;

import java.util.HashMap;
import java.util.Map;

public class RenderHelper {
	
    private static final ResourceLocation shader = new ResourceLocation(MODID, "shaderfix.png");
	
	public static final Map<EnumChatFormatting, Integer> formattingToColorCode = new HashMap<>();
	
	static {
		formattingToColorCode.put(EnumChatFormatting.BLACK, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x000000);
		formattingToColorCode.put(EnumChatFormatting.DARK_BLUE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x0000AA);
		formattingToColorCode.put(EnumChatFormatting.DARK_GREEN, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x00AA00);
		formattingToColorCode.put(EnumChatFormatting.DARK_AQUA, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x00AAAA);
		formattingToColorCode.put(EnumChatFormatting.DARK_RED, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xAA0000);
		formattingToColorCode.put(EnumChatFormatting.DARK_PURPLE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xAA00AA);
		formattingToColorCode.put(EnumChatFormatting.GOLD, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFFAA00);
		formattingToColorCode.put(EnumChatFormatting.GRAY, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xAAAAAA);
		formattingToColorCode.put(EnumChatFormatting.DARK_GRAY, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x555555);
		formattingToColorCode.put(EnumChatFormatting.BLUE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x5555FF);
		formattingToColorCode.put(EnumChatFormatting.GREEN, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x55FF55);
		formattingToColorCode.put(EnumChatFormatting.AQUA, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x55FFFF);
		formattingToColorCode.put(EnumChatFormatting.RED, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFF5555);
		formattingToColorCode.put(EnumChatFormatting.LIGHT_PURPLE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFF55FF);
		formattingToColorCode.put(EnumChatFormatting.YELLOW, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFFFF55);
		formattingToColorCode.put(EnumChatFormatting.WHITE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFFFFFF);
	}
	
	public static void updateFormattingToColorCode() {
		formattingToColorCode.clear();
		formattingToColorCode.put(EnumChatFormatting.BLACK, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x000000);
		formattingToColorCode.put(EnumChatFormatting.DARK_BLUE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x0000AA);
		formattingToColorCode.put(EnumChatFormatting.DARK_GREEN, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x00AA00);
		formattingToColorCode.put(EnumChatFormatting.DARK_AQUA, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x00AAAA);
		formattingToColorCode.put(EnumChatFormatting.DARK_RED, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xAA0000);
		formattingToColorCode.put(EnumChatFormatting.DARK_PURPLE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xAA00AA);
		formattingToColorCode.put(EnumChatFormatting.GOLD, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFFAA00);
		formattingToColorCode.put(EnumChatFormatting.GRAY, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xAAAAAA);
		formattingToColorCode.put(EnumChatFormatting.DARK_GRAY, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x555555);
		formattingToColorCode.put(EnumChatFormatting.BLUE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x5555FF);
		formattingToColorCode.put(EnumChatFormatting.GREEN, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x55FF55);
		formattingToColorCode.put(EnumChatFormatting.AQUA, Config.RARITY_OUTLINE_COLOR_ALPHA | 0x55FFFF);
		formattingToColorCode.put(EnumChatFormatting.RED, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFF5555);
		formattingToColorCode.put(EnumChatFormatting.LIGHT_PURPLE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFF55FF);
		formattingToColorCode.put(EnumChatFormatting.YELLOW, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFFFF55);
		formattingToColorCode.put(EnumChatFormatting.WHITE, Config.RARITY_OUTLINE_COLOR_ALPHA | 0xFFFFFF);
	}
	
	public static void renderTooltipText(Tooltip tooltip, int drawx, int drawy, int alpha) {
		for (int i = 0; i < tooltip.size(); i++) {
			String s = tooltip.getLine(i);
			if (i == 0)
				s = tooltip.getRarityColor() + s;
			Minecraft.getMinecraft().fontRenderer.drawString(s, drawx, drawy, 0xFFFFFF | alpha, true);
			if (i == 0)
				drawy += 2;
			drawy += 10;
		}
	}
	
	/// Taken from GTNHLib, with some changes
	public static void renderTooltipTile(int x, int y, int width, int height, int backgroundStart, int backgroundEnd, int borderStart, int borderEnd) {
		
		// spotless:off
		
		// draw background
		RenderHelper.drawGradientRect(x - 3,         y - 4,          width + 6, 1,          backgroundStart, backgroundStart); // top
		RenderHelper.drawGradientRect(x - 3,         y + height + 3, width + 6, 1,          backgroundEnd,   backgroundEnd);   // bottom
		RenderHelper.drawGradientRect(x - 3,         y - 3,          width + 6, height + 6, backgroundStart, backgroundEnd);   // middle
		RenderHelper.drawGradientRect(x - 4,         y - 3,          1,         height + 6, backgroundStart, backgroundEnd);   // left
		RenderHelper.drawGradientRect(x + width + 3, y - 3,          1,         height + 6, backgroundStart, backgroundEnd);   // right
	
		// draw inner border
		RenderHelper.drawGradientRect(x - 3,         y - 2,          1,         height + 4, borderStart, borderEnd);   // left
		RenderHelper.drawGradientRect(x + width + 2, y - 2,          1,         height + 4, borderStart, borderEnd);   // right
		RenderHelper.drawGradientRect(x - 3,         y - 3,          width + 6, 1,          borderStart, borderStart); // top
		RenderHelper.drawGradientRect(x - 3,         y + height + 2, width + 6, 1,          borderEnd,   borderEnd);   // bottom
		
		// spotless:on
	}

	public static void start() {
		GL11.glPushMatrix();
		// GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void end() {
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public static void drawGradientRect(int x, int y, int w, int h, int color1, int color2) {
		w += x;
		h += y;
		final float alpha1 = (color1 >> 24 & 0xff) / 255F;
		final float red1 = (color1 >> 16 & 0xff) / 255F;
		final float green1 = (color1 >> 8 & 0xff) / 255F;
		final float blue1 = (color1 & 0xff) / 255F;
		final float alpha2 = (color2 >> 24 & 0xff) / 255F;
		final float red2 = (color2 >> 16 & 0xff) / 255F;
		final float green2 = (color2 >> 8 & 0xff) / 255F;
		final float blue2 = (color2 & 0xff) / 255F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
        Minecraft.getMinecraft().renderEngine.bindTexture(shader);
		tessellator.setColorRGBA_F(red1, green1, blue1, alpha1);
		tessellator.addVertexWithUV(w, y, 0,0,1);
		tessellator.addVertexWithUV(x, y, 0,0,1);
		tessellator.setColorRGBA_F(red2, green2, blue2, alpha2);
		tessellator.addVertexWithUV(x, h, 0,0,1);
		tessellator.addVertexWithUV(w, h, 0,0,1);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
