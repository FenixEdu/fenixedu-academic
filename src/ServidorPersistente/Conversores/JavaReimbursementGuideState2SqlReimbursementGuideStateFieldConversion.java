package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.ReimbursementGuideState;

/**
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *         
 */

public class JavaReimbursementGuideState2SqlReimbursementGuideStateFieldConversion
    implements FieldConversion
{

    public Object javaToSql(Object source)
    {
        if (source instanceof ReimbursementGuideState)
        {
            ReimbursementGuideState s = (ReimbursementGuideState) source;
            return s.getReimbursementGuideState();
        } else
        {
            return source;
        }
    }

    public Object sqlToJava(Object source)
    {
        if (source instanceof Integer)
        {
            Integer src = (Integer) source;
            return new ReimbursementGuideState(src);
        } else
        {
            return source;
        }
    }

}