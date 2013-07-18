package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.person.PersonName;

public class SearchInternalPersons extends SearchParties<PersonName> {

    @Override
    protected Collection<PersonName> search(String value, int size) {
        return PersonName.findInternalPerson(value, size);
    }

}
