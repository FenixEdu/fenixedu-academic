package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.person.PersonName;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchPersonsForParticipations implements AutoCompleteProvider<PersonName> {

    @Override
    public Collection<PersonName> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        String size = argsMap.get("size");
        return PersonName.findInternalPerson(value, (size == null) ? 20 : Integer.parseInt(size));
    }

}
