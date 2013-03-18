package spider.utils.ImageComparator;

import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 2/26/13
 * Time: 1:53 PM
 * A class to hold the percentage and diff image image difference data
 */
public class ImageDifference {
	private BufferedImage diffImage;
	private float percentDiff;

	public ImageDifference(float percentDiff, BufferedImage diffImage)
	{
		this.diffImage = diffImage;
		this.percentDiff = percentDiff;
	}

	public BufferedImage getDiffImage() {
		return diffImage;
	}

	public float getPercentageDifferent() {
		return percentDiff;
	}
}
