package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchUnitByClassification extends SearchParties {

    @Override
    protected Collection search(String value, int size) {
	Collection<UnitName> units = UnitName.findInternalUnit(value, size);
	Collection<Unit> resultUnits = new ArrayList<Unit>();

	for (UnitName unitName : units) {
	    Unit unit = unitName.getUnit();
	    if (isValidClassification(unit.getClassification())) {
		resultUnits.add(unit);
	    }
	}
	return resultUnits;
    }

    protected boolean isValidClassification(UnitClassification classification) {
	return true;
    }

}
