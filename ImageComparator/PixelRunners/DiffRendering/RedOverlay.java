package spider.utils.ImageComparator.PixelRunners.DiffRendering;

import spider.utils.ImageComparator.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/8/13
 * Time: 12:46 PM
 */
public class RedOverlay implements DiffPixelMaker {
	private int grayness;

	public RedOverlay(int grayness)
	{
		this.grayness = (grayness & 0xFF) << Pixel.ALPHA_SHIFT;
	}

	@Override
	public int getDifferencePixel(Pixel a, Pixel b, Pixel difference) {
		int clampedDifference = (int)Math.max(0x20, Math.min(0xFF, difference.length()));
		Pixel overlay = new Pixel(clampedDifference,255,0,0);
		Pixel under = new Pixel(getSimilarityPixel(a,b,difference));
		return Pixel.overlay(under,overlay).getARGB();
	}

	@Override
	public int getSimilarityPixel(Pixel a, Pixel b, Pixel difference) {
		Pixel grayOverlay = new Pixel(a.getGrayedARGB() & (grayness | 0x00FFFFFF));
		return Pixel.overlay(a, grayOverlay).getARGB();
	}
}
