package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.CreditLineType;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Fernanda Quitério 28/Nov/2003
 *  
 */
public class CreditLineTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof CreditLineType) {
            CreditLineType creditLineType = (CreditLineType) obj;
            return new Integer(creditLineType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        CreditLineType creditLineType = null;
        if (obj instanceof Integer) {
            Integer creditLineTypeId = (Integer) obj;

            creditLineType = CreditLineType.getEnum(creditLineTypeId.intValue());

            if (creditLineType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal credit line type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal credit line type!(" + obj + ")");
        }
        return creditLineType;
    }
}