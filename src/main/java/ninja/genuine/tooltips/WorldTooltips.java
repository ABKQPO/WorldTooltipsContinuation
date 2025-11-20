package ninja.genuine.tooltips;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ninja.genuine.tooltips.client.Tooltip;
import ninja.genuine.tooltips.config.Config;
import ninja.genuine.tooltips.client.compat.GTNHLibModeHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = WorldTooltips.MODID, name = WorldTooltips.NAME, version = WorldTooltips.VERSION, canBeDeactivated = true, useMetadata = true, guiFactory = "ninja.genuine.tooltips.config.TooltipsGuiFactory")
public class WorldTooltips {
	
	/// Chore
	public static final String MODID = "world-tooltips";
	public static final String NAME = "World-Tooltips";
	public static final String VERSION = "1.2.3-87" + " kotmatross edition";
	public static final String GUIID = "worldtooltipsgui";
	public static final Logger logger = LogManager.getLogger("WorldTooltips");
    public boolean client = FMLLaunchHandler.side().isClient();
	public static Configuration config;
	
	/// Compat
	public static boolean isBetterTooltipsLoaded;
	
	@EventHandler
	public void pre(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		Config.syncConfig(config);
		
        isBetterTooltipsLoaded = Loader.isModLoaded("BetterTooltipBox");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
        if(client) {
            Tooltip.init();
			
			WorldTooltipsHandler handler = new WorldTooltipsHandler();
			handler.setupMc();
			
			MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
	}

	@EventHandler
	public void post(FMLPostInitializationEvent event) {
        if(client && Loader.isModLoaded("gtnhlib") && Config.CHANGE_GUI_TOOLTIP) {
			MinecraftForge.EVENT_BUS.register(new GTNHLibModeHandler());
        }
	}

}
