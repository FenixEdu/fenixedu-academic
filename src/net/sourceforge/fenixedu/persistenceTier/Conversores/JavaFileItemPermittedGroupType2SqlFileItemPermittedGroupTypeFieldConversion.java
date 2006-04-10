package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaFileItemPermittedGroupType2SqlFileItemPermittedGroupTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof FileItemPermittedGroupType) {
            final FileItemPermittedGroupType permittedGroupType = (FileItemPermittedGroupType) source;
			return permittedGroupType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            final String fileItemPermittedGroupTypeString = (String) source;
            return FileItemPermittedGroupType.valueOf(fileItemPermittedGroupTypeString);
        }
        return source;
    }

}