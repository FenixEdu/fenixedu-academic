package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.tests.NewPermission;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Permission2SqlPermissionConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof NewPermission) {
            return ((NewPermission) source).name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null || source.equals("")) {
            return null;
        } else if (source instanceof String) {
            return NewPermission.valueOf((String) source);
        }
        return source;
    }
}
