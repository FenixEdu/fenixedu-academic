package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

public class ExternalUniversityUnitAutoCompleteProvider implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<Unit> result = new ArrayList<Unit>();
        for (final UnitName unitName : UnitName.findExternalUnit(value, maxCount)) {
            final Unit unit = unitName.getUnit();
            if (unit instanceof UniversityUnit) {
                result.add(unit);
            }
        }
        return result;
    }

}
