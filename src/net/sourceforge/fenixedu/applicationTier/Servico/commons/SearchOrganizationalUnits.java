package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchOrganizationalUnits extends SearchParties {
    @Override
    protected Collection search(String value, int size) {
	Collection<UnitName> unitNames = UnitName.find(value, size);
	List<Unit> resultUnits = new ArrayList<Unit>();
	for (UnitName name : unitNames) {
	    Unit unit = name.getUnit();
	    if (unit.isDepartmentUnit() || unit.isScientificAreaUnit()) {
		resultUnits.add(unit);
	    }
	}
	return resultUnits;
    }

}
