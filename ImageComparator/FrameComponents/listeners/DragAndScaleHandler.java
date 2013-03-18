package spider.utils.ImageComparator.FrameComponents.listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/6/13
 * Time: 3:46 PM
 */
public class DragAndScaleHandler extends MouseAdapter implements MouseWheelListener {

	private Point location;
	private float scale;

	private Point lastMousePoint;

	private JComponent parent;

	public DragAndScaleHandler(JComponent parent)
	{
		location = new Point();
		scale = 1.0f;
		this.parent = parent;
		parent.addMouseWheelListener(this);
		parent.addMouseMotionListener(this);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int amount = e.getWheelRotation();
		scale -= amount/10.0f;
		parent.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		super.mouseDragged(mouseEvent);
		if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
			Point newPoint = mouseEvent.getPoint();
			location.x += (newPoint.x - lastMousePoint.x);
			location.y += (newPoint.y - lastMousePoint.y);
			lastMousePoint = newPoint;
			parent.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		super.mouseMoved(mouseEvent);
		lastMousePoint = mouseEvent.getPoint();
		parent.repaint();
	}

	public Point getLocation() {
		return location;
	}

	public float getScale() {
		return scale;
	}
}
