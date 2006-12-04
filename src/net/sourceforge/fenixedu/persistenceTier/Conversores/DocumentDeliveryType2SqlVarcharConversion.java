package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.parking.DocumentDeliveryType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class DocumentDeliveryType2SqlVarcharConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof DocumentDeliveryType) {
            final DocumentDeliveryType documentDeliveryType = (DocumentDeliveryType) obj;
            return documentDeliveryType.toString();
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        DocumentDeliveryType documentDeliveryType = null;
        if (obj instanceof String) {
            final String documentDeliveryTypeString = (String) obj;
            return DocumentDeliveryType.valueOf(documentDeliveryTypeString);
        }
        return documentDeliveryType;
    }
}
