package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.GuideState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGuideSituation2SqlGuideSituationFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GuideState) {
            GuideState s = (GuideState) source;
            return s.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return GuideState.valueOf(src);
        }
        return source;

    }

}