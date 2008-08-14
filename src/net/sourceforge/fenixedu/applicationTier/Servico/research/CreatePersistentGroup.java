package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreatePersistentGroup extends Service {

    public void run(Unit unit, String name, List<Person> people, PersistentGroupMembersType type) {

	PersistentGroupMembers members = new PersistentGroupMembers(name, type);
	members.setUnit(unit);
	for (Person person : people) {
	    members.addPersons(person);
	}
    }
}
