/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AcademicServiceRequestSituationType2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof AcademicServiceRequestSituationType) ? ((AcademicServiceRequestSituationType) source)
                .name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? AcademicServiceRequestSituationType.valueOf((String) source) : null;
    }

}
