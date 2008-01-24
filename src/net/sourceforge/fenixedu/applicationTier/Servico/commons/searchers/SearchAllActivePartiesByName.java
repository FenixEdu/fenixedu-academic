package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;

import org.joda.time.YearMonthDay;

public class SearchAllActivePartiesByName extends SearchParties {

    @Override
    protected Collection search(String value, int size) {
	
	Collection<Party> result = new ArrayList<Party>(); 		
	YearMonthDay currentDate = new YearMonthDay();
	
	Collection<PersonName> persons = PersonName.find(value, size);
	for (PersonName personName : persons) {	    
	    result.add(personName.getPerson());
	}
	
	Collection<UnitName> units = UnitName.find(value, size);
	for (UnitName unitName : units) {
	    if(unitName.getUnit().isActive(currentDate)) {
		result.add(unitName.getUnit());
	    }
	}
	
	return result;
    }

}
