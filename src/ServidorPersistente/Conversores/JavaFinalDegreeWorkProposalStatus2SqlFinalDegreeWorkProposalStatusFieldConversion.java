/*
 * Created on Apr 04, 2004
 * 
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.FinalDegreeWorkProposalStatus;
import Util.TipoCurso;

/**
 * 
 * @author Luis Cruz
 */
public class JavaFinalDegreeWorkProposalStatus2SqlFinalDegreeWorkProposalStatusFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof FinalDegreeWorkProposalStatus)
        {
        	FinalDegreeWorkProposalStatus status = (FinalDegreeWorkProposalStatus) source;
            return status.getStatus();
        }
        else {
            return source;
        }
    }    

   /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source)
    {
        if (source instanceof Integer)
        {
            Integer src = (Integer) source;
            return new FinalDegreeWorkProposalStatus(src);
        }
        else
        {
            return source;
        }
    }

}