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

    private final byte[] bytes;

    public ByteArray(byte[] value) {
        this.bytes = value;
    }

    public ByteArray(final InputStream stream) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        FileUtils.copy(stream, output);
        this.bytes = output.toByteArray();
    }

    public byte[] getBytes() {
        return bytes == null ? null : bytes.clone();
    }

    public static byte[] toBytes(final InputStream stream) throws IOException {
        final ByteArray byteArray = new ByteArray(stream);
        return byteArray.getBytes();
    }

}
