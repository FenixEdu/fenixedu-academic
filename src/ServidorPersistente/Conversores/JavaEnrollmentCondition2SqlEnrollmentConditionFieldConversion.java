package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.enrollment.EnrollmentCondition;

/**
 * @author David Santos in Jun 15, 2004
 */

public class JavaEnrollmentCondition2SqlEnrollmentConditionFieldConversion implements FieldConversion
{
	
    public Object javaToSql(Object source)
    {
        if (source instanceof EnrollmentCondition)
        {
            EnrollmentCondition condition = (EnrollmentCondition) source;
            return condition.getName();
        } else
        {
            return source;
        }
    }

    public Object sqlToJava(Object source)
    {
        if (source instanceof String)
        {
            String src = (String) source;
            return EnrollmentCondition.getEnum(src);
        } else
        {
            return source;
        }
    }
    
    
    
   
}