package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.Collection;
import java.util.Map;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import net.sourceforge.fenixedu.domain.person.PersonName;

public class SearchExternalPersonForParticipations implements AutoCompleteProvider<PersonName> {

    @Override
    public Collection<PersonName> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        String size = argsMap.get("size");
        return PersonName.findExternalPerson(value, (size == null) ? 20 : Integer.parseInt(size));
    }

}
