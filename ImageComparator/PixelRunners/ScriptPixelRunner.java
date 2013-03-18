package spider.utils.ImageComparator.PixelRunners;

import spider.utils.ImageComparator.*;

import javax.script.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/4/13
 * Time: 1:53 PM
 */
public class ScriptPixelRunner implements  PixelRunner {
	private BufferedImage out;
	private CompiledScript script;
	private ScriptEngine engine;
	private String error;
	private boolean hadError;

	public ScriptPixelRunner(int largerWidth, int largerHeight, String script)
	{
		engine = new ScriptEngineManager().getEngineByName("javascript");
		out = new BufferedImage(largerWidth, largerHeight, BufferedImage.TYPE_INT_ARGB);
		hadError = false;
		try {
			this.script = ((Compilable) engine).compile(script);
		} catch (ScriptException e) {
			error = e.getMessage();
			hadError = true;
		}
	}

	@Override
	public void run(Pixel a, Pixel b, int x, int y) {
		if(!hadError) {
			try {
				PixelInfo infoA = new PixelInfo(a);
				PixelInfo infoB = new PixelInfo(b);
				PixelInfo infoOut = new PixelInfo(a);
				Bindings bindings = new SimpleBindings();
				bindings.put("a", infoA);
				bindings.put("b", infoB);
				bindings.put("out", infoOut);
				script.eval(bindings);
				out.setRGB(x,y,infoOut.getARGB());
			} catch (ScriptException e) {
				error = e.getMessage();
				hadError = true;
				System.out.println(error);
			}
		}
	}

	public BufferedImage getOutput()
	{
		return out;
	}

	public String getError()
	{
		return error;
	}

	public class PixelInfo
	{
		public int a;
		public int r;
		public int g;
		public int b;

		public PixelInfo(Pixel pixel)
		{
			a = pixel.getAlpha();
			r = pixel.getRed();
			g = pixel.getGreen();
			b = pixel.getBlue();
		}

		public int getARGB()
		{
			return Pixel.combine(a,r,g,b);
		}
	}
}
