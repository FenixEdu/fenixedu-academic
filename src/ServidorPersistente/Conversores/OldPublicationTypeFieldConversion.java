/*
 * Created on 16/Nov/2003
 *  
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OldPublicationTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof OldPublicationType) {
            OldPublicationType oldPublicationType = (OldPublicationType) obj;
            return new Integer(oldPublicationType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        OldPublicationType oldPublicationType = null;
        if (obj instanceof Integer) {
            Integer oldPublicationTypeId = (Integer) obj;

            oldPublicationType = OldPublicationType.getEnum(oldPublicationTypeId.intValue());

            if (oldPublicationType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal provider regime type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal provider regime type!(" + obj + ")");
        }
        return oldPublicationType;
    }
}