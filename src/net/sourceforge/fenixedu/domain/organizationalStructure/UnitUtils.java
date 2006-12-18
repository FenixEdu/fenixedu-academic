/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.YearMonthDay;

public class UnitUtils {

    public static List<Unit> readAllExternalInstitutionUnits() {
	List<Unit> allExternalUnits = new ArrayList<Unit>();
	allExternalUnits.addAll(readExternalInstitutionUnit().getAllSubUnits());
	return allExternalUnits;
    }  

    public static Unit readExternalInstitutionUnitByName(String name) {
	for (Unit unit : readAllExternalInstitutionUnits()) {
	    if (unit.getName().equals(name)) {
		return unit;
	    }
	}
	return null;
    }

    public static List<Unit> readAllUnitsWithoutParents() {
	List<Unit> allUnitsWithoutParent = new ArrayList<Unit>();
	for (Unit unit : Unit.readAllUnits()) {
	    if (unit.getParentUnits().isEmpty()) {
		allUnitsWithoutParent.add(unit);
	    }
	}
	return allUnitsWithoutParent;
    }

    public static List<Unit> readAllUnitsByType(PartyTypeEnum type) {
    	final List<Unit> result = new ArrayList<Unit>();
    	final YearMonthDay now = new YearMonthDay();
    	for (final Unit unit : Unit.readAllUnits()) {
    	    if (unit.isActive(now) && unit.getType() == type) {
    		result.add(unit);
    	    }
    	}
    	return result;
    }
    
    public static List<Unit> readAllDepartmentUnits() {
    	return readAllUnitsByType(PartyTypeEnum.DEPARTMENT);
    }

    public static Unit readUnitWithoutParentstByAcronym(String acronym) {
	for (Unit topUnit : readAllUnitsWithoutParents()) {
	    if (topUnit.getAcronym() != null && topUnit.getAcronym().equals(acronym)) {
		return topUnit;
	    }
	}
	return null;
    }

    public static Unit readExternalInstitutionUnit() {
	return RootDomainObject.getInstance().getExternalInstitutionUnit();
    }

    public static Unit readInstitutionUnit() {
	return RootDomainObject.getInstance().getInstitutionUnit();
    }
}
