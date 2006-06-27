package net.sourceforge.fenixedu.persistenceTier.Conversores;


import net.sourceforge.fenixedu.domain.research.result.publication.ThesisType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ThesisType2SqlThesisTypeConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof ThesisType) {
            ThesisType s = (ThesisType) source;
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
            return ThesisType.valueOf(src);
        }
        return source;
    }
}
