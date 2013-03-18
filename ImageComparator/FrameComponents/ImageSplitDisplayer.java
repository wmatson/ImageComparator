package spider.utils.ImageComparator.FrameComponents;

import spider.utils.ImageComparator.FrameComponents.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/1/13
 * Time: 3:09 PM
 */
public class ImageSplitDisplayer extends JComponent {
	private BufferedImage image;
	private BufferedImage image2;
	private DragAndScaleHandler listener;

	public ImageSplitDisplayer(BufferedImage initial, BufferedImage initial2)
	{
		listener = new DragAndScaleHandler(this);
		image = initial;
		image2 = initial2;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	public void setImage2(BufferedImage image)
	{
		this.image2 = image;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Point imagePoint = listener.getLocation();
		float scale = listener.getScale();
		Graphics2D g = (Graphics2D) graphics;
		Rectangle left = new Rectangle(0,0, getWidth()/2, getHeight());
		Rectangle right = new Rectangle(left.width, 0, left.width,  left.height);
		g.setClip(left);
		Graphics2D inner = (Graphics2D) g.create();
		inner.translate(imagePoint.x, imagePoint.y);
		inner.scale(scale, scale);
		inner.drawImage(image, null, 0, 0);
		inner.dispose();
		g.setClip(null);
		g.setClip(right);
		inner = (Graphics2D) g.create();
		inner.translate(imagePoint.x, imagePoint.y);
		inner.scale(scale, scale);
		inner.drawImage(image2,null,0,0);
		inner.dispose();
		g.setColor(Color.BLACK);
		g.drawLine(right.width, 0,right.width, right.height);
	}
}
