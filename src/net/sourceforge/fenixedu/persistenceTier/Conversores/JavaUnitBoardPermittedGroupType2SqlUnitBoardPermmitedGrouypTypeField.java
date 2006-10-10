/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,11:57:54 AM
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br><br>
 * Created on Jun 27, 2006,11:57:54 AM
 *
 */
public class JavaUnitBoardPermittedGroupType2SqlUnitBoardPermmitedGrouypTypeField implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof UnitBoardPermittedGroupType) {
            final UnitBoardPermittedGroupType permittedGroupType = (UnitBoardPermittedGroupType) source;
            return permittedGroupType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            final String unitBoardPermittedGroupTypeString = (String) source;
            return UnitBoardPermittedGroupType.valueOf(unitBoardPermittedGroupTypeString);
        }
        return source;
    }

}
