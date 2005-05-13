package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.GraduationType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGraduationType2SqlGraduationTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GraduationType) {
            GraduationType s = (GraduationType) source;
            return s.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return GraduationType.valueOf(src);
        }
        return source;

    }

}