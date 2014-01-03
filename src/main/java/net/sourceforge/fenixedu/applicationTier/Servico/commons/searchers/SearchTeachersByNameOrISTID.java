package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;

import com.google.common.base.Predicate;

public class SearchTeachersByNameOrISTID implements AutoCompleteProvider<Teacher> {

    protected Collection<Teacher> search(final String value, final int size) {
        final Predicate<Person> predicate = new Predicate<Person>() {
            @Override
            public boolean apply(final Person person) {
                return person.hasTeacher();
            }
        };
        final Collection<Person> people = Person.findPerson(value, size, predicate);
        final Set<Teacher> teachers = new HashSet<Teacher>();
        for (final Person person : people) {
            teachers.add(person.getTeacher());
        }

        for (Teacher teacher : Bennu.getInstance().getTeachersSet()) {
            if (teacher.getTeacherId() != null && teacher.getTeacherId().indexOf(value) >= 0) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    @Override
    @Atomic
    public Collection<Teacher> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return search(value, maxCount);
    }

}
