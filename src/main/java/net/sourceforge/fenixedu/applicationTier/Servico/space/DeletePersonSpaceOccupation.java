package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeletePersonSpaceOccupation {

    @Atomic
    public static Boolean run(PersonSpaceOccupation personSpaceOccupation) {
        check(RolePredicates.SPACE_MANAGER_PREDICATE);
        if (personSpaceOccupation != null) {
            personSpaceOccupation.delete();
        }
        return true;
    }
}