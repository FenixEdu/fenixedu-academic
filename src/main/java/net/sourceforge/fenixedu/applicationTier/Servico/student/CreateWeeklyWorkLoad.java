package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.Attends;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateWeeklyWorkLoad {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Atomic
    public static void run(final String attendsID, final Integer contact, final Integer autonomousStudy, final Integer other) {
        final Attends attends = FenixFramework.getDomainObject(attendsID);
        attends.createWeeklyWorkLoad(contact, autonomousStudy, other);
    }

}