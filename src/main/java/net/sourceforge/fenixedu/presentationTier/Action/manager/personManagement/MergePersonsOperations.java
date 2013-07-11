package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class MergePersonsOperations {

    @Atomic
    public static void removeFromPersistentGroups(Person person) {
        for (PersistentGroupMembers group : person.getPersistentGroups()) {
            group.removePersons(person);
        }
    }

    @Atomic
    public static void removeFromUploadUnits(Person person) {
        for (Unit unit : person.getUnitsWithUploadPermission()) {
            unit.removeAllowedPeopleToUploadFiles(person);
        }
    }

}
