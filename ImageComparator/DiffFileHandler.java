package spider.utils.ImageComparator;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.zip.*;

/**
 * Created with IntelliJ IDEA.
 * User: wesleymatson
 * Date: 3/12/13
 * Time: 1:21 PM
 */
public class DiffFileHandler {
	private BufferedImage imageA;
	private BufferedImage imageB;

	public DiffFileHandler(File diffFile) throws IOException {
		ZipInputStream zipInput = new ZipInputStream(new FileInputStream(diffFile));
		zipInput.getNextEntry();
		imageA = getNextEntryImage(zipInput);
		zipInput.closeEntry();
		zipInput.getNextEntry();
		imageB = getNextEntryImage(zipInput);
		zipInput.closeEntry();
		zipInput.close();
	}

	public BufferedImage getImageA() {
		return imageA;
	}

	public BufferedImage getImageB() {
		return imageB;
	}

	private BufferedImage getNextEntryImage(ZipInputStream zipInput) throws IOException {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		int numBytesRead;
		while((numBytesRead = zipInput.read(buffer, 0, buffer.length)) > -1) {
			bytesOut.write(buffer,0,numBytesRead);
		}
		return ImageIO.read(new ByteArrayInputStream(bytesOut.toByteArray()));
	}

	public static void writeDiffFile(BufferedImage imageA, BufferedImage imageB, File writeLocation) throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(writeLocation));

		ZipEntry entry = new ZipEntry("image A");
		out.putNextEntry(entry);
		ImageIO.write(imageA, "png",out);
		out.closeEntry();

		entry = new ZipEntry("image B");
		out.putNextEntry(entry);
		ImageIO.write(imageB, "png", out);
		out.closeEntry();

		out.flush();
		out.close();
	}
}
