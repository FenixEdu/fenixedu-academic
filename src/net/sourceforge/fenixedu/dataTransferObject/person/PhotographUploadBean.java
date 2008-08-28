package net.sourceforge.fenixedu.dataTransferObject.person;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class PhotographUploadBean implements Serializable {
    private static final long serialVersionUID = 1207257680390113173L;

    private static final int OUTPUT_PHOTO_WIDTH = 100;

    private static final int OUTPUT_PHOTO_HEIGHT = 100;

    private transient byte[] rawContents;

    private transient byte[] compressedContents;

    private File temporaryFile;

    private File tempCompressedFile;

    private String filename;

    private String contentType;

    public InputStream getFileInputStream() {
	try {
	    if (rawContents != null)
		return new ByteArrayInputStream(rawContents);
	    if (temporaryFile != null)
		return new FileInputStream(temporaryFile);
	    else
		return null;
	} catch (FileNotFoundException e) {
	    return null;
	}
    }

    public void setFileInputStream(InputStream inputStream) {
	try {
	    this.rawContents = new ByteArray(inputStream).getBytes();
	} catch (IOException e) {
	    this.rawContents = null;
	}
    }

    public InputStream getCompressedInputStream() {
	try {
	    if (compressedContents != null)
		return new ByteArrayInputStream(compressedContents);
	    if (tempCompressedFile != null)
		return new FileInputStream(tempCompressedFile);
	    else
		return null;
	} catch (FileNotFoundException e) {
	    return null;
	}
    }

    public int getRawSize() {
	return rawContents.length;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public void createTemporaryFiles() {
	try {
	    temporaryFile = rawContents != null ? FileUtils.copyToTemporaryFile(new ByteArrayInputStream(rawContents)) : null;
	    tempCompressedFile = compressedContents != null ? FileUtils.copyToTemporaryFile(new ByteArrayInputStream(
		    compressedContents)) : null;
	} catch (IOException exception) {
	    temporaryFile = null;
	    tempCompressedFile = null;
	}
    }

    public File getTemporaryFile() {
	return temporaryFile;
    }

    public File getTempCompressedFile() {
	return tempCompressedFile;
    }

    public void deleteTemporaryFiles() {
	if (temporaryFile != null)
	    temporaryFile.delete();
	if (tempCompressedFile != null)
	    tempCompressedFile.delete();
    }

    public String getContentType() {
	return contentType;
    }

    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    public void processImage() throws IOException {
	BufferedImage image = ImageIO.read(new ByteArrayInputStream(rawContents));
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	// calculate resize factor
	double resizeFactor = Math.min((double) OUTPUT_PHOTO_WIDTH / image.getWidth(), (double) OUTPUT_PHOTO_HEIGHT
		/ image.getHeight());

	// resize image
	AffineTransform tx = new AffineTransform();
	tx.scale(resizeFactor, resizeFactor);
	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	image = op.filter(image, null);

	// set compression
	ImageWriter writer = ImageIO.getImageWritersByMIMEType(ContentType.getContentType(contentType).getMimeType()).next();
	ImageWriteParam param = writer.getDefaultWriteParam();
	if (contentType.equals(ContentType.JPG)) {
	    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    param.setCompressionQuality(1);
	}

	// write to stream
	writer.setOutput(ImageIO.createImageOutputStream(outputStream));
	writer.write(null, new IIOImage(image, null, null), param);
	compressedContents = outputStream.toByteArray();
    }
}
