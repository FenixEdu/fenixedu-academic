/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 30, 2006,6:05:46 PM
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.ExecutionCourseBoardPermittedGroupType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 30, 2006,6:05:46 PM
 * 
 */
public class JavaExecutionCourseBoardPermittedGroupType2SqlExecutionCourseBoardPermmitedGrouypTypeField
        implements FieldConversion {
    public Object javaToSql(Object source) {
        if (source instanceof ExecutionCourseBoardPermittedGroupType) {
            final ExecutionCourseBoardPermittedGroupType permittedGroupType = (ExecutionCourseBoardPermittedGroupType) source;
            return permittedGroupType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            final String executionCourseBoardPermittedGroupTypeString = (String) source;
            return ExecutionCourseBoardPermittedGroupType
                    .valueOf(executionCourseBoardPermittedGroupTypeString);
        }
        return source;
    }
}
