package spider.utils.ImageComparator.PixelRunners;

import spider.utils.ImageComparator.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 2/25/13
 * Time: 1:04 PM
 */
public interface PixelRunner {

	public void run(Pixel a, Pixel b, int x, int y);
}
