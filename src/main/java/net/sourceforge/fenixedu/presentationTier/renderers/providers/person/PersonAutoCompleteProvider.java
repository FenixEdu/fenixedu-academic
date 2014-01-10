package net.sourceforge.fenixedu.presentationTier.renderers.providers.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

public class PersonAutoCompleteProvider implements AutoCompleteProvider<Person> {

    @Override
    public Collection<Person> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String searchValue = StringNormalizer.normalize(value);

        final List<Person> result = new ArrayList<Person>();
        result.addAll(Person.findInternalPerson(value));
        result.add(Person.findByUsername(searchValue));
        return result;
    }

    private boolean match(final String stringToMatch, final String content) {
        final String normalizedContent = StringNormalizer.normalize(content);
        return normalizedContent.indexOf(stringToMatch) >= 0;
    }

}
