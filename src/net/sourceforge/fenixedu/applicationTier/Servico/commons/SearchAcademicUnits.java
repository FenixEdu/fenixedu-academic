package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchAcademicUnits extends SearchParties {

    @Override
    protected Collection search(String value, int size) {
	Collection<UnitName> unitNames = UnitName.find(value, size);
	List<DegreeUnit> units = new ArrayList<DegreeUnit>();

	for (UnitName unitName : unitNames) {
	    Unit unit = unitName.getUnit();
	    if (unit.isDegreeUnit()) {
		units.add((DegreeUnit) unit);
	    }
	}
	return units;
    }

}
