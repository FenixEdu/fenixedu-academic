/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class UnitUtils {

    public static final String IST_UNIT_NAME = "Instituto Superior Técnico (IST)";
    public static final String EXTERNAL_INSTITUTION_UNIT_NAME = "Instituições Externas";          
    
    public static List<Unit> readAllExternalInstitutionUnits() throws ExcepcaoPersistencia {        
        List<Unit> allUnits = RootDomainObject.readAllUnits(); 
        List<Unit> allExternalUnits = new ArrayList<Unit>();

        for (Unit unit : allUnits) {
            if (unit.getType() != null && unit.getType().equals(PartyType.EXTERNAL_INSTITUTION)) {
                allExternalUnits.add(unit);
            }
        }
        return allExternalUnits;
    }

    public static Unit readExternalInstitutionUnitByName(String name) throws ExcepcaoPersistencia{
        List<Unit> allUnits = RootDomainObject.readAllUnits();        
        Unit externalUnit = null;
        
        for (Unit unit : allUnits) {
            if (unit.getType() != null && unit.getType().equals(PartyType.EXTERNAL_INSTITUTION)
                    && unit.getName().equals(name)) {
                externalUnit = unit;
            }
        }
        
        return externalUnit;
    }
        
    public static Unit readUnitWithoutParentstByName(String name) throws ExcepcaoPersistencia{        
        List<Unit> allUnitsWithoutParent = readAllUnitsWithoutParents();
        for (Unit unit : allUnitsWithoutParent) {
            if(unit.getName().equals(name)){
                return unit;
            }
        }
        return null;
    }           
    
    public static List<Unit> readAllUnitsWithoutParents() throws ExcepcaoPersistencia {
        List<Unit> allUnits = RootDomainObject.readAllUnits();        
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
        for (final Unit unit : RootDomainObject.readAllUnits()) {
            if (unit.isActive(now) && unit.getType() == PartyType.DEPARTMENT) {
                result.add(unit);
            }
        }
        return result;
    }
}
