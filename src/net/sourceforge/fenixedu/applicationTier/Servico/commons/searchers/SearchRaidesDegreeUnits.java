package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchRaidesDegreeUnits extends SearchParties {

	@Override
	protected Collection search(String value, int size) {
		return UnitName.findExternalAcademicUnit(value, size);
	}
}
