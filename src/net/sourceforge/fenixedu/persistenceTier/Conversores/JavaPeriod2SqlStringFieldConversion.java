package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.Period;

public class JavaPeriod2SqlStringFieldConversion implements FieldConversion {

    public Object javaToSql(final Object object) {
        if (object != null && object instanceof Period) {
            final Period period = (Period) object;
	    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    try {
		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(period);
	    } catch (final IOException exception) {
		throw new ConversionException(exception);
	    }
	    return byteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public Object sqlToJava(final Object object) {
        if (object != null && object instanceof byte[]) {
            final byte[] bytes = (byte[]) object;
	    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
	    try {
		final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		return objectInputStream.readObject();
	    } catch (final IOException exception) {
		throw new ConversionException(exception);
	    } catch (final ClassNotFoundException exception) {
		throw new ConversionException(exception);
	    }
        }
        return object;
    }

}