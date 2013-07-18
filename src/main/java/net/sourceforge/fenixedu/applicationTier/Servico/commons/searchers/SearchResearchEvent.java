package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

public class SearchResearchEvent extends AbstractSearchObjects<ResearchEvent> implements
        AutoCompleteProvider<ResearchEvent> {

    @Override
    public Collection<ResearchEvent> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(RootDomainObject.getInstance().getEvents(), value, maxCount, argsMap);
    }
}
