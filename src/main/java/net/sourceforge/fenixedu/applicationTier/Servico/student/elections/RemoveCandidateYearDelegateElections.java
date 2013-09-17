package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class RemoveCandidateYearDelegateElections {

    @Atomic
    public static void run(YearDelegateElection yearDelegateElection, Student student) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        try {
            yearDelegateElection.removeCandidates(student);
        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }

}