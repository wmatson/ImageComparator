package spider.utils.ImageComparator.FrameComponents.dialogs.structs;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/14/13
 * Time: 1:28 PM
 */
public class ScreenSelection {
		public boolean tracking = false;
		public Point point1 = new Point();
		public Point point2 = new Point();

		public Rectangle getRect()
		{
			int x = Math.min(point1.x, point2.x);
			int y = Math.min(point1.y, point2.y);
			int width = Math.abs(point2.x - point1.x);
			int height = Math.abs(point2.y - point1.y);
			return new Rectangle(x,y,width,height);
		}
}
