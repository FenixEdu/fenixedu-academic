/*
 * JavaEspecializacao2SqlEspecializacaoFieldConversion.java
 *
 * Created on 18 de Novembro de 2002, 17:37
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.PeriodState;

public class JavaPeriodState2SqlPeriodState implements FieldConversion{

	/*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof PeriodState)
        {
            PeriodState periodState = (PeriodState) source;
            return periodState.getStateCode();
        }
        else {
            return source;
        }
    }    

   /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source){
        if (source instanceof String){
        	String code = (String) source;
			PeriodState periodState = null;
        	if (code.equals(PeriodState.ACTUAL_CODE)){
        		periodState = new PeriodState(PeriodState.ACTUAL);
        	}else if (code.equals(PeriodState.OPEN_CODE)){
        		periodState = new PeriodState(PeriodState.OPEN);
        	}else if (code.equals(PeriodState.NOT_OPEN_CODE)){
				periodState = new PeriodState(PeriodState.NOT_OPEN);
			}else if (code.equals(PeriodState.CLOSED_CODE)){
				periodState = new PeriodState(PeriodState.CLOSED);
			}
        	return periodState;
        }
        else{
            return source;
        }
    }    
}
