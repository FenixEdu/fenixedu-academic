package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.Object2ByteArrFieldConversion;

public class Object2ByteArrayConverter extends Object2ByteArrFieldConversion {

    public Object javaToSql(Object source) {
	return (source == null) ? null : super.javaToSql(source);
    }
}
