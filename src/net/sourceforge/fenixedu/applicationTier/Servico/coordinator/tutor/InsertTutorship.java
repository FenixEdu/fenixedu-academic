package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertTutorship extends TutorshipManagement {
	
	public void run(Integer executionDegreeID, TutorshipManagementBean bean) 
		throws FenixServiceException, ExcepcaoPersistencia {
		
		final Integer studentNumber = bean.getStudentNumber();
		final Teacher teacher = bean.getTeacher();
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		
		validateTeacher(teacher, executionDegree);

		Registration registration = Registration.readRegisteredRegistrationByNumberAndDegreeType(
				studentNumber, executionDegree.getDegreeCurricularPlan().getDegreeType());
		
		validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

		validateTutorship(registration);
		
		createTutorship(teacher, registration.getActiveStudentCurricularPlan(), bean.getTutorshipEndMonth().getNumberOfMonth(), bean.getTutorshipEndYear());
	}

	public List<TutorshipErrorBean> run(Integer executionDegreeID, StudentsByEntryYearBean bean) 
		throws FenixServiceException, ExcepcaoPersistencia {
		
		final List<StudentCurricularPlan> students = bean.getStudentsToCreateTutorshipList();
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		final Teacher teacher = bean.getTeacher();
		
		validateTeacher(teacher, executionDegree);
		
		List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

		for (StudentCurricularPlan studentCurricularPlan : students) {
			Registration registration = studentCurricularPlan.getRegistration();
			Integer studentNumber = registration.getNumber();
			
			if (!studentCurricularPlan.getRegistrationStateType().isActive()) {
				studentsWithErrors.add(new TutorshipErrorBean("error.tutor.notActiveRegistration", new String[] {Integer.toString(studentNumber)}));
				continue;
			}
			
			try{
				validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);
				
				validateTutorship(registration);
				
				createTutorship(teacher, studentCurricularPlan, bean.getTutorshipEndMonth().getNumberOfMonth(), bean.getTutorshipEndYear());
			
			} catch (FenixServiceException ex) {
				studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
			} catch (DomainException ex) {
				studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
			}
		}

		return studentsWithErrors;
	}
}
