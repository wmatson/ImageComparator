package spider.utils.ImageComparator.PixelRunners;

import spider.utils.ImageComparator.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 2/26/13
 * Time: 1:50 PM
 */
public class PercentageDiffGetter implements PixelRunner
{
	private int differingPixels;
	private int threshold;
	private int totalPixels;

	public PercentageDiffGetter(int threshold, int totalPixels)
	{
		this.totalPixels = totalPixels;
		this.threshold = threshold;
	}

	@Override
	public void run(Pixel a, Pixel b, int x, int y) {
		Pixel difference = Pixel.getDifference(a, b);

		if(difference.length() > threshold)
		{
			differingPixels++;
		}
	}

	public float getPercentage()
	{
		return (differingPixels * 1.0f)/totalPixels;
	}
}
