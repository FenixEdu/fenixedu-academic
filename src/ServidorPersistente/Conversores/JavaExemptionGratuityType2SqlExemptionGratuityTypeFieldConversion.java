/*
 * Created on 5/Jan/2004
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.ExemptionGratuityType;

/**
 * 
 * @author Tânia Pousão
 *         
 */

public class JavaExemptionGratuityType2SqlExemptionGratuityTypeFieldConversion
    implements FieldConversion
{

    public Object javaToSql(Object source)
    {
        if (source instanceof ExemptionGratuityType)
        {
        	ExemptionGratuityType exemptionGratuityType = (ExemptionGratuityType) source;
        	return new Integer(exemptionGratuityType.getValue());
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
            return ExemptionGratuityType.getEnum(src.intValue());
        } else
        {
            return source;
        }
    }

}