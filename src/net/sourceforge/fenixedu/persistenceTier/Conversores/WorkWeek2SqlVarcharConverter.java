package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumSet;

import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.codec.binary.Base64;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class WorkWeek2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof WorkWeek) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
		ObjectOutputStream dos = new ObjectOutputStream(baos);
		dos.writeObject(((WorkWeek) source).getDays());
	    } catch (IOException e) {
		return null;
	    }
	    return new String(Base64.encodeBase64(baos.toByteArray()));
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    byte[] workWeekByteArray = Base64.decodeBase64(((String) source).getBytes());

	    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(workWeekByteArray);
	    try {
		ObjectInputStream stream = new ObjectInputStream(byteInputStream);
		WorkWeek workWeek = new WorkWeek((EnumSet<WeekDay>) stream.readObject());
		return workWeek;
	    } catch (IOException e) {
		return null;
	    } catch (ClassNotFoundException e) {
		return null;
	    }
	}
	return source;
    }

}
