/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class DocumentRequestType2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof DocumentRequestType) ? ((DocumentRequestType) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? DocumentRequestType.valueOf((String) source) : null;
    }

}
