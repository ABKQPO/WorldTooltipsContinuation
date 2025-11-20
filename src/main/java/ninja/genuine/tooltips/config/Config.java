package ninja.genuine.tooltips.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import ninja.genuine.tooltips.WorldTooltips;
public class Config {
	
	public static boolean ENABLE_MOD = false;
	
	/// RENDER
	public static boolean CHANGE_GUI_TOOLTIP = true;
	public static boolean ENABLE_RARITY_OUTLINE_COLOR = true;
	public static int RARITY_OUTLINE_COLOR_ALPHA = 0xFF000000;
	public static int UPPER_BACKGROUND_COLOR = 0xF0100010;
	public static int LOWER_BACKGROUND_COLOR = 0xF0100010;
	public static int UPPER_OUTLINE_COLOR = 0x505000FF;
	public static int LOWER_OUTLINE_COLOR = (0x505000FF & 0xFEFEFE) >> 1 | 0x505000FF & 0xFF000000;

	/// "LOGIC"
	public static float MAX_DISTANCE; // maxDistance - 2F = 1 block
	public static boolean HIDE_MOD_NAME = false;
	public static int TICKS_DELAY = 10;
	public static boolean TICKS_DELAY_RESET = true;
	public static boolean ENABLE_MAX_DISTANCE_METHOD = false;
	
	private static final String DESC = "Choose a color in hexadecimal (ie: 0xFFAB12CD). Make sure all letters are uppercase and DO NOTE that this is an 8-digit HEX : first two characters after 0x are alpha (transparency)";
	public static final String catAppearance = "appearance";
	
	public static void syncConfig(Configuration config) {
		ENABLE_MOD = config.get(catAppearance, "ENABLE_MOD", true, "Enable mod's features").getBoolean();
		CHANGE_GUI_TOOLTIP = config.get(catAppearance, "CHANGE_GUI_TOOLTIP", true, "[REQUIRES GTNHLib] Allows all color settings to also apply to the tooltips in the GUIs").getBoolean();
		ENABLE_RARITY_OUTLINE_COLOR = config.get(catAppearance, "ENABLE_RARITY_OUTLINE_COLOR", true, "Whether to change tooltip's outline depending on the item's rarity").getBoolean();
		
		/// Whatever, yeah
		try {
			RARITY_OUTLINE_COLOR_ALPHA = (int)Long.parseLong(config.get(catAppearance, "RARITY_OUTLINE_COLOR_ALPHA", "0xFF000000", "Color mask applied to the outline if ENABLE_RARITY_OUTLINE_COLOR == true. Change the first two characters after 0x to change the outline's transparency", Property.Type.COLOR).getString().replaceFirst("^(0x)", ""), 16);
			UPPER_BACKGROUND_COLOR = (int)Long.parseLong(config.get(catAppearance, "UPPER_BACKGROUND_COLOR", "0xF0100010", DESC, Property.Type.COLOR).getString().replaceFirst("^(0x)", ""), 16);
			LOWER_BACKGROUND_COLOR = (int)Long.parseLong(config.get(catAppearance, "LOWER_BACKGROUND_COLOR", "0xF0100010", DESC, Property.Type.COLOR).getString().replaceFirst("^(0x)", ""), 16);
			UPPER_OUTLINE_COLOR = (int)Long.parseLong(config.get(catAppearance, "UPPER_OUTLINE_COLOR", "0x505000FF", DESC, Property.Type.COLOR).getString().replaceFirst("^(0x)", ""), 16);
			LOWER_OUTLINE_COLOR = (int)Long.parseLong(config.get(catAppearance, "LOWER_OUTLINE_COLOR", "0x5028007F", DESC, Property.Type.COLOR).getString().replaceFirst("^(0x)", ""), 16);
		} catch (NumberFormatException e) {
			WorldTooltips.logger.error("Error while trying to parse config, : " + e.getMessage());
			
			RARITY_OUTLINE_COLOR_ALPHA = 0xFF000000;
			UPPER_BACKGROUND_COLOR = 0xF0100010;
			LOWER_BACKGROUND_COLOR = 0xF0100010;
			UPPER_OUTLINE_COLOR = 0x505000FF;
			LOWER_OUTLINE_COLOR = 0x5028007F;
		}
		
		MAX_DISTANCE = config.getFloat("MAX_DISTANCE", catAppearance, 10.0F, 2.0F, 64.0F, "Set the maximum distance that tooltips should be displayed from. Requires \"Enable maxDistance Method\" ");
		HIDE_MOD_NAME = config.getBoolean("HIDE_MOD_NAME", catAppearance, false, "Hide mod names on tooltips.");
		TICKS_DELAY = config.getInt("TICKS_DELAY", catAppearance, 10, 0, 2147483647, "Delay (in ticks) before rendering the tooltip");
		TICKS_DELAY_RESET = config.getBoolean("TICKS_DELAY_RESET", catAppearance, true, "Whether to reset the tick counter if the player isn't looking at the item. If true, then wait for the delay every time");
		ENABLE_MAX_DISTANCE_METHOD = config.getBoolean("ENABLE_MAX_DISTANCE_METHOD", catAppearance, false, "If enabled, the render method will switch to the old getMouseOver() system, this will allow the Maximum Draw Distance config option to be used, but this means tooltips will be rendered through walls");
		
		if (config.hasChanged()) config.save();
	}
	
}
