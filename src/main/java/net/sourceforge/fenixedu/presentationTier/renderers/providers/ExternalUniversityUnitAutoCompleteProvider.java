package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.joda.time.YearMonthDay;

public class ExternalUniversityUnitAutoCompleteProvider implements AutoCompleteProvider<Unit> {

    @Override
    public Collection<Unit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<Unit> result = new ArrayList<Unit>();
        final YearMonthDay today = new YearMonthDay();
        final Predicate predicate = new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final UnitName unitName = (UnitName) arg0;
                final Unit unit = unitName.getUnit();
                return unit instanceof UniversityUnit && unit.isActive(today);
            }
        };
        for (final UnitName unitName : UnitName.findExternalUnit(value, maxCount, predicate)) {
            final Unit unit = unitName.getUnit();
            result.add(unit);
        }
        return result;
    }

}
