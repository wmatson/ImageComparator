package spider.utils.ImageComparator;

import spider.utils.ImageComparator.PixelRunners.*;
import spider.utils.ImageComparator.PixelRunners.DiffRendering.*;

import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 1/25/13
 * Time: 12:21 PM
 *
 * Utility class for comparing images.
 *
 * The threshold parameter in many methods is a value between 0 and 510 (inclusive)
 * threshold indicates how different two pixels may be before they are considered different in calculations
 * 510 is the difference between an opaque white pixel and a transparent black pixel
 * 0 is the difference between two pixels with the same ARGB value
 * Difference (for threshold) is calculated per-channel in ARGB color space and interpreted as a sum of differences.
 * The difference between 0xFFFFFFFF and 0xFEFDFCFB is 1 (alpha) + 2 (red) + 3 (green) + 4 (blue) which is 9
 */
public class ImageComparator {

	/**
	 * Returns a float between 0 and 1 representing the percentage of pixels differing between the two images.
	 * @param image1 The first image
	 * @param image2 The second image
	 * @param threshold a number between 0 and 510 (inclusive) to determine how different pixels may be before being considered different
	 * @return the percentage of differing pixels in the two images
	 */
	public static float getDiffPercentage(BufferedImage image1, BufferedImage image2, int threshold)
	{
		ImageComparer comparer = new ImageComparer(image1, image2);
		PercentageDiffGetter percentGetter = new PercentageDiffGetter(threshold,comparer.getLargerWidth()*comparer.getLargerHeight());
		return percentGetter.getPercentage();
	}

	/**
	 * Returns an image representing the difference between the correct image and the other image
	 * @param correctImage The image considered correct
	 * @param otherImage The other image
	 * @param threshold a number between 0 and 510 (inclusive) to determine how different pixels may be before being considered different
	 * @return the difference of the two images
	 */
	public static BufferedImage getDiffImage(BufferedImage correctImage, BufferedImage otherImage, int threshold)
	{
		ImageComparer comparer = new ImageComparer(correctImage,  otherImage);
		DiffImageGetter diffImageGetter = new DiffImageGetter(threshold,  comparer.getLargerWidth(), comparer.getLargerHeight(), new RedOverlay(0xDD));
		comparer.execute(diffImageGetter);
		return diffImageGetter.makeDiff();
	}

	public static ImageDifference getDiff(BufferedImage correctImage, BufferedImage b, int threshold)
	{
		return getDiff(correctImage, b, threshold, 0xAA);
	}

	public static ImageDifference getDiff(BufferedImage correctImage,BufferedImage b, int threshold, int grayness)
	{
		return getDiff(correctImage, b,threshold, new RedOverlay(grayness));
	}

	public static ImageDifference getDiff(BufferedImage correctImage,BufferedImage b, int threshold, DiffPixelMaker maker)
	{
		return getDiff(correctImage, b,threshold, maker, 0,0);
	}

	/**
	 * returns an ImageDifference (holding diff BufferedImage and diffPercentage) based on the two given images, using
	 * the given threshold, and moving the second image to the point specified by the offsets for comparison
	 * @param correctImage the first image (in some DiffPixelMakers, this will be considered the correct image.)
	 * @param b the second image (should not be considered the correct image in a DiffPixelMaker)
	 * @param threshold the threshold to consider two pixels different (see class documentation)
	 * @param maker the DiffPixelMaker to use for rendering the diff image
	 * @param offsetX the offset in the x direction to move b for comparison
	 * @param offsetY the offset in the y direction to move b for comparison
	 * @return an ImageDifference based on the parameters
	 */
	public static ImageDifference getDiff(BufferedImage correctImage,BufferedImage b, int threshold, DiffPixelMaker maker, int offsetX, int offsetY)
	{
		ImageComparer comparer = new ImageComparer(correctImage, b,offsetX,offsetY);
		PercentageDiffGetter percentGetter = new PercentageDiffGetter(threshold,comparer.getLargerWidth()*comparer.getLargerHeight());
		DiffImageGetter diffImageGetter = new DiffImageGetter(threshold, comparer.getLargerWidth(), comparer.getLargerHeight(), maker);
		comparer.execute(percentGetter,  diffImageGetter);
		ImageDifference result = new ImageDifference(percentGetter.getPercentage(),diffImageGetter.makeDiff());
		return result;
	}

	/**
	 * Gets a diff based on the entered script (Rhino (javascript)) and images
	 * The script is run per-pixel. The script has access to implicit objects 'a' and 'b' which each have the fields
	 * a, r, g, b for alpha, red, green, and blue respectively.  The script's output is in modifying the fields of
	 * an object identical to 'a' with the name 'out'.
	 * @param a the first image to compare (also default for the out parameter of the script)
	 * @param b the second image to compare
	 * @param script the script to run
	 * @param errorOut an array of size one to place errors into
	 * @return the diff created by the script
	 */
	public static BufferedImage getScriptDiff(BufferedImage a, BufferedImage b, String script, String[] errorOut)
	{
		ImageComparer comparer = new ImageComparer(a, b);
		ScriptPixelRunner runner = new ScriptPixelRunner(comparer.getLargerWidth(), comparer.getLargerHeight(), script);
		comparer.execute(runner);
		errorOut[0] = runner.getError();
		return runner.getOutput();
	}

	public static BufferedImage getScriptDiff(BufferedImage a, BufferedImage b, String script)
	{
		return getScriptDiff(a, b, script, new String[1]);
	}

	/**
	 * returns an argb pixel value for the given image at the given coordinates.
	 * Pixels outside of the given image are considered opaque black pixels.
	 * @param correctImage the image from which to get data
	 * @param x the x coordinate of the pixel to get
	 * @param y the y coordinate of the pixel to get
	 * @return an argb integer for the given pixel's data
	 */
	public static int safeGetRGB(BufferedImage correctImage, int x, int y) {
		int rgb = 0xFF000000;
		if(x < correctImage.getWidth() && y < correctImage.getHeight() && x >= 0 && y >= 0)
			rgb = correctImage.getRGB(x,y);
		return rgb;
	}

}
