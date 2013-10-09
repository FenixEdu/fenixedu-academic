package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateWeeklyWorkLoad {

    @Atomic
    public static void run(final String attendsID, final Integer contact, final Integer autonomousStudy, final Integer other) {
        check(RolePredicates.STUDENT_PREDICATE);
        final Attends attends = FenixFramework.getDomainObject(attendsID);
        attends.createWeeklyWorkLoad(contact, autonomousStudy, other);
    }

}