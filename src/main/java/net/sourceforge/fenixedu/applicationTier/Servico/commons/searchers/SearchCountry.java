package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

public class SearchCountry extends AbstractSearchObjects<Country> implements AutoCompleteProvider<Country> {

    @Override
    public Collection<Country> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(RootDomainObject.getInstance().getCountrysSet(), value, maxCount, argsMap);
    }

}
