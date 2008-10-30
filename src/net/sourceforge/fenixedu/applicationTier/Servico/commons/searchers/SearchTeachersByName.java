package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

public class SearchTeachersByName extends FenixService implements AutoCompleteSearchService {

    protected Collection search(final String value, final int size) {
	final Collection<Person> people = Person.findPerson(value, size);
	final List<Teacher> teachers = new ArrayList<Teacher>();
	for (final Person person : people) {
	    final Teacher teacher = person.getTeacher();
	    if (teacher != null) {
		teachers.add(teacher);
	    }
	}
	return teachers;
    }

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
	return search(value, limit);
    }

}
