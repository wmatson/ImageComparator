package spider.utils.ImageComparator.PixelRunners.DiffRendering;

import spider.utils.ImageComparator.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/8/13
 * Time: 12:44 PM
 */
public interface DiffPixelMaker {
	public int getDifferencePixel(Pixel a, Pixel b, Pixel difference);
	public int getSimilarityPixel(Pixel a, Pixel b, Pixel difference);
}
