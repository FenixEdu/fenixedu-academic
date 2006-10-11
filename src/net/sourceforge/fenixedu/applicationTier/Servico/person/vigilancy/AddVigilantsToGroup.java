package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddVigilantsToGroup extends Service {

//    public void run(List<Person> people, VigilantGroup group) throws ExcepcaoPersistencia {
//
//        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
//        for (Person person : people) {
//            Vigilant vigilant = person.getVigilantForGivenExecutionYear(currentYear);
//            group.addVigilants((vigilant == null) ? new Vigilant(person, currentYear) : vigilant);
//        }
//    }

	 public void run(Map<VigilantGroup,List<Person>> peopleToAdd) throws ExcepcaoPersistencia {
		 
		 Set<VigilantGroup> groups = peopleToAdd.keySet();
		 for (VigilantGroup group : groups) {
				List<Person> people = peopleToAdd.get(group);
				for (Person person : people) {
					Vigilant vigilant = person.getCurrentVigilant();
					group.addVigilants((vigilant == null) ? new Vigilant(person)
							: vigilant);
				}
			}
	 }

}