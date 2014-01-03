package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

public class SearchPartiesBySocialSecurityNumber implements AutoCompleteProvider<Party> {

    @Override
    public Collection<Party> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<Party> parties = new ArrayList<Party>();
        for (final PartySocialSecurityNumber partySocialSecurityNumber : Bennu.getInstance()
                .getPartySocialSecurityNumbersSet()) {
            if (partySocialSecurityNumber.getSocialSecurityNumber().indexOf(value) >= 0) {
                parties.add(partySocialSecurityNumber.getParty());
            }
        }
        return parties;
    }

}
