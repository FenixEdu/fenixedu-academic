package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import pt.utl.ist.fenix.tools.file.VirtualPath;

public class VirtualPath2SqlVarcharConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof VirtualPath) {
	    return ((VirtualPath) source).toXMLString();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    VirtualPath path = new VirtualPath();
	    path.fromXMLString((String) source);
	    return path;
	}
	return source;
    }

}
