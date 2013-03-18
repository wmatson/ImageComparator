package spider.utils.ImageComparator.FrameComponents.dialogs;

import spider.utils.ImageComparator.FrameComponents.*;
import spider.utils.ImageComparator.FrameComponents.dialogs.structs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/14/13
 * Time: 1:27 PM
 */
public class DragScreenSelectionListener extends MouseAdapter{

	private ScreenSelection struct;
	private ScreenSelection realCoords;
	private JFrame parent;
	private Robot robot;

	private JFrame newFrame;

	private BufferedImage result;

	private CountDownLatch blocker;

	public DragScreenSelectionListener(JFrame parent)
	{
		blocker = new CountDownLatch(1);
		this.struct = new ScreenSelection();
		this.realCoords = new ScreenSelection();
		this.parent = parent;

		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}

		newFrame = new JFrame();
		JPanel component = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.clearRect(0,0,getWidth(),getHeight());
				g.setColor( new Color(255,255,255,30));
				g.fillRect(0,0, getWidth(), getHeight());
				if(struct.tracking)
				{
					Rectangle rect = struct.getRect();
					int x = rect.x;
					int y = rect.y;
					int width = rect.width;
					int height = rect.height;
					g.clearRect(x, y, width, height);
					g.setColor(Color.BLUE);
					g.drawRect(x,y, width, height);
				}
			}
		};
		component.setBackground(new Color(255,255,255,30));
		newFrame.setContentPane(component);
		newFrame.setUndecorated(true);
		newFrame.setBackground(new Color(0,0,0,0));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();
		newFrame.setBounds(0,0,screen.width,screen.height);

		newFrame.addMouseListener(this);
		newFrame.addMouseMotionListener(this);
		newFrame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
	}

	public void show()
	{
		newFrame.setVisible(true);
		this.parent.setVisible(false);
	}

	public void await()
	{
		try {
			blocker.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		struct.point1 = e.getPoint();
		realCoords.point1 = e.getLocationOnScreen();
		struct.tracking = true;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Rectangle prevRect = struct.getRect();
		struct.point2 = e.getPoint();
		realCoords.point2 = e.getLocationOnScreen();
		Rectangle newRect = struct.getRect();
		int x = Math.min(prevRect.x,newRect.x);
		int y = Math.min(prevRect.y,newRect.y);
		int width = Math.max(prevRect.width, newRect.width);
		int height = Math.max(prevRect.height, newRect.height);
		newFrame.repaint(x,y,width+1,height+1);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		newFrame.dispose();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			throw new RuntimeException(e1);
		}
		result = robot.createScreenCapture(realCoords.getRect());
		parent.setVisible(true);
		blocker.countDown();
	}

	public BufferedImage getResult()
	{
		return result;
	}
}
