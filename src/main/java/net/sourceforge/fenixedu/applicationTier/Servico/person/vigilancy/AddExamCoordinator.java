package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class AddExamCoordinator {

    @Service
    public static void run(Person person, ExecutionYear executionYear, Unit unit) {
        person.addExamCoordinator(executionYear, unit);
    }

}
