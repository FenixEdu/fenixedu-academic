package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent.ResultPatentType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResultPatentType2SqlResultPatentTypeConversion implements FieldConversion{
    
        public Object javaToSql(Object source) throws ConversionException {
            if (source instanceof ResultPatentType) {
                ResultPatentType s = (ResultPatentType) source;
                return s.name();
            }
            return source;       
        }

        public Object sqlToJava(Object source) throws ConversionException {
            if (source instanceof String) {            
                String src = (String) source;            
                return ResultPatentType.valueOf(src);
            }
            return source;        
        }
}
