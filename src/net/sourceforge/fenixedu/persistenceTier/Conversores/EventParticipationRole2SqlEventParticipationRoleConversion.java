
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class EventParticipationRole2SqlEventParticipationRoleConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof EventParticipationRole) {
            EventParticipationRole s = (EventParticipationRole) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return EventParticipationRole.valueOf(src);
        }
        return source;        
    }
}
