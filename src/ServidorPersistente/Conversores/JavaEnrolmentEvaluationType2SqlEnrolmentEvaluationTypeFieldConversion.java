/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 *
 * Created on 21 de Novembro de 2002, 15:57
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrolmentEvaluationType;

public class JavaEnrolmentEvaluationType2SqlEnrolmentEvaluationTypeFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof EnrolmentEvaluationType)
        {
            EnrolmentEvaluationType s = (EnrolmentEvaluationType) source;
            return s.getType();
        }
       
            return source;
        
    }    

   /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source)
    {
        if (source instanceof Integer)
        {
            Integer src = (Integer) source;
            return new EnrolmentEvaluationType(src);
        }
       
            return source;
        
    }    
}
