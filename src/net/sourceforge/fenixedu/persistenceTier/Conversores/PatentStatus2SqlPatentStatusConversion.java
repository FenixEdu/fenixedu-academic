package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.PatentStatus;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class PatentStatus2SqlPatentStatusConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof PatentStatus) {
        	PatentStatus s = (PatentStatus) source;
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
            return PatentStatus.valueOf(src);
        }
        return source;
    }
}
