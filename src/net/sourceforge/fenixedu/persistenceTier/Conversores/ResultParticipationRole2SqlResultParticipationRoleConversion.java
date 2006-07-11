package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResultParticipationRole2SqlResultParticipationRoleConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof ResultParticipationRole) {
            ResultParticipationRole s = (ResultParticipationRole) source;
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
            return ResultParticipationRole.valueOf(src);
        }
        return source;
    }
}
