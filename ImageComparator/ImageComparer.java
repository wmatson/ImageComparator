package spider.utils.ImageComparator;

import spider.utils.ImageComparator.PixelRunners.*;

import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 2/25/13
 * Time: 12:49 PM
 */
public class ImageComparer {
	private BufferedImage imageA;
	private BufferedImage imageB;
	private int largerWidth;
	private int largerHeight;
	private int offsetX;
	private int offsetY;

	public ImageComparer(BufferedImage a, BufferedImage b, int offsetX, int offsetY)
	{
		imageA = a;
		imageB = b;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		largerWidth = Math.max(a.getWidth(), b.getWidth());
		largerHeight = Math.max(a.getHeight(), b.getHeight());
	}

	public ImageComparer(BufferedImage a, BufferedImage b)
	{
		this(a,b,0,0);
	}

	public BufferedImage[] getSourceImages()
	{
		return new BufferedImage[]{imageA, imageB};
	}

	public void execute(PixelRunner... runners)
	{

		for(int x = 0; x < largerWidth; x++)
		{
			for(int y = 0; y < largerHeight; y++)
			{
				Pixel pixelA = new Pixel(ImageComparator.safeGetRGB(imageA, x, y));
				Pixel pixelB = new Pixel(ImageComparator.safeGetRGB(imageB, x+offsetX, y+offsetY));
				for(PixelRunner runner : runners) {
					runner.run(pixelA, pixelB, x, y);
				}
			}
		}
	}

	public int getLargerWidth() {
		return largerWidth;
	}

	public int getLargerHeight() {
		return largerHeight;
	}
}
