package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.GraduationType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGraduationType2SqlGraduationTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GraduationType) {
            GraduationType s = (GraduationType) source;
            return s.getType();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new GraduationType(src);
        }
        return source;

    }

}