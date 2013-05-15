package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

public class SearchPartySocialSecurityNumber extends AbstractSearchObjects<PartySocialSecurityNumber> implements
        AutoCompleteProvider<PartySocialSecurityNumber> {

    @Override
    public Collection<PartySocialSecurityNumber> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(RootDomainObject.getInstance().getPartySocialSecurityNumbersSet(), value, maxCount, argsMap);
    }

}
