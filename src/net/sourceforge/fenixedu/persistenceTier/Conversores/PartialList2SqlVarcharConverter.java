package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.PartialList;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.Partial;

public class PartialList2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof PartialList) {
	    final StringBuilder result = new StringBuilder();
	    PartialList partialList = (PartialList) source;
	    for (Partial partial : partialList.getPartials()) {
		if (result.length() != 0) {
		    result.append(";");
		}
		JavaPartial2SqlStringFieldConversion converter = new JavaPartial2SqlStringFieldConversion();
		String partialString = (String) converter.javaToSql(partial);
		result.append(partialString);
	    }
	    return result;
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    PartialList result = new PartialList();
	    for (final String partialString : ((String) source).split(";")) {
		JavaPartial2SqlStringFieldConversion converter = new JavaPartial2SqlStringFieldConversion();
		Partial newPartial = (Partial) converter.sqlToJava(partialString);
		result.getPartials().add(newPartial);
	    }
	    return result;
	}
	return source;
    }

}
