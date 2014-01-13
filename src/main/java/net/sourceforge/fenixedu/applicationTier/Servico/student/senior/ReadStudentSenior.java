package net.sourceforge.fenixedu.applicationTier.Servico.student.senior;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadStudentSenior {

    @Atomic
    public static Senior run(final Registration registration) {
        check(RolePredicates.STUDENT_PREDICATE);
        if (registration.hasSenior()) {
            return registration.getSenior();
        } else if (registration.isQualifiedForSeniority()) {
            return new Senior(registration);
        } else {
            return null;
        }
    }

}