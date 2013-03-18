package spider.utils.ImageComparator.FrameComponents;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/1/13
 * Time: 2:04 PM
 */
public class LabeledSlider extends JComponent
{
	private JSlider slider;
	private JLabel label;

	public static final int VERTICAL = JSlider.VERTICAL;
	public static final int HORIZONTAL = JSlider.HORIZONTAL;

	public LabeledSlider(int min, int max, String name, int orientation)
	{
		slider = new JSlider(orientation);
		slider.setMinimum(min);
		slider.setMaximum(max);
		label = new JLabel(name);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(slider);
		add(label);
	}

	public LabeledSlider(int min, int max, String name)
	{
		this(min,max, name, VERTICAL);
	}

	public void addChangeListener(ChangeListener listener)
	{
		slider.addChangeListener(listener);
	}

	public int getValue()
	{
		return slider.getValue();
	}
}
