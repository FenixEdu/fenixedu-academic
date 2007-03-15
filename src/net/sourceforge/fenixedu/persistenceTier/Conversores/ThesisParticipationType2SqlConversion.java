package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ThesisParticipationType2SqlConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((ThesisParticipationType) source).name();
        }
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null && source.equals("")) {
            return null;
        }
        else {
            return ThesisParticipationType.valueOf((String) source);
        }
    }

}
