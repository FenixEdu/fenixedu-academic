package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;

public class FileSetMetaData2SqlVarcharConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof FileSetMetaData) {
	    return ((FileSetMetaData) source).toXMLString();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    FileSetMetaData metadata = new FileSetMetaData();
	    metadata.fromXMLString((String) source);
	    return metadata;
	}
	return source;
    }

}
