/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
    
    public static Unit readEarthUnit() {
	return RootDomainObject.getInstance().getEarthUnit();
    }

    public static Set<Unit> readExternalUnitsByNameAndTypes(final String unitName, List<PartyTypeEnum> types) {
	if (unitName == null) {
	    return Collections.emptySet();
	}
	
	final String nameToSearch = unitName.replaceAll("%", ".*").toLowerCase();
	final Set<Unit> result = new HashSet<Unit>();
	for (final Unit unit : Unit.readAllUnits()) {
	    if (types.contains(unit.getType()) && unit.getName().toLowerCase().matches(nameToSearch) && !unit.isInternal()) {
		result.add(unit);
	    }
	}
	return result;		
    }
    
    public static List<Unit> getUnitFullPath(final Unit unit, final List<AccountabilityTypeEnum> validAccountabilityTypes) {
	final Collection<Unit> parentUnits = unit.getParentUnits(validAccountabilityTypes);
	if (parentUnits.isEmpty()) {
	    return Collections.emptyList();
	}
	if (parentUnits.size() == 1){
	    final List<Unit> result = new ArrayList<Unit>();
	    result.add(unit);
	    result.addAll(0, getUnitFullPath(parentUnits.iterator().next(), validAccountabilityTypes));
	    return result;
	} 
	throw new DomainException("error.unitUtils.unit.full.path.has.more.than.one.parent");
    }
    
    public static List<Unit> readExternalUnitsByNameAndTypesStartingAtEarth(final String unitName, List<PartyTypeEnum> types) {
	
	if (unitName == null) {
	    return Collections.emptyList();
	}
	
	final String nameToSearch = unitName.replaceAll("%", ".*").toLowerCase();
	return readExternalUnitsByNameAndTypesStartingEarth(nameToSearch, types, readEarthUnit());
    }

    private static List<Unit> readExternalUnitsByNameAndTypesStartingEarth(String unitName, List<PartyTypeEnum> types, Unit unit) {
	final List<Unit> result = new ArrayList<Unit>();
	for (final Unit each : unit.getSubUnits()) {
	    checkUnit(unitName, types, result, each);
	    result.addAll(readExternalUnitsByNameAndTypesStartingEarth(unitName, types, each));
	}
	return result;
    }

    private static void checkUnit(String unitName, List<PartyTypeEnum> types, final List<Unit> result, final Unit unit) {
	if (types.contains(unit.getType()) && unit.getName().toLowerCase().matches(unitName) && !unit.isInternal()) {
	    result.add(unit);
	}
    }
}
