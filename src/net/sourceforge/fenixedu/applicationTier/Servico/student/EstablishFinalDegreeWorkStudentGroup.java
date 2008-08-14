/*
 * Created on 2004/04/15
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class EstablishFinalDegreeWorkStudentGroup extends Service {

    public boolean run(Person person, Integer executionDegreeOID) throws FenixServiceException {
	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);

	final Registration registration = getRegistrationForExecutionDegree(person, executionDegree);
	if (registration == null) {
	    throw new StudentCannotBeACandidateForSelectedDegree("Student.Cannot.Be.A.Candidate.For.Selected.Degree");
	}
	FinalDegreeWorkGroup group = registration.findFinalDegreeWorkGroupForExecutionYear(executionDegree.getExecutionYear());
	if (group == null) {
	    group = new FinalDegreeWorkGroup();
	    GroupStudent groupStudent = new GroupStudent();
	    groupStudent.setRegistration(registration);
	    groupStudent.setFinalDegreeDegreeWorkGroup(group);
	    // } else {
	    // if (!group.getGroupProposals().isEmpty()) {
	    // throw new GroupProposalCandidaciesExistException();
	    // }
	    // if (group.getGroupStudents().size() > 1) {
	    // throw new GroupStudentCandidaciesExistException();
	    // }
	}

	if (group.getExecutionDegree() == null || !group.getExecutionDegree().getIdInternal().equals(executionDegreeOID)) {
	    if (executionDegree != null) {
		group.setExecutionDegree(executionDegree);
	    }
	}

	return true;
    }

    private Registration getRegistrationForExecutionDegree(final Person person, final ExecutionDegree executionDegree) {
	final Student student = person.getStudent();
	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	for (final Registration registration : student.getRegistrationsSet()) {
	    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		if (degreeCurricularPlan == studentCurricularPlan.getDegreeCurricularPlan()) {
		    return registration;
		}
	    }
	}
	for (final Registration registration : student.getRegistrationsSet()) {
	    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getSecondCycle();
		if (cycleCurriculumGroup != null
			&& cycleCurriculumGroup.getDegreeCurricularPlanOfDegreeModule() == degreeCurricularPlan) {
		    return registration;
		}
	    }
	}
	return null;
    }

    public class StudentCannotBeACandidateForSelectedDegree extends FenixServiceException {

	public StudentCannotBeACandidateForSelectedDegree() {
	    super();
	}

	public StudentCannotBeACandidateForSelectedDegree(int errorType) {
	    super(errorType);
	}

	public StudentCannotBeACandidateForSelectedDegree(String s) {
	    super(s);
	}

	public StudentCannotBeACandidateForSelectedDegree(Throwable cause) {
	    super(cause);
	}

	public StudentCannotBeACandidateForSelectedDegree(String message, Throwable cause) {
	    super(message, cause);
	}

    }

    public class GroupStudentCandidaciesExistException extends FenixServiceException {

	public GroupStudentCandidaciesExistException() {
	    super();
	}

	public GroupStudentCandidaciesExistException(int errorType) {
	    super(errorType);
	}

	public GroupStudentCandidaciesExistException(String s) {
	    super(s);
	}

	public GroupStudentCandidaciesExistException(Throwable cause) {
	    super(cause);
	}

	public GroupStudentCandidaciesExistException(String message, Throwable cause) {
	    super(message, cause);
	}
    }

    public class GroupProposalCandidaciesExistException extends FenixServiceException {

	public GroupProposalCandidaciesExistException() {
	    super();
	}

	public GroupProposalCandidaciesExistException(int errorType) {
	    super(errorType);
	}

	public GroupProposalCandidaciesExistException(String s) {
	    super(s);
	}

	public GroupProposalCandidaciesExistException(Throwable cause) {
	    super(cause);
	}

	public GroupProposalCandidaciesExistException(String message, Throwable cause) {
	    super(message, cause);
	}
    }

}