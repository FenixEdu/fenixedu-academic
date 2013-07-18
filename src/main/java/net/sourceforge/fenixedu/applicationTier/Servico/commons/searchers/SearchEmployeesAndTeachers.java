package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchEmployeesAndTeachers implements AutoCompleteProvider<Person> {

    @Override
    public Collection<Person> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Person> result = new ArrayList<Person>();

        String slotName = argsMap.get("slot");

        if (value == null) {
            result = new ArrayList<Person>(Person.findInternalPerson(""));
        } else {
            for (Person person : Person.findInternalPerson(StringNormalizer.normalize(value).toLowerCase())) {
                if ((person.hasTeacher() && person.getTeacher().isActive())
                        || (person.hasEmployee() && person.getEmployee().isActive())) {
                    result.add(person);
                }
            }
        }

        Collections.sort(result, new BeanComparator(slotName));
        return result;
    }
}
