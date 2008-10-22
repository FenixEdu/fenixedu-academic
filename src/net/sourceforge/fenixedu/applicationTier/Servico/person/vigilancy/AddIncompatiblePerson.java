package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import pt.ist.fenixWebFramework.services.Service;

public class AddIncompatiblePerson extends FenixService {

    @Service
    public static void run(Vigilant vigilant, Person person) {

	if (vigilant.hasIncompatiblePerson()) {
	    vigilant.removeIncompatiblePerson();
	}
	if (person.getCurrentVigilant().hasIncompatiblePerson()) {
	    person.getCurrentVigilant().removeIncompatiblePerson();
	}
	vigilant.setIncompatiblePerson(person);
    }

}