/*
 * Created on 16/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class PublicationTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof PublicationType) {
            PublicationType publicationType = (PublicationType) obj;
            return new Integer(publicationType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        PublicationType publicationType = null;
        if (obj instanceof Integer) {
            Integer publicationTypeId = (Integer) obj;

            publicationType = PublicationType.getEnum(publicationTypeId.intValue());

            if (publicationType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal provider regime type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal provider regime type!(" + obj + ")");
        }
        return publicationType;
    }
}