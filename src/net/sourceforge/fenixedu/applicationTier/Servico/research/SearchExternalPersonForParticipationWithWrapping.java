package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.research.SearchPersonForParticipationWithWrapping.PersonWrapper;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;

public class SearchExternalPersonForParticipationWithWrapping extends SearchExternalPersonForParticipations {
   
	@Override
	public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {

		List<DomainObject> objects = super.run(type, value, limit, arguments);
		List personWrappers = new ArrayList();
		for (DomainObject object : objects) {
			personWrappers.add(new PersonWrapper((Person) object));
		}

		return personWrappers;
	}
}
