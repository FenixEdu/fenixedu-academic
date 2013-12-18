package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

public class SearchResearchEvent extends AbstractSearchObjects<ResearchEvent> implements AutoCompleteProvider<ResearchEvent> {

    @Override
    public Collection<ResearchEvent> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(Bennu.getInstance().getEventsSet(), value, maxCount, argsMap);
    }
}
