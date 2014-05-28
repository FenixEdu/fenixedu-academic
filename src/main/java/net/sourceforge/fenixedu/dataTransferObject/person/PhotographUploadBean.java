/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import java.awt.color.CMMException;
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

import com.google.common.io.BaseEncoding;

public class PhotographUploadBean implements Serializable {
    public class UnableToProcessTheImage extends Exception {
        private static final long serialVersionUID = 1728978041377445492L;
    }

    private static final long serialVersionUID = 1207257680390113173L;

    private static final int OUTPUT_PHOTO_WIDTH = 100;

    private static final int OUTPUT_PHOTO_HEIGHT = 100;

    private transient byte[] rawContents;

    private transient byte[] compressedContents;

    private File temporaryFile;

    private File tempCompressedFile;

    private String filename;

    private String contentType;

    private String base64RawThumbnail;

    private String base64RawContent;

    private String username;

    public InputStream getFileInputStream() throws FileNotFoundException {
        if (base64RawContent != null && base64RawThumbnail != null) {
            this.rawContents = BaseEncoding.base64().decode(base64RawContent);
            this.compressedContents = BaseEncoding.base64().decode(base64RawThumbnail);
            return new ByteArrayInputStream(rawContents);
        }
        if (rawContents != null) {
            return new ByteArrayInputStream(rawContents);
        }
        if (temporaryFile != null) {
            return new FileInputStream(temporaryFile);
        } else {
            return null;
        }
    }

    public void setFileInputStream(InputStream inputStream) throws IOException {
        this.rawContents = (inputStream != null) ? new ByteArray(inputStream).getBytes() : null;
    }

    public String getBase64RawThumbnail() {
        return base64RawThumbnail;
    }

    public void setBase64RawThumbnail(String base64RawThumbnail) {
        this.base64RawThumbnail = base64RawThumbnail;
    }

    public String getBase64RawContent() {
        return base64RawContent;
    }

    public void setBase64RawContent(String base64RawContent) {
        this.base64RawContent = base64RawContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InputStream getCompressedInputStream() throws FileNotFoundException {
        if (compressedContents != null) {
            return new ByteArrayInputStream(compressedContents);
        }
        if (tempCompressedFile != null) {
            return new FileInputStream(tempCompressedFile);
        } else {
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

    public void createTemporaryFiles() throws IOException {
        temporaryFile = rawContents != null ? FileUtils.copyToTemporaryFile(new ByteArrayInputStream(rawContents)) : null;
        tempCompressedFile =
                compressedContents != null ? FileUtils.copyToTemporaryFile(new ByteArrayInputStream(compressedContents)) : null;
    }

    public File getTemporaryFile() {
        return temporaryFile;
    }

    public File getTempCompressedFile() {
        return tempCompressedFile;
    }

    public void deleteTemporaryFiles() {
        if (temporaryFile != null) {
            temporaryFile.delete();
        }
        if (tempCompressedFile != null) {
            tempCompressedFile.delete();
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void processImage() throws IOException, UnableToProcessTheImage {
        if (base64RawContent != null && base64RawThumbnail != null) {
            return;
        }
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(rawContents));
            if (image == null) {
                throw new UnableToProcessTheImage();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // calculate resize factor
            double resizeFactor =
                    Math.min((double) OUTPUT_PHOTO_WIDTH / image.getWidth(), (double) OUTPUT_PHOTO_HEIGHT / image.getHeight());

            if (resizeFactor == 1) {
                compressedContents = rawContents;
            } else {
                // resize image
                AffineTransform tx = new AffineTransform();
                tx.scale(resizeFactor, resizeFactor);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);

                // set compression
                ImageWriter writer =
                        ImageIO.getImageWritersByMIMEType(ContentType.getContentType(contentType).getMimeType()).next();
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
        } catch (final CMMException ex) {
            throw new UnableToProcessTheImage();
        }

    }
}
