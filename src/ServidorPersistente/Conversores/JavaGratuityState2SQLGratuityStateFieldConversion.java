/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.GratuityState;

public class JavaGratuityState2SQLGratuityStateFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof GratuityState) {
            GratuityState s = (GratuityState) source;
            return new Integer(s.getValue());
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        GratuityState gratuityState = null;
        if (source instanceof Integer) {
            Integer gratuityStateID = (Integer) source;

            gratuityState = GratuityState.getEnum(gratuityStateID.intValue());
            if (gratuityState == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal Gratuity State!(" + source + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal Gratuity State!(" + source + ")");
        }
        return gratuityState;
    }
}

