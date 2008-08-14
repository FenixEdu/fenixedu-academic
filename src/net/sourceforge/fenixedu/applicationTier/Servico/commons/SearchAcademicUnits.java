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
	Collection<UnitName> unitNames = UnitName.findInternalUnitWithType(value, size, DegreeUnit.class);
	List<Unit> units = new ArrayList<Unit>();

	for (UnitName name : unitNames) {
	    units.add(name.getUnit());
	}

	return units;
    }

}
