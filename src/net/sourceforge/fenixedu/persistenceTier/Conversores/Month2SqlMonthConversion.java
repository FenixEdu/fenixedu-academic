package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.Month;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Month2SqlMonthConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof Month) {
            Month s = (Month) source;
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
            return Month.valueOf(src);
        }
        return source;
    }
}
