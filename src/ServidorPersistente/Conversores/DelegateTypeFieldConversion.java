/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.DelegateType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class DelegateTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof DelegateType) {
            DelegateType delegateType = (DelegateType) obj;
            return new Integer(delegateType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        DelegateType delegateType = null;
        if (obj instanceof Integer) {
            Integer delegateTypeId = (Integer) obj;

            delegateType = DelegateType.getEnum(delegateTypeId.intValue());

            if (delegateType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal delegate type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal delegate type!(" + obj + ")");
        }
        return delegateType;
    }
}