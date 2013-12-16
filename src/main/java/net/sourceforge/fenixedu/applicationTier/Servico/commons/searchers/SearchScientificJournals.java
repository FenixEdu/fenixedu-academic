package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchScientificJournals extends AbstractSearchObjects<ScientificJournal> implements
        AutoCompleteProvider<ScientificJournal> {

    @Override
    public Collection<ScientificJournal> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(Bennu.getInstance().getScientificJournalsSet(), value, maxCount, argsMap);
    }

}
