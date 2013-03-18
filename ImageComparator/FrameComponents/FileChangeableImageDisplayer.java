package spider.utils.ImageComparator.FrameComponents;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/1/13
 * Time: 2:08 PM
 */
public class FileChangeableImageDisplayer extends JComponent
{
	private ImageDisplayer displayer;
	private JFileChooser chooser;
	private JButton imageChangeButton;
	private List<ActionListener> listeners;

	public FileChangeableImageDisplayer(BufferedImage initial, JFileChooser chooser)
	{
		listeners = new LinkedList<ActionListener>();
		setLayout(new BorderLayout());
		displayer = new ImageDisplayer(initial);
		add(displayer,  BorderLayout.CENTER);
		this.chooser = chooser;
		imageChangeButton = new JButton("Choose Image...");
		imageChangeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				openFileDialog();
			}
		});
		add(imageChangeButton, BorderLayout.NORTH);
	}

	public void addActionListener(ActionListener listener)
	{
		listeners.add(listener);
	}

	private void fireAction()
	{
		for(ActionListener listener : listeners)
		{
			listener.actionPerformed(new ActionEvent(this, 0, "ImageChanged"));
		}
	}

	public ImageDisplayer getUnderlyingImageDisplayer()
	{
		return displayer;
	}

	public void setImage(BufferedImage image)
	{
		displayer.setImage(image);
		fireAction();
	}

	private void openFileDialog()
	{
		try {
			int openReturn = chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			if(openReturn == JFileChooser.APPROVE_OPTION) {
				BufferedImage image = ImageIO.read(file);
				setImage(image);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
