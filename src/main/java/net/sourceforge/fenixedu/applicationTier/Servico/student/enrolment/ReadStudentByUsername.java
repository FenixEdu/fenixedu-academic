/*
 * Created on Sep 26, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadStudentByUsername {

    @Atomic
    public static Registration run(String studentUsername) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        final Registration registration = Registration.readByUsername(studentUsername);
        if (registration == null) {
            throw new FenixServiceException("error.noStudent");
        }
        return registration;
    }

}