package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeletePersonSpaceOccupation {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Atomic
    public static Boolean run(PersonSpaceOccupation personSpaceOccupation) {
        if (personSpaceOccupation != null) {
            personSpaceOccupation.delete();
        }
        return true;
    }
}