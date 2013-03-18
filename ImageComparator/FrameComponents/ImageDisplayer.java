package spider.utils.ImageComparator.FrameComponents;

import spider.utils.ImageComparator.FrameComponents.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/1/13
 * Time: 2:03 PM
 */
public class ImageDisplayer extends JComponent
{
	private BufferedImage currentImage;
	private DragAndScaleHandler listener;

	public ImageDisplayer(BufferedImage initial)
	{
		setImage(initial);
		listener = new DragAndScaleHandler(this);
	}

	public void setImage(BufferedImage image)
	{
		currentImage = image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		repaint();
	}

	public BufferedImage getImage()
	{
		return currentImage;
	}

	private boolean calculating = false;
	public void setCalculating(boolean calculating)
	{
		this.calculating = calculating;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D)graphics.create();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0, getWidth(), getHeight());
		double scale = listener.getScale();
		Point location = listener.getLocation();

		g.translate(location.x, location.y);
		g.scale(scale,scale);

		g.drawImage(currentImage, null, 0, 0);

		g.dispose();

		if(calculating) {
			graphics.setColor(new Color(68, 68, 68, 127));
			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.setColor(Color.WHITE);
			graphics.drawString("Calculating...",getWidth()/2,getHeight()/2);
		}
	}
}
