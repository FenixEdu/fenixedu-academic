package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

public class SearchPartySocialSecurityNumber extends AbstractSearchObjects<PartySocialSecurityNumber> implements
        AutoCompleteProvider<PartySocialSecurityNumber> {

    @Override
    public Collection<PartySocialSecurityNumber> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(Bennu.getInstance().getPartySocialSecurityNumbersSet(), value, maxCount, argsMap);
    }

}
