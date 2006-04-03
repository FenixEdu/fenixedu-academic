/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class UnitUtils {

    public static final String IST_UNIT_NAME = getLabel("ist.unit.name");

    public static final String EXTERNAL_INSTITUTION_UNIT_NAME = getLabel("external.instituions.name");

    public static List<Unit> readAllExternalInstitutionUnits() throws ExcepcaoPersistencia {
        List<Unit> allUnits = Party.readAllUnits();
        List<Unit> allExternalUnits = new ArrayList<Unit>();

        for (Unit unit : allUnits) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.EXTERNAL_INSTITUTION)) {
                allExternalUnits.add(unit);
            }
        }
        return allExternalUnits;
    }

    public static Unit readExternalInstitutionUnitByName(String name) throws ExcepcaoPersistencia {
        List<Unit> allUnits = Party.readAllUnits();
        Unit externalUnit = null;

        for (Unit unit : allUnits) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.EXTERNAL_INSTITUTION)
                    && unit.getName().equals(name)) {
                externalUnit = unit;
            }
        }

        return externalUnit;
    }

    public static Unit readUnitWithoutParentstByName(String name) throws ExcepcaoPersistencia {
        List<Unit> allUnitsWithoutParent = readAllUnitsWithoutParents();
        for (Unit unit : allUnitsWithoutParent) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }
        return null;
    }

    public static List<Unit> readAllUnitsWithoutParents() throws ExcepcaoPersistencia {
        List<Unit> allUnits = Party.readAllUnits();
        List<Unit> allUnitsWithoutParent = new ArrayList<Unit>();

        for (Unit unit : allUnits) {
            if (unit.getParentUnits().isEmpty()) {
                allUnitsWithoutParent.add(unit);
            }
        }
        return allUnitsWithoutParent;
    }

    public static List<Unit> readAllDepartmentUnits() {
        final List<Unit> result = new ArrayList<Unit>();
        final Date now = Calendar.getInstance().getTime();
        for (final Unit unit : Party.readAllUnits()) {
            if (unit.isActive(now) && unit.getType() == PartyTypeEnum.DEPARTMENT) {
                result.add(unit);
            }
        }
        return result;
    }    

    private static String getLabel(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources");
        return bundle.getString(key);
    }
}
