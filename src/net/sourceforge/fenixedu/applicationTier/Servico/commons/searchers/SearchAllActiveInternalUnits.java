package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.joda.time.YearMonthDay;

public class SearchAllActiveInternalUnits extends SearchInternalUnits {

    @Override
    protected Collection search(String value, int size) {
	Collection<UnitName> units = super.search(value, size);
        
        List<Unit> result = new ArrayList<Unit>();
        YearMonthDay now = new YearMonthDay();

        for (UnitName unitName : units) {            
            Unit unit = unitName.getUnit();
            if(unit.isActive(now)) {
        	result.add(unit);            
            }
        }
       
        return result;	
    }
}
