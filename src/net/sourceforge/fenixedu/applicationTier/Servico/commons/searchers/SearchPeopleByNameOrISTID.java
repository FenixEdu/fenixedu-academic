package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;

public class SearchPeopleByNameOrISTID extends SearchPeople {

	@Override
	protected Collection search(final String value, final int size) {
		if (value.length() > 3 && value.substring(0, 3).equals("ist")) {
			ArrayList<Person> result = new ArrayList<Person>();
			Person person = Person.readPersonByIstUsername(value);
			if (person != null) {
				result.add(person);
			}
			return result;
		} else {
			return super.search(value, size);

		}
	}

}
