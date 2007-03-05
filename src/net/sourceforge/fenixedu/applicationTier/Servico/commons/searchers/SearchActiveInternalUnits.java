package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchActiveInternalUnits extends SearchInternalUnits {

    @Override
    protected Collection search(String value, int size) {
        Collection<UnitName> units = super.search(value, size);
        
        List<UnitName> result = new ArrayList<UnitName>();
        YearMonthDay now = new YearMonthDay();

        for (UnitName unitName : units) {
            if (unitName.getUnit().isActive(now)) {
                result.add(unitName);
            }
        }
        
        return result;
    }

}
