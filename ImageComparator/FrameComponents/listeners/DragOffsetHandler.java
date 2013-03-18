package spider.utils.ImageComparator.FrameComponents.listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/12/13
 * Time: 2:05 PM
 */
public class DragOffsetHandler extends MouseAdapter {
	private Point offset;
	private Point lastMousePoint;
	private boolean tracking;

	private java.util.List<ActionListener> listeners;

	public DragOffsetHandler()
	{
		offset = new Point();
		listeners = new LinkedList<ActionListener>();
		tracking = true;
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		super.mouseDragged(mouseEvent);
		Point newPoint = mouseEvent.getPoint();
		if(tracking && SwingUtilities.isRightMouseButton(mouseEvent)) {
			offset.x -= (newPoint.x - lastMousePoint.x);
			offset.y -= (newPoint.y - lastMousePoint.y);
			valueChanged();
		}
		lastMousePoint = newPoint;
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		super.mouseMoved(mouseEvent);
		lastMousePoint = mouseEvent.getPoint();
	}

	public void setTracking(boolean tracking)
	{
		this.tracking = tracking;
	}

	public void resetOffset()
	{
		offset.x = offset.y = 0;
		valueChanged();
	}

	public void setOffset(Point newOffset)
	{
		offset = newOffset;
		valueChanged();
	}

	public Point getOffset() {
		return offset;
	}

	public void addActionListener(ActionListener listener)
	{
		listeners.add(listener);
	}

	private void valueChanged()
	{
		for(ActionListener listener : listeners)
		{
			listener.actionPerformed(new ActionEvent(this, 0, "Drag Offset Changed"));
		}
	}
}
