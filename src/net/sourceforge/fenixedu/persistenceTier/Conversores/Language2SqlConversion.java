package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.Language;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Language2SqlConversion implements FieldConversion {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((Language) source).name();
        }
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return Language.valueOf((String) source);
        }
    }

}
