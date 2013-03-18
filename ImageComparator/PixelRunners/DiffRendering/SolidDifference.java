package spider.utils.ImageComparator.PixelRunners.DiffRendering;

import spider.utils.ImageComparator.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/8/13
 * Time: 12:54 PM
 */
public class SolidDifference implements DiffPixelMaker{

	private Pixel difference;
	private Pixel similarity;

	public SolidDifference(Pixel difference, Pixel similarity)
	{
		this.difference = difference;
		this.similarity = similarity;
	}

	@Override
	public int getDifferencePixel(Pixel a, Pixel b, Pixel difference) {
		return this.difference.getARGB();
	}

	@Override
	public int getSimilarityPixel(Pixel a, Pixel b, Pixel difference) {
		return similarity.getARGB();
	}
}
