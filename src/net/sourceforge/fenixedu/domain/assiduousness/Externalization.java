package net.sourceforge.fenixedu.domain.assiduousness;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumSet;

import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.codec.binary.Base64;

public class Externalization {

    public static String externalizeWorkWeek(WorkWeek source) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	try {
	    ObjectOutputStream dos = new ObjectOutputStream(baos);
	    dos.writeObject(source.getDays());
	} catch (IOException e) {
	    return null;
	}
	return new String(Base64.encodeBase64(baos.toByteArray()));
    }

    public static WorkWeek internalizeWorkWeek(String source) {
	byte[] workWeekByteArray = Base64.decodeBase64(source.getBytes());

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
}
