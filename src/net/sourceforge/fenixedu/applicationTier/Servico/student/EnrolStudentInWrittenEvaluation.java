package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EnrolStudentInWrittenEvaluation extends Service {

    public void run(String username, Integer writtenEvaluationOID) throws FenixServiceException,
            ExcepcaoPersistencia {

	final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject.readEvaluationByOID(writtenEvaluationOID);
	final Person person = Person.readPersonByUsername(username);
	final Student student = person.getStudent();
	final Registration registration = findCorrectRegistration(student, writtenEvaluation.getAssociatedExecutionCoursesSet());
        if (writtenEvaluation == null || registration == null) {
            throw new InvalidArgumentsServiceException();
        }

        enrolmentAction(writtenEvaluation, registration);
    }

    private Registration findCorrectRegistration(final Student student, final Set<ExecutionCourse> associatedExecutionCoursesSet) {
	for (final Registration registration : student.getRegistrationsSet()) {
	    for (final Attends attends : registration.getAssociatedAttendsSet()) {
		final ExecutionCourse executionCourse = attends.getExecutionCourse();
		if (associatedExecutionCoursesSet.contains(executionCourse)) {
		    return registration;
		}
	    }
	}
	return null;
    }

    public void enrolmentAction(final WrittenEvaluation writtenEvaluation, final Registration registration) {
	writtenEvaluation.enrolStudent(registration);
    }
}

