package ninja.genuine.tooltips.client.compat;

import com.gtnewhorizon.gtnhlib.client.event.RenderTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ninja.genuine.tooltips.client.RenderHelper;
import ninja.genuine.tooltips.config.Config;

public class GTNHLibModeHandler {
	
	@SubscribeEvent
	public void onRenderTooltip(RenderTooltipEvent event) {
		if(!Config.CHANGE_GUI_TOOLTIP)
			return;
		
		if(event.itemStack.getItem() != null) {
			event.backgroundStart = Config.UPPER_BACKGROUND_COLOR;
			event.backgroundEnd = Config.LOWER_BACKGROUND_COLOR;
			
			if(Config.ENABLE_RARITY_OUTLINE_COLOR) {
				event.borderStart = RenderHelper.formattingToColorCode.get(event.itemStack.getItem().getRarity(event.itemStack).rarityColor);
				event.calculateBorderEnd();
			} else {
				event.borderStart = Config.UPPER_OUTLINE_COLOR;
				event.borderEnd = Config.LOWER_OUTLINE_COLOR;
			}
		}
	}
	
}
