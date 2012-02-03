package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchEmployeesAndTeachers extends FenixService implements AutoCompleteSearchService {

    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
	List<Person> result = new ArrayList<Person>();

	String slotName = arguments.get("slot");

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
