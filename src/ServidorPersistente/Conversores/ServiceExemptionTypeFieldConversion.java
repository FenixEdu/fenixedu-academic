/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.credits.ServiceExemptionType;

/**
 * @author jpvl
 */
public class ServiceExemptionTypeFieldConversion implements FieldConversion
{

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException
    {
        if (obj instanceof ServiceExemptionType)
        {
            ServiceExemptionType serviceExemptionType = (ServiceExemptionType) obj;
            return new Integer(serviceExemptionType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException
    {
        ServiceExemptionType serviceExemptionType = null;
        if (obj instanceof Integer)
        {
            Integer serviceExemptionId = (Integer) obj;

            serviceExemptionType = ServiceExemptionType.getEnum(serviceExemptionId.intValue());

            if (serviceExemptionType == null)
            {
                throw new IllegalArgumentException(
                    this.getClass().getName() + ": Illegal service exemption type!(" + obj + ")");
            }
        } else
        {
            throw new IllegalArgumentException("Illegal service exemption type!(" + obj + ")");
        }
        return serviceExemptionType;

    }

}
