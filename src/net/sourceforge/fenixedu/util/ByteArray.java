package net.sourceforge.fenixedu.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ByteArray {
    private byte[] bytes;

    public ByteArray(byte[] value) {
	this.bytes = value;
    }

    public ByteArray(InputStream stream) throws IOException {
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	FileUtils.copy(stream, output);
	this.bytes = output.toByteArray();
    }

    public byte[] getBytes() {
	return bytes;
    }

    public void setBytes(byte[] bytes) {
	this.bytes = bytes;
    }
}
