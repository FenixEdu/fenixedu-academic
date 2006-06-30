package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResultUnitAssociationRole2SqlResultUnitAssociationRoleConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ResultUnitAssociationRole) {
            ResultUnitAssociationRole s = (ResultUnitAssociationRole) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ResultUnitAssociationRole.valueOf(src);
        }
        return source;        
    }
}