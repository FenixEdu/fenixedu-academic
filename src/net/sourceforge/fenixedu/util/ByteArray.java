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

    public ByteArray(InputStream stream) throws IOException {
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	FileUtils.copy(stream, output);
	this.bytes = output.toByteArray();
    }

    /*
     * This class is used as a value-type, which means that it should
     * be immutable.  Yet, this method returns the byte[] contained
     * within this object, therefore allowing that a mis-behaving
     * caller may change it.  Probably, it should be changed to return
     * a copy of the byte[], but that may change the semantics of the
     * program (even if that semantics is erroneously depending on
     * mutating the byte[]).  So, it will stay like this for now.
     */
    public byte[] getBytes() {
	return bytes;
    }
}
