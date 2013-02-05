package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchUnits extends SearchParties {

    @Override
    protected Collection search(final String value, final int size) {
        final Collection<UnitName> unitNames = UnitName.find(value, size);
        final List<Unit> resultUnits = new ArrayList<Unit>();
        for (final UnitName name : unitNames) {
            final Unit unit = name.getUnit();
            resultUnits.add(unit);
        }
        return resultUnits;
    }

}
