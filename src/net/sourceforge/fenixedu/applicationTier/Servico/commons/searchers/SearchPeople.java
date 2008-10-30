package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;

public abstract class SearchPeople extends SearchParties {

    @Override
    protected Collection search(final String value, final int size) {
	return Person.findPerson(value, size);
    }

}
