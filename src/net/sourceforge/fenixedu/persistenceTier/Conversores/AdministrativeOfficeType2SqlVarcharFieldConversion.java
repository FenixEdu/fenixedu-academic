/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AdministrativeOfficeType2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof AdministrativeOfficeType) ? ((AdministrativeOfficeType) source).name()
                : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? AdministrativeOfficeType.valueOf((String) source) : null;
    }

}
