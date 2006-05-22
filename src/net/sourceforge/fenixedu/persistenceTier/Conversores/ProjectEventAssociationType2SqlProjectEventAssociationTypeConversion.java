package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.ProjectParticipationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ProjectEventAssociationType2SqlProjectEventAssociationTypeConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ProjectParticipationType) {
            ProjectEventAssociationRole s = (ProjectEventAssociationRole) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ProjectEventAssociationRole.valueOf(src);
        }
        return source;        
    }
}
