package ninja.genuine.tooltips.config;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import ninja.genuine.tooltips.WorldTooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TooltipsConfigGUI extends GuiConfig {
	
	private static final IConfigElement<?> client = new ConfigElement<>(WorldTooltips.config.getCategory(Config.catAppearance));
	
	public TooltipsConfigGUI(GuiScreen parent) {
		super(
				parent,
				ImmutableList.of(client),
				WorldTooltips.MODID,
				false,
				false,
				GuiConfig.getAbridgedConfigPath(WorldTooltips.config.toString())
		);
	}
	
}
