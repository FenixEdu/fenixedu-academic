package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.person.PersonName;

public class SearchInternalPersonsByNameHavingTeacher extends SearchParties {

    @Override
    protected Collection<PersonName> search(String value, int size) {
	final Collection<PersonName> result = new HashSet<PersonName>();
	for (final PersonName personName : PersonName.findInternalPerson(value, size)) {
	    if (personName.getPerson().hasTeacher()) {
		result.add(personName);
	    }
	}
	return result;
    }

}
