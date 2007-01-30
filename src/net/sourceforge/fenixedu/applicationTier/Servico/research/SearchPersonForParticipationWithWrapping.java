package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;

public class SearchPersonForParticipationWithWrapping extends SearchPersonsForParticipations {

	@Override
	public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {

		List<DomainObject> objects = super.run(type, value, limit, arguments);
		List personWrappers = new ArrayList();
		for (DomainObject object : objects) {
			personWrappers.add(new PersonWrapper((Person) object));
		}

		return personWrappers;
	}

	public static class PersonWrapper implements Serializable {
		private int idInternal;

		private String text;

		public PersonWrapper() {
			super();
		}

		public PersonWrapper(Person person) {
			this();
			this.idInternal = person.getIdInternal();
			this.text = buildText(person);
		}

		public Integer getIdInternal() {
			return this.idInternal;
		}

		public void setIdInternal(int idInternal) {
			this.idInternal = idInternal;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		private String buildText(Person person) {
			final StringBuilder text = new StringBuilder();
			text.append(person.getName());

			Employee employee = person.getEmployee();
			if (employee != null && employee.getLastWorkingPlace() != null) {
				text.append("(" + employee.getLastWorkingPlace().getName() + ")");
			} else {
				if (person.hasExternalPerson()) {
					text.append("(" + person.getExternalPerson().getInstitutionUnit().getName() + ")");
				} else {
					text.append("(" + person.getUsername() + ")");
				}
			}
			return text.toString();
		}
	}
}
