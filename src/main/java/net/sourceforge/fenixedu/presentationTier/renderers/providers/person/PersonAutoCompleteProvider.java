package net.sourceforge.fenixedu.presentationTier.renderers.providers.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;
import net.sourceforge.fenixedu.util.StringUtils;

public class PersonAutoCompleteProvider implements AutoCompleteProvider<Person> {

    @Override
    public Collection<Person> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String searchValue = StringUtils.normalize(value);

        final List<Person> result = new ArrayList<Person>();
        result.addAll(Person.findInternalPerson(value));
        result.add(Person.findByUsername(searchValue));
        return result;
    }

    private boolean match(final String stringToMatch, final String content) {
        final String normalizedContent = StringUtils.normalize(content);
        return normalizedContent.indexOf(stringToMatch) >= 0;
    }

}
