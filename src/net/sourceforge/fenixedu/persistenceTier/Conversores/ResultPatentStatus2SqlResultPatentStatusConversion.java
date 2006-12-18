package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent.ResultPatentStatus;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResultPatentStatus2SqlResultPatentStatusConversion implements FieldConversion{
    
    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ResultPatentStatus) {
            ResultPatentStatus s = (ResultPatentStatus) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ResultPatentStatus.valueOf(src);
        }
        return source;        
    }

}
