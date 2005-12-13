/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.LegalRegimenType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class LegalRegimenType2SqlLegalRegimenTypeConversion implements FieldConversion  {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof LegalRegimenType) {
            LegalRegimenType s = (LegalRegimenType) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return LegalRegimenType.valueOf(src);
        }
        return source;
    }

}
