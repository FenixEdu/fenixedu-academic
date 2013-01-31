package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

import com.google.common.base.Predicate;

public class SearchTeachersByNameOrISTID extends FenixService implements AutoCompleteSearchService {

	protected Collection search(final String value, final int size) {
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

		for (Teacher teacher : RootDomainObject.getInstance().getTeachers()) {
			if (teacher.getTeacherId() != null && teacher.getTeacherId().indexOf(value) >= 0) {
				teachers.add(teacher);
			}
		}
		return teachers;
	}

	@Override
	@Service
	public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
		return search(value, limit);
	}

}
