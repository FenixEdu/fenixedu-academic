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
	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party.isUnit()) {
		Unit unit = (Unit) party;
		if (unit.getParentUnits().isEmpty()) {
		    allUnitsWithoutParent.add(unit);
		}
	    }
	}
	return allUnitsWithoutParent;
    }

    public static List<Unit> readAllInternalActiveUnitsThatCanBeResponsibleOfSpaces(){
	List<Unit> result = new ArrayList<Unit>();
	final YearMonthDay now = new YearMonthDay();
	Unit institutionUnit = readInstitutionUnit();
	if(institutionUnit != null) {
	    if (institutionUnit.getCanBeResponsibleOfSpaces() && institutionUnit.isActive(now)) {
		result.add(institutionUnit);		    
	    }
	    for (Unit subUnit : institutionUnit.getSubUnits()) {
		if (subUnit.getCanBeResponsibleOfSpaces() && subUnit.isActive(now)) {
		    result.add(subUnit);		    
		}
		readAllInternalActiveSubUnitsThatCanBeResponsibleOfSpaces(result, subUnit, now);
	    }
	}
	return result;
    }

    private static void readAllInternalActiveSubUnitsThatCanBeResponsibleOfSpaces(List<Unit> result, Unit subUnit, YearMonthDay now) {
	for (Unit unit : subUnit.getSubUnits()) {
	    if (unit.getCanBeResponsibleOfSpaces() && unit.isActive(now)) {
		result.add(unit);		    
	    }
	    readAllInternalActiveSubUnitsThatCanBeResponsibleOfSpaces(result, unit, now);
	}
    }

    public static List<Unit> readAllActiveUnitsByType(PartyTypeEnum type) {
	final List<Unit> result = new ArrayList<Unit>();
	final YearMonthDay now = new YearMonthDay();	
	PartyType partyType = PartyType.readPartyTypeByType(type);
	if(partyType != null) {
	    List<Party> parties = partyType.getParties();
	    for (Party party : parties) {
		if (party.isUnit()) {
		    Unit unit = (Unit) party;
		    if (unit.isActive(now)) {
			result.add(unit);
		    }
		}
	    }
	}	
	return result;
    }

    public static List<Unit> readAllActiveUnitsByClassification(UnitClassification unitClassification) {
	final List<Unit> result = new ArrayList<Unit>();
	final YearMonthDay now = new YearMonthDay();		
	if(unitClassification != null) {
	    for (Party party : RootDomainObject.getInstance().getPartys()) {
		if (party.isUnit()) {
		    Unit unit = (Unit) party;
		    if(unit.getClassification() != null && unit.getClassification().equals(unitClassification)
			    && unit.isActive(now)) {
			result.add(unit);
		    }
		}
	    }
	}	
	return result;
    }

    public static List<DepartmentUnit> readAllDepartmentUnits() {
	List<DepartmentUnit> result = new ArrayList<DepartmentUnit>();
	List<Unit> readAllActiveUnitsByType = readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
	for (Unit unit : readAllActiveUnitsByType) {
	    result.add((DepartmentUnit) unit);
	}
	return result;
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
	    if (types.contains(unit.getType()) && unit.getName().toLowerCase().matches(nameToSearch)
		    && !unit.isInternal()) {
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
	if (parentUnits.size() == 1) {
	    final List<Unit> result = new ArrayList<Unit>();
	    result.add(unit);
	    result.addAll(0, getUnitFullPath(parentUnits.iterator().next(), validAccountabilityTypes));
	    return result;
	}
	throw new DomainException("error.unitUtils.unit.full.path.has.more.than.one.parent");
    }

    public static StringBuilder getUnitFullPathName(final Unit unit, final List<AccountabilityTypeEnum> validAccountabilityTypes) {
	if (unit == readEarthUnit()) {
	    return new StringBuilder(0);
	}
	final Collection<Unit> parentUnits = unit.getParentUnits(validAccountabilityTypes);
	if (parentUnits.isEmpty()) {
	    return new StringBuilder(unit.getName());
	}
	if (parentUnits.size() == 1) {
	    final StringBuilder builder = new StringBuilder();
	    builder.append(parentUnits.iterator().next() == readEarthUnit() ? "" : " > ").append(unit.getName());
	    builder.insert(0, getUnitFullPathName(parentUnits.iterator().next(), validAccountabilityTypes));
	    return builder;
	}
	throw new DomainException("error.unitUtils.unit.full.path.has.more.than.one.parent");
    }

    public static List<Unit> readExternalUnitsByNameAndTypesStartingAtEarth(final String unitName, final List<PartyTypeEnum> types) {
	if (unitName == null) {
	    return Collections.emptyList();
	}
	final String nameToSearch = unitName.replaceAll("%", ".*").toLowerCase();
	final List<Unit> result = new ArrayList<Unit>();
	for (final UnitName name : RootDomainObject.getInstance().getUnitNameSet()) {
	    if (name.getName().toLowerCase().matches(nameToSearch) && name.getIsExternalUnit() && types.contains(name.getUnit().getType())) {
		result.add(name.getUnit());
	    }
	}
	return result;
    }

    public static Collection<Unit> readAllUnitsWithClassification(UnitClassification classification) {
	List<Unit> result = new ArrayList<Unit>();

	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party.isUnit()) {
		Unit unit = (Unit) party;

		UnitClassification unitClassification = unit.getClassification();
		if (unitClassification != null && unitClassification.equals(classification)) {
		    result.add(unit);
		}
	    }
	}

	return result;
    }
}
