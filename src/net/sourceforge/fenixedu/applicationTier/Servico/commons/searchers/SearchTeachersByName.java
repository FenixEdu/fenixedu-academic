package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

import com.google.common.base.Predicate;

public class SearchTeachersByName extends FenixService implements AutoCompleteSearchService {

    protected Collection search(final String value, final int size) {
        final Predicate<Person> predicate = new Predicate<Person>() {
            @Override
            public boolean apply(final Person person) {
                return person.hasTeacher();
            }
        };
        final Collection<Person> people = Person.findPerson(value, size, predicate);
        final List<Teacher> teachers = new ArrayList<Teacher>();
        for (final Person person : people) {
            final Teacher teacher = person.getTeacher();
            teachers.add(teacher);
        }
        return teachers;
    }

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        return search(value, limit);
    }

}
