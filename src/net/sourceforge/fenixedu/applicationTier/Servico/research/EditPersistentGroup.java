package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class EditPersistentGroup extends Service {

	public void run(PersistentGroupMembers group, String name, List<Person> people, Unit unit) {

		group.setName(name);
		group.setUnit(unit);
		group.getPersons().clear();
		for(Person person : people) {
			group.addPersons(person);
		}
	}

}
