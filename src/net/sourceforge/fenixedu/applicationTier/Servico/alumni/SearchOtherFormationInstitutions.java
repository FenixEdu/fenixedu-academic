package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SearchOtherFormationInstitutions extends Service implements AutoCompleteSearchService {

    public Collection run(Class type, String value, final int limit, Map<String, String> arguments) {

	value = value.toLowerCase();
	List<DomainObject> result = new ArrayList<DomainObject>();
	//TODO filter by foreign units only
	for (AcademicalInstitutionUnit unit : AcademicalInstitutionUnit.readOtherAcademicUnits()) {
	    if (unit.getName().toLowerCase().contains(value)) {
		result.add(unit);
	    }
	    
	    if (result.size() >= limit) {
		break;
	    }
	}

	return result;
    }
}
