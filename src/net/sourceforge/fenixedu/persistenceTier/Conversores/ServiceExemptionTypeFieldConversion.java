/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.credits.ServiceExemptionType;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author jpvl
 */
public class ServiceExemptionTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof ServiceExemptionType) {
            ServiceExemptionType serviceExemptionType = (ServiceExemptionType) obj;
            return serviceExemptionType.toString();
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        ServiceExemptionType serviceExemptionType = null;
        if (obj instanceof String) {
            String serviceExemption = (String) obj;

            serviceExemptionType = ServiceExemptionType.valueOf(serviceExemption);

            if (serviceExemptionType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal service exemption type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal service exemption type!(" + obj + ")");
        }
        return serviceExemptionType;

    }

}