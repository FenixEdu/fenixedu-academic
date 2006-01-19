/*
 * Created on Oct 6, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class UnitType2SqlUnitTypeConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof UnitType) {
            UnitType s = (UnitType) source;
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
            return UnitType.valueOf(src);
        }
        return source;
    }
}
