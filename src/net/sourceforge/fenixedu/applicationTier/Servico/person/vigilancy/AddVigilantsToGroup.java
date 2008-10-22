package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;

public class AddVigilantsToGroup extends FenixService {

    @Service
    public static void run(Map<VigilantGroup, List<Person>> peopleToAdd) {

	Set<VigilantGroup> groups = peopleToAdd.keySet();
	for (VigilantGroup group : groups) {
	    List<Person> people = peopleToAdd.get(group);
	    for (Person person : people) {
		Vigilant vigilant = person.getCurrentVigilant();
		group.addVigilants((vigilant == null) ? new Vigilant(person) : vigilant);
	    }
	}
    }

}