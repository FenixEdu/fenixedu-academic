package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ResearchUnitPathProcessor extends AbstractPathProcessor {

    private List<Unit> findUnitsInPath(String[] possibleUnits) {
	List<Unit> units = new ArrayList<Unit>();
	Unit currentUnit = Unit.readUnitsByAcronym(possibleUnits[0]).get(0);

	if (currentUnit != null) {
	    units.add(currentUnit);
	    for (String name : possibleUnits) {
		for (Unit subUnit : currentUnit.getSubUnits()) {
		    if (subUnit.getAcronym().equalsIgnoreCase(name)) {
			currentUnit = subUnit;
			units.add(subUnit);
		    }
		}
	    }
	}
	return units;

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
	return path.substring(buffer.toString().length());

    }
}
