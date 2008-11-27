package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class RemoveCandidateYearDelegateElections extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static void run(YearDelegateElection yearDelegateElection, Student student) throws FenixServiceException {

	try {
	    yearDelegateElection.removeCandidates(student);
	} catch (DomainException ex) {
	    throw new FenixServiceException(ex.getMessage(), ex.getArgs());
	}
    }

}