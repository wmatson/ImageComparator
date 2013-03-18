package spider.utils.ImageComparator;

import spider.utils.ImageComparator.FrameComponents.*;
import spider.utils.ImageComparator.FrameComponents.dialogs.*;
import spider.utils.ImageComparator.FrameComponents.listeners.*;
import spider.utils.ImageComparator.PixelRunners.DiffRendering.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 2/22/13
 * Time: 12:43 PM
 *
 * Simple GUI for testing parameters of ImageComparator
 */
public class ImageComparatorFrame extends JFrame {

	private FileChangeableImageDisplayer imageA;
	private FileChangeableImageDisplayer imageB;
	private ImageDisplayer diffImage;
	private ImageSplitDisplayer split;

	private ImageDisplayer scriptDiffImage;

	private JTabbedPane tabArea;

	private JLabel percentLabel;
	private JLabel thresholdLabel;
	private LabeledSlider thresholdSlider;
	private LabeledSlider graynessSlider;
	private JButton diffSwapButton;
	private JButton splitterSwapButton;
	private JTextArea scriptArea;
	private JButton executeScriptButton;
	private JCheckBox blackWhiteBox;

	private JFileChooser imageChooser;
	private JFileChooser diffChooser;

	private JMenuItem openDiffItem;
	JMenuItem saveDiffItem;

	private DragOffsetHandler offsetHandler;
	JButton alignTop;
	JButton alignLeft;
	JButton alignBottom;
	JButton alignRight;
	JButton unalign;
	JSlider horizontalAlignmentSlider;
	JSlider verticalAlignmentSlider;

	private ImageDisplayer snapshotDisplayer;
	private JPanel testPane;

	public ImageComparatorFrame(BufferedImage initialA, BufferedImage initialB)
	{
		Container contentPane = getContentPane();


		imageChooser = new JFileChooser();
		imageChooser.setFileFilter(new FileNameExtensionFilter("Image", "png", "jpg", "bmp", "gif"));
		diffChooser = new JFileChooser();
		diffChooser.setFileFilter(new FileNameExtensionFilter("Image Diff", "diff"));

		offsetHandler = new DragOffsetHandler();

		imageA = new FileChangeableImageDisplayer(initialA, imageChooser);
		imageB = new FileChangeableImageDisplayer(initialB, imageChooser);

		diffImage = new ImageDisplayer(new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB));
		diffImage.addMouseMotionListener(offsetHandler);
		JPanel diffImagePanel = new JPanel(new BorderLayout());
		diffImagePanel.add(diffImage, BorderLayout.CENTER);

		thresholdSlider = new LabeledSlider(0, 510, "Threshold", LabeledSlider.HORIZONTAL);
		graynessSlider = new LabeledSlider(0, 0xFF, "Grayness", LabeledSlider.HORIZONTAL);
		percentLabel = new JLabel("SliderValue");
		thresholdLabel = new JLabel("Threshold: " + thresholdSlider.getValue());
		diffSwapButton = new JButton("Swap Images");
		blackWhiteBox = new JCheckBox("Black/White Diff Render");
		alignTop = new JButton("Align Top");
		alignLeft = new JButton("Align Left");
		alignBottom = new JButton("Align Bottom");
		alignRight = new JButton("Align Right");
		unalign = new JButton("Reset Alignment");
		JPanel outerAlignmentPanel = new JPanel(new BorderLayout());
		JPanel alignmentPanel = new JPanel(new BorderLayout());
		alignmentPanel.add(alignTop, BorderLayout.NORTH);
		alignmentPanel.add(alignLeft,  BorderLayout.WEST);
		alignmentPanel.add(alignRight, BorderLayout.EAST);
		alignmentPanel.add(alignBottom, BorderLayout.SOUTH);
		alignmentPanel.add(unalign, BorderLayout.CENTER);
		outerAlignmentPanel.add(alignmentPanel);
		horizontalAlignmentSlider = new JSlider(JSlider.HORIZONTAL);
		horizontalAlignmentSlider.setValue(0);
		verticalAlignmentSlider = new JSlider(JSlider.VERTICAL);
		verticalAlignmentSlider.setValue(0);
		outerAlignmentPanel.add(horizontalAlignmentSlider, BorderLayout.SOUTH);
		outerAlignmentPanel.add(verticalAlignmentSlider , BorderLayout.EAST);

		diffImagePanel.add(getControlPanel(thresholdSlider, graynessSlider, outerAlignmentPanel,getVerticalBoxPanel(diffSwapButton, blackWhiteBox), getVerticalBoxPanel(percentLabel, thresholdLabel)),BorderLayout.SOUTH);


		scriptDiffImage = new ImageDisplayer(initialA);
		scriptArea = new JTextArea(50,10);
		scriptArea.setText("/*Javascript\n" +
			"'a' and 'b' are the source pixels,\n" +
			" they are objects with fields a, r, g, and b\n" +
			" for alpha, red, green, and blue channels respectively,\n" +
			" values for each field are 0-255 (inclusive)\n" +
			"Store output in the object 'out' (defaults to have the same values and fields as 'a')*/\n" +
			"if(a.a != b.a || a.r != b.r || a.g != b.g || a.b != b.b)\n" +
			"{\n" +
			"\tout.a = out.r = out.g = out.b = 0;\n" +
			"} else {\n" +
			"\tout.a = out.r = out.g = out.b = 255;\n" +
			"}");
		scriptArea.setMinimumSize(new Dimension(0,0));
		executeScriptButton = new JButton("Execute Script");
		JPanel scriptControlPanel = new JPanel();
		scriptControlPanel.setLayout(new BorderLayout());
		scriptControlPanel.add(scriptArea, BorderLayout.CENTER);
		scriptControlPanel.add(executeScriptButton, BorderLayout.SOUTH);
		JSplitPane scriptPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, scriptDiffImage,  scriptControlPanel);
		scriptPanel.setDividerLocation(300);
		scriptPanel.setResizeWeight(1);

		split = new ImageSplitDisplayer(initialA, initialB);
		JPanel splitPanel = new JPanel(new BorderLayout());
		splitPanel.add(split, BorderLayout.CENTER);
		splitterSwapButton = new JButton("Swap Images");
		splitPanel.add(getControlPanel(splitterSwapButton), BorderLayout.SOUTH);


		tabArea = new JTabbedPane();
		tabArea.setBackground(Color.DARK_GRAY);
		contentPane.add(tabArea, BorderLayout.CENTER);

		tabArea.addTab("Diff", diffImagePanel);
		tabArea.setMnemonicAt(0, KeyEvent.VK_D);
		tabArea.addTab("Image A", imageA);
		tabArea.setMnemonicAt(1, KeyEvent.VK_A);
		tabArea.setDisplayedMnemonicIndexAt(1, "Image A".length() - 1);
		tabArea.addTab("Image B", imageB);
		tabArea.setMnemonicAt(2, KeyEvent.VK_B);
		tabArea.setBackground(Color.DARK_GRAY);
		tabArea.addTab("Splitter", splitPanel);
		tabArea.setMnemonicAt(3, KeyEvent.VK_S);
		tabArea.addTab("Script", scriptPanel);

		JButton test = new JButton("Test Snapshot");
		JButton swap = new JButton("Swap Images");
		swap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				swapImages();
			}
		});
		testPane = new JPanel(new BorderLayout());
		snapshotDisplayer = new ImageDisplayer(initialA);
		testPane.add(getControlPanel(test,swap), BorderLayout.NORTH);
		testPane.add(snapshotDisplayer);
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				final DragScreenSelectionListener screenGetter = new DragScreenSelectionListener(ImageComparatorFrame.this);
				screenGetter.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						screenGetter.await();
						imageA.setImage(screenGetter.getResult());
					}
				}).start();

			}
		});

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if(e.getKeyChar() == ']' && tabArea.getTabCount() > 5)
				{
					tabArea.removeTabAt(5);
				} else {
					tabArea.addTab("Snapshot",testPane);
				}
				return false;
			}
		});


		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		openDiffItem = new JMenuItem("Open Diff...");
		openDiffItem.setMnemonic('O');
		fileMenu.add(openDiffItem);
		saveDiffItem = new JMenuItem("Save Diff...");
		fileMenu.add(saveDiffItem);
		menubar.add(fileMenu);

		this.setJMenuBar(menubar);


		//contentPane.add(sliderPanel, BorderLayout.SOUTH);

		hookEvents();

		recalculateAlignmentSliderRanges();
		recalculateDiff();
	}

	private JPanel getVerticalBoxPanel(Component... components)
	{
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
		for(Component c : components)
		{
			result.add(c);
		}
		return result;
	}

	private JPanel getControlPanel(int flowLayoutDirection, Component... components)
	{
		JPanel result = new JPanel(new FlowLayout(flowLayoutDirection));
		for(Component component : components)
		{
			result.add(component);
		}
		return result;
	}

	private JPanel getControlPanel(Component... components)
	{
		return getControlPanel(FlowLayout.LEADING, components);
	}

	private void hookEvents() {
		hookImageChangeEvents();
		hookImageSwapEvents();
		hookDiffControlEvents();
		hookScriptEvents();
		hookOffsetEvents();
		hookMenuBarEvents();
	}

	private void hookMenuBarEvents() {
		openDiffItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int openReturn = diffChooser.showOpenDialog(ImageComparatorFrame.this);
				if(openReturn == JFileChooser.APPROVE_OPTION)
				{
					try {
						DiffFileHandler handler = new DiffFileHandler(diffChooser.getSelectedFile());
						imageA.setImage(handler.getImageA());
						imageB.setImage(handler.getImageB());
						offsetHandler.resetOffset();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(ImageComparatorFrame.this, "Selected file was not properly formatted.");
					}
				}
			}
		});
		saveDiffItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = diffChooser.showSaveDialog(ImageComparatorFrame.this);
				File file = diffChooser.getSelectedFile();
				if (returnValue == JFileChooser.APPROVE_OPTION &&
					(!file.exists() || JOptionPane.showConfirmDialog(ImageComparatorFrame.this, "Overwrite File?","Overwrite Diff", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
					try {
						DiffFileHandler.writeDiffFile(imageA.getUnderlyingImageDisplayer().getImage(), imageB.getUnderlyingImageDisplayer().getImage(), diffChooser.getSelectedFile());
					} catch (IOException e1) {
						throw new RuntimeException(e1);
					}
				}
			}
		});
	}

	private void hookScriptEvents() {
		executeScriptButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				executeScriptDiff();
			}
		});
	}

	private void hookDiffControlEvents() {
		thresholdSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				thresholdLabel.setText("Threshold: " + thresholdSlider.getValue());
				recalculateDiff();
			}
		});
		graynessSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				recalculateDiff();
			}
		});
		blackWhiteBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recalculateDiff();
			}
		});
	}

	private void hookImageSwapEvents() {
		diffSwapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				swapImages();
			}
		});
		splitterSwapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				swapImages();
			}
		});
	}

	private void hookImageChangeEvents() {
		imageA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				recalculateDiff();
				BufferedImage newImage = imageA.getUnderlyingImageDisplayer().getImage();
				split.setImage(newImage);
				snapshotDisplayer.setImage(newImage);
				offsetHandler.resetOffset();
				recalculateAlignmentSliderRanges();
			}
		});
		imageB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				recalculateDiff();
				split.setImage2(imageB.getUnderlyingImageDisplayer().getImage());
				offsetHandler.resetOffset();
			}
		});
	}

	private void hookOffsetEvents() {
		offsetHandler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point offset = offsetHandler.getOffset();
				horizontalAlignmentSlider.setValue(-offset.x);
				verticalAlignmentSlider.setValue(offset.y);
				recalculateDiff();
			}
		});
		alignTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point offset = offsetHandler.getOffset();
				offset.y = 0;
				offsetHandler.setOffset(offset);
			}
		});
		alignLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point offset = offsetHandler.getOffset();
				offset.x = 0;
				offsetHandler.setOffset(offset);
			}
		});
		alignRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point offset = offsetHandler.getOffset();
				BufferedImage a = imageA.getUnderlyingImageDisplayer().getImage();
				BufferedImage b = imageB.getUnderlyingImageDisplayer().getImage();
				offset.x = b.getWidth() - a.getWidth();
				offsetHandler.setOffset(offset);
			}
		});
		alignBottom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point offset = offsetHandler.getOffset();
				BufferedImage a = imageA.getUnderlyingImageDisplayer().getImage();
				BufferedImage b = imageB.getUnderlyingImageDisplayer().getImage();
				offset.y = b.getHeight() - a.getHeight();
				offsetHandler.setOffset(offset);
			}
		});
		unalign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				offsetHandler.resetOffset();
			}
		});
		horizontalAlignmentSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Point offset = offsetHandler.getOffset();
				offset.x = -horizontalAlignmentSlider.getValue();
				offsetHandler.setOffset(offset);
			}
		});
		verticalAlignmentSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Point offset = offsetHandler.getOffset();
				offset.y = verticalAlignmentSlider.getValue();
				offsetHandler.setOffset(offset);
			}
		});
	}

	private class DiffRecalculator extends SwingWorker<ImageDifference,Object> {

		@Override
		protected ImageDifference doInBackground() throws Exception {
			publish();
			BufferedImage a = imageA.getUnderlyingImageDisplayer().getImage();
			BufferedImage b = imageB.getUnderlyingImageDisplayer().getImage();
			int threshold = thresholdSlider.getValue();
			DiffPixelMaker maker;
			if(blackWhiteBox.isSelected())
			{
				maker = new SolidDifference(Pixel.BLACK, Pixel.WHITE);
			} else {
				maker = new RedOverlay(graynessSlider.getValue());
			}
			return ImageComparator.getDiff(a,b,threshold,maker,offsetHandler.getOffset().x,offsetHandler.getOffset().y);
		}

		@Override
		protected void process(List<Object> objects) {
			super.process(objects);
			percentLabel.setText("Percent Diff: Calculating...");
			diffImage.setCalculating(true);
		}

		@Override
		protected void done() {
			try {
				percentLabel.setText(String.format("Percent Diff: %.3f", 100 * get().getPercentageDifferent()));
				diffImage.setImage(get().getDiffImage());
				diffImage.setCalculating(false);
			} catch (InterruptedException e) {
				//silent
			} catch (ExecutionException e) {
				//silent
			} catch (CancellationException e)
			{
				//silent, meant to happen if it isn't done.
			}
		}
	}

	private DiffRecalculator recalculator = new DiffRecalculator();
	private void recalculateDiff()
	{
		if(recalculator.getState().equals(SwingWorker.StateValue.PENDING)) {
			recalculator.execute();
		} else {
			if(recalculator.getState().equals(SwingWorker.StateValue.STARTED)) {
				recalculator.cancel(true);
			}
			recalculator = new DiffRecalculator();
			recalculator.execute();
		}
	}

	private void recalculateAlignmentSliderRanges()
	{
		BufferedImage a = imageA.getUnderlyingImageDisplayer().getImage();
		horizontalAlignmentSlider.setMinimum(-a.getWidth());
		horizontalAlignmentSlider.setMaximum(a.getWidth());
		verticalAlignmentSlider.setMinimum(-a.getHeight());
		verticalAlignmentSlider.setMaximum(a.getHeight());
	}

	private class ScriptDiffCalculator extends SwingWorker<BufferedImage,Object> {

		@Override
		protected BufferedImage doInBackground() throws Exception {
			publish();
			return ImageComparator.getScriptDiff(imageA.getUnderlyingImageDisplayer().getImage(), imageB.getUnderlyingImageDisplayer().getImage(), scriptArea.getText());
		}

		@Override
		protected void process(List<Object> objects) {
			super.process(objects);
			scriptDiffImage.setCalculating(true);
			tabArea.setTitleAt(4,"Script (Working...)");
		}

		@Override
		protected void done() {
			try {
				scriptDiffImage.setImage(get());
				scriptDiffImage.setCalculating(false);
				tabArea.setTitleAt(4,"Script");
			} catch (InterruptedException e) {
				//silent
			} catch (ExecutionException e) {
				//silent
			} catch (CancellationException e)
			{
				//silent, meant to happen if it isn't done.
			}
		}
	}

	private ScriptDiffCalculator scriptCalculator = new ScriptDiffCalculator();
	private void executeScriptDiff()
	{
		if(scriptCalculator.getState().equals(SwingWorker.StateValue.PENDING)) {
			scriptCalculator.execute();
		} else {
			if(scriptCalculator.getState().equals(SwingWorker.StateValue.STARTED)) {
				scriptCalculator.cancel(true);
			}
			scriptCalculator= new ScriptDiffCalculator();
			scriptCalculator.execute();
		}
	}

	private void swapImages()
	{
		BufferedImage temp = imageA.getUnderlyingImageDisplayer().getImage();
		imageA.getUnderlyingImageDisplayer().setImage(imageB.getUnderlyingImageDisplayer().getImage());
		imageB.getUnderlyingImageDisplayer().setImage(temp);
		split.setImage(imageA.getUnderlyingImageDisplayer().getImage());
		split.setImage2(imageB.getUnderlyingImageDisplayer().getImage());
		snapshotDisplayer.setImage(imageA.getUnderlyingImageDisplayer().getImage());
		offsetHandler.resetOffset();
		recalculateDiff();
		recalculateAlignmentSliderRanges();
		repaint();
	}



	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		BufferedImage correct;
		BufferedImage other;
		if(args.length == 1) {
			DiffFileHandler handler = new DiffFileHandler(new File(args[0]));
			correct = handler.getImageA();
			other = handler.getImageB();
		} else {
			correct = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
			createFace1(correct);

			other = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
			createFace2(other);
		}

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();

		final JFrame frame = new ImageComparatorFrame(correct, other);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds((int) screenSize.getWidth() / 8, (int) screenSize.getHeight() / 8, (int) (3 * screenSize.getWidth() / 4), (int) (3 * screenSize.getHeight() / 4));
		frame.setVisible(true);
	}

	private static void createFace2(BufferedImage other) {
		final int width = other.getWidth();
		final int height = other.getHeight();
		Graphics g = other.getGraphics();
		Graphics2D g2 = (Graphics2D) g.create();
		GradientPaint gp = new GradientPaint(0,0,Color.BLUE,width-1,height-1,Color.YELLOW);
		g2.setPaint(gp);
		g2.fillRect(0, 0, width-1, height-1);
		g2.dispose();
		g.drawRect(0, 0, width-1, height-1);
		g.drawOval(0, 0, width-1, height-1);
		g.drawOval(width/5, height/5, width/10, height/10);
		g.drawRect(3*width/5, height/5, width/10, height/10);
		g.drawOval(2*width/5, 3*width/5, width/10, height/10);
	}

	private static void createFace1(BufferedImage other) {
		final int width = other.getWidth();
		final int height = other.getHeight();
		Graphics g = other.getGraphics();
		Graphics2D g2 = (Graphics2D) g.create();
		GradientPaint gp = new GradientPaint(0,0,Color.BLUE,width-1,height-1,Color.GREEN);
		g2.setPaint(gp);
		g2.fillRect(0, 0, width-1, height-1);
		g2.dispose();
		g.drawRect(0, 0, width-1, height-1);
		g.drawOval(0, 0, width-1, height-1);
		g.drawOval(width/5, height/5, width/10, height/10);
		g.drawOval(3 * width / 5, height / 5, width / 10, height / 10);
		g.drawOval(2*width/5, 3*width/5, width/10, height/10);
	}
}
