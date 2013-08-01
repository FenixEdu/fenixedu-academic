package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchPartySocialSecurityNumber extends AbstractSearchObjects<PartySocialSecurityNumber> implements
        AutoCompleteProvider<PartySocialSecurityNumber> {

    @Override
    public Collection<PartySocialSecurityNumber> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(Bennu.getInstance().getPartySocialSecurityNumbersSet(), value, maxCount, argsMap);
    }

}
