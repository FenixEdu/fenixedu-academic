package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.SituationOfGuide;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGuideSituation2SqlGuideSituationFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof SituationOfGuide) {
            SituationOfGuide s = (SituationOfGuide) source;
            return s.getSituation();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new SituationOfGuide(src);
        }
        return source;

    }

}