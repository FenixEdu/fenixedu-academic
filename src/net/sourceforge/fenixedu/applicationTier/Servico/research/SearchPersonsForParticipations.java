package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class SearchPersonsForParticipations extends Service implements AutoCompleteSearchService {

    public Collection<PersonName> run(Class type, String value, int limit, Map<String, String> arguments) {
	if (type != PersonName.class) {
	    return null;
	}

	final int size = Integer.parseInt(arguments.get("size"));
	return PersonName.find(value, size);
    }

}
