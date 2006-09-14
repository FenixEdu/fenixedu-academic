package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddVigilantsToGroup extends Service {

    public void run(List<Person> people, VigilantGroup group) throws ExcepcaoPersistencia {

        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        for (Person person : people) {
            Vigilant vigilant = person.getVigilantForGivenExecutionYear(currentYear);
            group.addVigilants((vigilant == null) ? new Vigilant(person, currentYear) : vigilant);
        }
    }

}