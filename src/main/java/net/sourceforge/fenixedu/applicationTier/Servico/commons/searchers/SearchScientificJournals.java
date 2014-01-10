package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AbstractSearchObjects;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchScientificJournals extends AbstractSearchObjects<ScientificJournal> implements
        AutoCompleteProvider<ScientificJournal> {

    @Override
    public Collection<ScientificJournal> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return process(Bennu.getInstance().getScientificJournalsSet(), value, maxCount, argsMap);
    }

}
