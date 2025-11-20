package ninja.genuine.tooltips.client.compat;

import com.xiao_xing.BetterTooltipBox.Util.TooltipHelper;
public class BetterTooltipsCompat {
	
	public static void DrawTooltip(int x, int y, int w, int h) {
		TooltipHelper.z = 0; //Whatever
		TooltipHelper.DrawTooltip(x, y, w, h);
	}
	
}
