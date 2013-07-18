package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchExternalUnits extends SearchParties<UnitName> {

    @Override
    protected Collection<UnitName> search(String value, int size) {
        return UnitName.findExternalUnit(value, size);
    }

}
