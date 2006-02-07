/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class UnitUtils {

    public static final String IST_UNIT_NAME = "Instituto Superior Técnico (IST)";
    public static final String EXTERNAL_INSTITUTION_UNIT_NAME = "Instituições Externas";
    
    private static List<Unit> readAllUnits() throws ExcepcaoPersistencia{
        IPersistentObject persistentObject = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentObject();
        List<Unit> allUnits = (List<Unit>) persistentObject.readAll(Unit.class);
        return allUnits;
    }
    
    public static List<Unit> readAllExternalInstitutionUnits() throws ExcepcaoPersistencia {        
        List<Unit> allUnits = readAllUnits(); 
        List<Unit> allExternalUnits = new ArrayList<Unit>();

        for (Unit unit : allUnits) {
            if (unit.getType() != null && unit.getType().equals(UnitType.EXTERNAL_INSTITUTION)) {
                allExternalUnits.add(unit);
            }
        }
        return allExternalUnits;
    }

    public static Unit readExternalInstitutionUnitByName(String name) throws ExcepcaoPersistencia{
        List<Unit> allUnits = readAllUnits();        
        Unit externalUnit = null;
        
        for (Unit unit : allUnits) {
            if (unit.getType() != null && unit.getType().equals(UnitType.EXTERNAL_INSTITUTION)
                    && unit.getName().equals(name)) {
                externalUnit = unit;
            }
        }
        
        return externalUnit;
    }
    
    public static List<Unit> readAllUnitsWithoutParent() throws ExcepcaoPersistencia {
        List<Unit> allUnits = readAllUnits();        
        List<Unit> allUnitsWithoutParent = new ArrayList<Unit>();
        
        for (Unit unit : allUnits) {
            if (unit.getParentUnits().isEmpty()) {
                allUnitsWithoutParent.add(unit);
            }
        }
        return allUnitsWithoutParent;
    }
}
