/*
 * Created on Jan 26, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.project.ProjectType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ProjectType2SqlProjectTypeConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ProjectType) {
            ProjectType s = (ProjectType) source;
            return s.name();
        }
        return source;       
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ProjectType.valueOf(src);
        }
        return source;        
    }
}
