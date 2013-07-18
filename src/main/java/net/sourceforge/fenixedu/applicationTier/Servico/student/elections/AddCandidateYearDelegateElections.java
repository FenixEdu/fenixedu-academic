package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AddCandidateYearDelegateElections {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static void run(YearDelegateElection yearDelegateElection, Student student) throws FenixServiceException {

        try {
            if (!yearDelegateElection.getCandidates().contains(student)) {
                yearDelegateElection.addCandidates(student);
            } else {
                throw new FenixServiceException("error.student.elections.candidacy.studentAlreadyCandidated");
            }
        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }

}