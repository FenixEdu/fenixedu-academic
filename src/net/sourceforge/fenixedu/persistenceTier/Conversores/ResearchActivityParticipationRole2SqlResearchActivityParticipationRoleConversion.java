
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResearchActivityParticipationRole2SqlResearchActivityParticipationRoleConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ResearchActivityParticipationRole) {
        	ResearchActivityParticipationRole s = (ResearchActivityParticipationRole) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ResearchActivityParticipationRole.valueOf(src);
        }
        return source;        
    }
}
