/*
 * Created on 17/Ago/2004
 */
package ServidorPersistente.Conversores;

/**
 * @author joaosa & rmalo
 */

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.ProposalState;


public class ProposalState2EnumProposalStateFieldConversion implements FieldConversion{



    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        if (source instanceof ProposalState) {
        	ProposalState s = (ProposalState) source;
            return s.getState();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new ProposalState(src);
        }

        return source;

    }

}