package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManageUnitPersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class EditPersistentGroup extends FenixService {

    protected void run(PersistentGroupMembers group, String name, List<Person> people, Unit unit) {

        group.setName(name);
        group.setUnit(unit);
        group.getPersons().clear();
        for (Person person : people) {
            group.addPersons(person);
        }
    }

    // Service Invokers migrated from Berserk

    private static final EditPersistentGroup serviceInstance = new EditPersistentGroup();

    @Service
    public static void runEditPersistentGroup(PersistentGroupMembers group, String name, List<Person> people, Unit unit)
            throws NotAuthorizedException {
        ManageUnitPersistentGroup.instance.execute(group.getUnit());
        serviceInstance.run(group, name, people, unit);
    }

}