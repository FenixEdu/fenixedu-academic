package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public abstract class AbstractUnitAcronymPathProcessor extends AbstractPathProcessor {

    private List<Unit> findUnitsInPath(String[] possibleUnits) {
	List<Unit> units = new ArrayList<Unit>();
	Unit currentUnit = findCorrectUnit(Unit.readUnitsByAcronym(possibleUnits[0]));

	if (currentUnit != null && isDescendantOfBaseUnit(currentUnit)) {
	    units.add(currentUnit);
	    for (int i = 1; i < possibleUnits.length; i++) {
		for (Unit subUnit : currentUnit.getSubUnits()) {
		    if (possibleUnits[i].equalsIgnoreCase(subUnit.getAcronym())) {
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

    private Unit findCorrectUnit(List<Unit> units) {
	final Class[] types = getAcceptableTypes();
	Collection unitsOfGivenType = CollectionUtils.select(units, new Predicate() {
	    public boolean evaluate(Object unit) {
		for (Class clazz : types) {
		    if (clazz.equals(unit.getClass())) {
			return true;
		    }
		}
		return false;
	    }
	});
	return unitsOfGivenType.size() == 1 ? (Unit) unitsOfGivenType.iterator().next() : null;
    }

    @Override
    public Content processPath(String path) {

	String[] possibleUnits = path.split("/");
	List<Unit> unitsList = findUnitsInPath(possibleUnits);
	return unitsList.isEmpty() ? null : unitsList.get(unitsList.size() - 1).getSite();

    }

    @Override
    public String getTrailingPath(String path) {
	String[] possibleUnits = path.split("/");
	List<Unit> unitsList = findUnitsInPath(possibleUnits);
	StringBuffer buffer = new StringBuffer("");
	Iterator<Unit> unitIterator = unitsList.iterator();
	while (unitIterator.hasNext()) {
	    buffer.append(unitIterator.next().getAcronym());
	    buffer.append("/");
	}

	return path.length() < buffer.length() ? "" : path.substring(buffer.length());
    }

    protected Unit getBaseUnit() {
	return RootDomainObject.getInstance().getInstitutionUnit();
    }

    protected abstract Class[] getAcceptableTypes();
}
