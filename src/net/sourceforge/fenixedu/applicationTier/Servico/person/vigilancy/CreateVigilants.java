package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateVigilants extends Service {

    public void run(List<Person> persons, VigilantGroup vigilantGroup) throws ExcepcaoPersistencia {

        for (Person person : persons) {
            Vigilant vigilant = new Vigilant(person);
            vigilant.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
            vigilant.addVigilantGroups(vigilantGroup);
        }

    }

}