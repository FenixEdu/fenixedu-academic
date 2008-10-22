package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;

public class CreateVigilants extends FenixService {

    @Service
    public static void run(List<Person> persons, VigilantGroup vigilantGroup) {

	for (Person person : persons) {
	    Vigilant vigilant = new Vigilant(person);
	    vigilant.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    vigilant.addVigilantGroups(vigilantGroup);
	}

    }

}