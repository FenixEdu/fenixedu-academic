package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 *  
 */

public class JavaReimbursementGuideState2SqlReimbursementGuideStateFieldConversion implements
        FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof ReimbursementGuideState) {
            ReimbursementGuideState state = (ReimbursementGuideState) source;
            return state.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return ReimbursementGuideState.valueOf(src);
        }
        return source;
    }

}