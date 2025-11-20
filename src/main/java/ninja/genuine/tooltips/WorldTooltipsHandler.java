package ninja.genuine.tooltips;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import ninja.genuine.tooltips.client.MiscUtils;
import ninja.genuine.tooltips.client.RenderHelper;
import ninja.genuine.tooltips.client.Tooltip;
import ninja.genuine.tooltips.config.Config;
public class WorldTooltipsHandler {
	
	private static Minecraft mc;
	private Tooltip cache;
	private double distance;
	private int ticksOver;
	private long lastTickCount = 0;
	
	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
		if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		if(mc == null || mc.theWorld == null || mc.thePlayer == null || mc.objectMouseOver == null) {
			return;
		}
		
		MovingObjectPosition objectMouseOver = mc.objectMouseOver;
		EntityClientPlayerMP thePlayer = mc.thePlayer;
		WorldClient theWorld = mc.theWorld;
		
		if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			distance = thePlayer.getDistance(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
		} else if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
			distance = thePlayer.getDistanceToEntity(objectMouseOver.entityHit);
		}
		
		EntityItem entity = Config.ENABLE_MAX_DISTANCE_METHOD
				? MiscUtils.getMouseOver(mc, event.partialTicks)
				: MiscUtils.getEntityItem(mc, distance, thePlayer, event.partialTicks);
		
		long currentTickCount = theWorld.getTotalWorldTime();
		
		if (currentTickCount != lastTickCount) {
			lastTickCount = currentTickCount;
			if (entity != null) {
				ticksOver++;
			} else {
				ticksOver = Config.TICKS_DELAY_RESET ? 0 : Math.max(0, --ticksOver);
			}
		}
		
		if (ticksOver >= Config.TICKS_DELAY && entity != null) {
			if (cache == null || cache.getEntity() != entity)
				cache = new Tooltip(thePlayer, entity);
			
			cache.renderTooltip3D(mc, event.partialTicks);
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(WorldTooltips.MODID)) {
			Config.syncConfig(WorldTooltips.config);
			RenderHelper.updateFormattingToColorCode();
		}
	}
	
	public void setupMc() {
		mc = Minecraft.getMinecraft();
	}
	
}
