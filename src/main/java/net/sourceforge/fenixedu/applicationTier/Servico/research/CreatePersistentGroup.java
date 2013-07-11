package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManageUnitPersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class CreatePersistentGroup {

    protected void run(Unit unit, String name, List<Person> people, PersistentGroupMembersType type) {

        PersistentGroupMembers members = new PersistentGroupMembers(name, type);
        members.setUnit(unit);
        for (Person person : people) {
            members.addPersons(person);
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreatePersistentGroup serviceInstance = new CreatePersistentGroup();

    @Atomic
    public static void runCreatePersistentGroup(Unit unit, String name, List<Person> people, PersistentGroupMembersType type)
            throws NotAuthorizedException {
        ManageUnitPersistentGroup.instance.execute(unit);
        serviceInstance.run(unit, name, people, type);
    }
}