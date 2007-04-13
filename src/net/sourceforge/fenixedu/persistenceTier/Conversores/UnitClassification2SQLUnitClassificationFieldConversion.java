/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class UnitClassification2SQLUnitClassificationFieldConversion implements FieldConversion {
    
    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof UnitClassification) {
            UnitClassification s = (UnitClassification) source;
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
            return UnitClassification.valueOf(src);
        }
        return source;
    }
}
