/*
 * Created on Apr 04, 2004
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Luis Cruz
 */
public class JavaFinalDegreeWorkProposalStatus2SqlFinalDegreeWorkProposalStatusFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof FinalDegreeWorkProposalStatus) {
            FinalDegreeWorkProposalStatus status = (FinalDegreeWorkProposalStatus) source;
            return status.getStatus();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new FinalDegreeWorkProposalStatus(src);
        }

        return source;

    }

}