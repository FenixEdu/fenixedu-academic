package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.GuideRequester;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGuideRequester2SqlGuideRequesterFieldConversion implements
        FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GuideRequester) {
            GuideRequester s = (GuideRequester) source;
            return s.getType();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new GuideRequester(src);
        }
        return source;

    }

}