package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public abstract class AbstractUnitAcronymPathProcessor extends AbstractPathProcessor {

    private List<Unit> findUnitsInPath(String[] possibleUnits) {
	List<Unit> units = new ArrayList<Unit>();
	String unitName = possibleUnits[0];

	Unit currentUnit = findCorrectUnit(Unit.readUnitsByAcronym(unitName, true));

	if (currentUnit == null && unitName.contains("-")) {
	    currentUnit = findCorrectUnit(Unit.readUnitsByAcronym(unitName.replace('-', ' '), true));
	}

	if (currentUnit != null && isDescendantOfBaseUnit(currentUnit)) {
	    units.add(currentUnit);
	    for (int i = 1; i < possibleUnits.length; i++) {
		for (Unit subUnit : currentUnit.getSubUnits()) {
		    if (possibleUnits[i].equalsIgnoreCase(Content.normalize(subUnit.getAcronym()))) {
			currentUnit = subUnit;
			units.add(subUnit);
		    }
		}
	    }
	}
	return units;
    }

    private boolean isDescendantOfBaseUnit(Unit unit) {
	Unit baseUnit = getBaseUnit();
	if (unit.getParentUnits().size() > 1) {
	    throw new DomainException("error.unit.has.more.than.one.parent");
	}
	for (Unit parentUnit : unit.getParentUnits()) {
	    if (parentUnit == baseUnit) {
		return true;
	    } else {
		if (parentUnit.isAggregateUnit()) {
		    return isDescendantOfBaseUnit(parentUnit);
		}
	    }
	}
	return false;
    }

    private Unit findCorrectUnit(final List<Unit> units) {
	final Class[] types = getAcceptableTypes();
	Unit unitOfGivinType = null;
	for (final Unit unit : units) {
	    for (final Class clazz : types) {
		if (clazz.equals(unit.getClass())) {
		    if (unitOfGivinType == null) {
			unitOfGivinType = unit;
		    } else {
			return null;
		    }
		}
	    }
	}
	return unitOfGivinType;
    }

    @Override
    public Content processPath(String path) {
	String[] possibleUnits = path.split("/");
	List<Unit> unitsList = findUnitsInPath(possibleUnits);
	return unitsList.isEmpty() ? null : unitsList.get(unitsList.size() - 1).getSite();

    }

    @Override
    public String getTrailingPath(String path) {
	final String[] possibleUnits = path.split("/");
	final List<Unit> unitsList = findUnitsInPath(possibleUnits);
	final StringBuilder buffer = new StringBuilder();
	for (final Unit unit : unitsList) {
	    buffer.append(unit.getAcronym());
	    buffer.append("/");
	}

	return path.length() < buffer.length() ? "" : path.substring(buffer.length());
    }

    protected Unit getBaseUnit() {
	return RootDomainObject.getInstance().getInstitutionUnit();
    }

    protected abstract Class[] getAcceptableTypes();
}
