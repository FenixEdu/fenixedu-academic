/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

/**
 * @author joaosa & rmalo
 */

import net.sourceforge.fenixedu.util.ProposalState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;


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