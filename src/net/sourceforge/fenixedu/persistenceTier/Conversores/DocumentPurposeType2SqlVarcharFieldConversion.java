/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class DocumentPurposeType2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof DocumentPurposeType) ? ((DocumentPurposeType) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? DocumentPurposeType.valueOf((String) source) : null;
    }

}
