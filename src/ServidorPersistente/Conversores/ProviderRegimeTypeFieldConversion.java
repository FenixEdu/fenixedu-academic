/*
 * Created on 16/Nov/2003
 *  
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ProviderRegimeTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof ProviderRegimeType) {
            ProviderRegimeType providerRegimeType = (ProviderRegimeType) obj;
            return new Integer(providerRegimeType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        ProviderRegimeType providerRegimeType = null;
        if (obj instanceof Integer) {
            Integer providerRegimeTypeId = (Integer) obj;

            providerRegimeType = ProviderRegimeType.getEnum(providerRegimeTypeId.intValue());

            if (providerRegimeType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal provider regime type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal provider regime type!(" + obj + ")");
        }
        return providerRegimeType;
    }
}