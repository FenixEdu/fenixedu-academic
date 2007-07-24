package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.YearMonthList;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class YearMonthList2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof YearMonthList) {
	    YearMonthList yearMonthList = (YearMonthList) source;
	    return yearMonthList.exportAsString();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	return source instanceof String ? YearMonthList.importFromString((String) source) : source;
    }

}
