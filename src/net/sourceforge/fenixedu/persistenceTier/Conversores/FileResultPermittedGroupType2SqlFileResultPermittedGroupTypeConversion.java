package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile.FileResultPermittedGroupType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class FileResultPermittedGroupType2SqlFileResultPermittedGroupTypeConversion implements
	FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof FileResultPermittedGroupType) {
	    FileResultPermittedGroupType s = (FileResultPermittedGroupType) source;
	    return s.name();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if(source instanceof String) {
	    String src = (String) source;            
            return FileResultPermittedGroupType.valueOf(src);
	}
	return source;
    }
}
