package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.ByteArray;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class JavaByteArray2SqlByteArrayFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof ByteArray) {
	    final ByteArray byteArray = (ByteArray) source;
	    return byteArray.getBytes();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof byte[]) {
	    return new ByteArray((byte[]) source);
	}
	return source;
    }
}