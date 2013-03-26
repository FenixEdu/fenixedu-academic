package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

public class SearchPartiesBySocialSecurityNumber extends FenixService implements AutoCompleteSearchService {

    protected Collection search(final String value, final int size) {
        final List<Party> parties = new ArrayList<Party>();
        for (final PartySocialSecurityNumber partySocialSecurityNumber : rootDomainObject.getPartySocialSecurityNumbersSet()) {
            if (partySocialSecurityNumber.getSocialSecurityNumber().indexOf(value) >= 0) {
                parties.add(partySocialSecurityNumber.getParty());
            }
        }
        return parties;
    }

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        return search(value, limit);
    }

}
