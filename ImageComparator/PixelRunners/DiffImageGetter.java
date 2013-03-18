package spider.utils.ImageComparator.PixelRunners;

import spider.utils.ImageComparator.*;
import spider.utils.ImageComparator.PixelRunners.DiffRendering.*;

import java.awt.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 2/26/13
 * Time: 1:49 PM
 */
public class DiffImageGetter implements PixelRunner
{

	private BufferedImage diff;
	private int threshold;
	private int largerWidth;
	private int largerHeight;
	private DiffPixelMaker diffRenderer;

	public DiffImageGetter(int threshold, int largerWidth, int largerHeight, DiffPixelMaker diffRenderer)
	{
		diff = new BufferedImage(largerWidth, largerHeight, BufferedImage.TYPE_INT_ARGB);
		this.threshold = threshold;
		this.largerHeight = largerHeight;
		this.largerWidth = largerWidth;
		this.diffRenderer = diffRenderer;
	}

	@Override
	public void run(Pixel a, Pixel b, int x, int y) {

		Pixel difference = Pixel.getDifference(a, b);
		if(difference.length() > threshold)
		{
			diff.setRGB(x,y, diffRenderer.getDifferencePixel(a,b,difference));
		} else {
			diff.setRGB(x,y, diffRenderer.getSimilarityPixel(a,b,difference));
		}
	}

	public BufferedImage makeDiff()
	{
		BufferedImage overlay = new BufferedImage(largerWidth, largerHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = overlay.createGraphics();
		graphics.drawImage(diff, null, 0,0);
		return overlay;
	}
}
