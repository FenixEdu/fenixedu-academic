package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Person;

public class SearchPeople extends FenixService implements AutoCompleteSearchService {

    protected Collection search(final String value, final int size) {
        return Person.findPerson(value, size);
    }

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        return search(value, limit);
    }

}
