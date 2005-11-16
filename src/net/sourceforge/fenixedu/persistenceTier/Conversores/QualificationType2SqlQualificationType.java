/*
 * Created on Nov 14, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.QualificationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class QualificationType2SqlQualificationType implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        
        if(source == null || source.equals("")){
            return null;
        }
        else if (source instanceof QualificationType) {
            QualificationType s = (QualificationType) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        
        if(source == null || source.equals("")){
            return null;
        }            
        else if (source instanceof String) {            
            String src = (String) source;            
            return QualificationType.valueOf(src);
        }
        return source;
    }
}
