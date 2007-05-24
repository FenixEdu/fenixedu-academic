/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.ProfessionalSituationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ProfessionalSituationType2SqlProfessionalSituationTypeConversion implements FieldConversion  {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ProfessionalSituationType) {
            ProfessionalSituationType s = (ProfessionalSituationType) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ProfessionalSituationType.valueOf(src);
        }
        return source;
    }

}
