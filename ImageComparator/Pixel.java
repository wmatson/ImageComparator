package spider.utils.ImageComparator;

public class Pixel
{
	public static final Pixel BLACK = new Pixel(0xFF000000);
	public static final Pixel WHITE = new Pixel(0xFFFFFFFF);

	private int argb;

	public static int ALPHA_SHIFT = 24;
	public static int RED_SHIFT = 16;
	public static int GREEN_SHIFT = 8;
	public static int BLUE_SHIFT = 0;

	public Pixel(int argb)
	{
		this.argb = argb;
	}

	public Pixel(int alpha, int red, int green, int blue)
	{
		this(((alpha & 0xFF) << ALPHA_SHIFT) | ((red & 0xFF) << RED_SHIFT) | ((green & 0xFF) << GREEN_SHIFT) | ((blue & 0xFF) << BLUE_SHIFT));
	}

	public int getAlpha()
	{
		return (argb & 0xFF000000) >> ALPHA_SHIFT;
	}

	public int getRed()
	{
		return (argb & 0x00FF0000) >> RED_SHIFT;
	}

	public int getGreen()
	{
		return (argb & 0x0000FF00) >> GREEN_SHIFT;
	}

	public int getBlue()
	{
		return (argb & 0x000000FF) >> BLUE_SHIFT;
	}

	public int getARGB()
	{
		return argb;
	}

	public int getGrayedARGB()
	{
		int sum = getRed() + getGreen() + getBlue();
		int average = sum/3;
		return 0xFF000000 | (average << RED_SHIFT) | (average << GREEN_SHIFT) | (average << BLUE_SHIFT);
	}

	/**
	 * returns the length of this Pixel, interpreting the Pixel as a vector in ARGB space
	 * @return the length of the color vector represented by this Pixel
	 */
	public double length()
	{
		return Math.sqrt(Math.pow(getRed(), 2) + Math.pow(getGreen(), 2) + Math.pow(getBlue(),  2) + Math.pow(getAlpha(), 2));
	}

	public static Pixel overlay(Pixel toCover, Pixel top)
	{
		float alphaMixer = (top.getAlpha()&0xFF)/255.0f;
		int red = (int) (alphaMixer*top.getRed() + (1-alphaMixer)*toCover.getRed());
		int green = (int) (alphaMixer*top.getGreen() + (1-alphaMixer)*toCover.getGreen());
		int blue = (int) (alphaMixer*top.getBlue() + (1-alphaMixer)*toCover.getBlue());
		return new Pixel(255,red,green,blue);
	}

	public static Pixel getDifference(Pixel a, Pixel b)
	{
		int alpha = Math.abs(a.getAlpha() - b.getAlpha());
		int red = Math.abs(a.getRed() - b.getRed());
		int green = Math.abs(a.getGreen() - b.getGreen());
		int blue = Math.abs(a.getBlue() - b.getBlue());

		return new Pixel(combine(alpha, red, green, blue));
	}

	public static Pixel average(Pixel... pixels)
	{
		int alpha = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		for(Pixel pixel : pixels)
		{
			alpha += pixel.getAlpha();
			red += pixel.getRed();
			green += pixel.getGreen();
			blue += pixel.getBlue();
		}
		alpha /= pixels.length;
		red /= pixels.length;
		blue /= pixels.length;
		green /= pixels.length;

		return new Pixel(combine(alpha, red, green, blue));

	}

	public static int combine(int alpha, int red, int green, int blue)
	{
		int overall = 0;
		overall |= alpha << ALPHA_SHIFT;
		overall |= red << RED_SHIFT;
		overall |= green << GREEN_SHIFT;
		overall |= blue << BLUE_SHIFT;
		return overall;
	}
}