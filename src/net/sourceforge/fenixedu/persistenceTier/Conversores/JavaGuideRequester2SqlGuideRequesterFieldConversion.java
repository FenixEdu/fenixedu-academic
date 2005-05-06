package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGuideRequester2SqlGuideRequesterFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GuideRequester) {
            GuideRequester s = (GuideRequester) source;
            return s.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return GuideRequester.valueOf(src);
        }
        return source;

    }

}