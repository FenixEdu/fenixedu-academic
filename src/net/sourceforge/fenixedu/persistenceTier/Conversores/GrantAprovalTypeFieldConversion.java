/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.AprovalType;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author pica
 * @author barbosa
 */
public class GrantAprovalTypeFieldConversion implements FieldConversion {
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof AprovalType) {
            AprovalType aprovalType = (AprovalType) obj;
            return new Integer(aprovalType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        AprovalType aprovalType = null;

        if (obj instanceof Integer) {
            Integer aprovalTypeId = (Integer) obj;
            aprovalType = AprovalType.getEnum(aprovalTypeId.intValue());

            if (aprovalType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal aproval type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal aproval type!(" + obj + ")");
        }
        return aprovalType;
    }

}