package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResultEventAssociationRole2SqlResultEventAssociationRoleConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ResultEventAssociationRole) {
            ResultEventAssociationRole s = (ResultEventAssociationRole) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ResultEventAssociationRole.valueOf(src);
        }
        return source;        
    }
}
