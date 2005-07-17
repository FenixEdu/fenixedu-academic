package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;


public class EnrolmentEvaluationTest extends DomainTestBase {

	private IEnrolmentEvaluation evaluation;
	private IEnrolmentEvaluation improvementEvaluation;
	private IEnrolmentEvaluation notImprovementEvaluation;
	
	private IExecutionPeriod currentExecutionPeriod;
	private IAttends attendsToDelete;
	private IAttends attendsNotToDelete;
	
	
	
	
	
	protected void setUp() throws Exception {
        super.setUp();
		
		evaluation = new EnrolmentEvaluation();
		evaluation.setIdInternal(1);
		

		IPerson person = new Person();
		person.setIdInternal(1);
		
		IEmployee employee = new Employee();
		employee.setIdInternal(1);
		
		IEnrolment enrolment = new Enrolment();
		enrolment.setIdInternal(1);
		
		
		
		evaluation.setPersonResponsibleForGrade(person);
		evaluation.setEmployee(employee);
		evaluation.setEnrolment(enrolment);
		
		
		improvementEvaluation = new EnrolmentEvaluation();
		notImprovementEvaluation = new EnrolmentEvaluation();
		improvementEvaluation.setIdInternal(2);
		notImprovementEvaluation.setIdInternal(3);
		
		improvementEvaluation.setEnrolment(enrolment);
		notImprovementEvaluation.setEnrolment(enrolment);
		
		improvementEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		improvementEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
		
		notImprovementEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		notImprovementEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		
		
		
		
		
		
		
		//associated attends
		currentExecutionPeriod = new ExecutionPeriod();
		currentExecutionPeriod.setIdInternal(1);
		IExecutionPeriod notCurrentExecutionPeriod = new ExecutionPeriod();
		notCurrentExecutionPeriod.setIdInternal(2);
		
		attendsToDelete = new Attends();
		attendsToDelete.setIdInternal(1);
		attendsNotToDelete = new Attends();
		attendsNotToDelete.setIdInternal(2);
		
		ICurricularCourse curricularCourse = new CurricularCourse();
		curricularCourse.setIdInternal(1);
		
		IExecutionCourse currentExecutionCourse = new ExecutionCourse();
		currentExecutionCourse.setIdInternal(1);
		IExecutionCourse notCurrentExecutionCourse = new ExecutionCourse();
		notCurrentExecutionCourse.setIdInternal(2);
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		studentCurricularPlan.setIdInternal(1);
		IStudent student = new Student();
		student.setIdInternal(1);
		
		enrolment.setCurricularCourse(curricularCourse);
		enrolment.setStudentCurricularPlan(studentCurricularPlan);
		enrolment.addAttends(attendsToDelete);
		enrolment.addAttends(attendsNotToDelete);
		curricularCourse.addAssociatedExecutionCourses(currentExecutionCourse);
		curricularCourse.addAssociatedExecutionCourses(notCurrentExecutionCourse);
		studentCurricularPlan.setStudent(student);
		attendsToDelete.setAluno(student);
		attendsNotToDelete.setAluno(student);
		attendsToDelete.setDisciplinaExecucao(currentExecutionCourse);
		attendsNotToDelete.setDisciplinaExecucao(notCurrentExecutionCourse);
		currentExecutionCourse.setExecutionPeriod(currentExecutionPeriod);
		notCurrentExecutionCourse.setExecutionPeriod(notCurrentExecutionPeriod);
		
		
    }

	
	
	
    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDelete() {
		
		evaluation.delete();
		
		assertFalse(evaluation.hasPersonResponsibleForGrade());
		assertFalse(evaluation.hasEmployee());
		assertFalse(evaluation.hasEnrolment());
		
		
		try {
			improvementEvaluation.unEnrollImprovment(currentExecutionPeriod);
			
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		
		
		assertFalse(attendsToDelete.hasEnrolment());
		assertTrue(attendsNotToDelete.hasEnrolment());
		
		
		try {
			notImprovementEvaluation.unEnrollImprovment(currentExecutionPeriod);
			fail("Should not have been deleted.");
		} catch (DomainException e) {
			
		}
	}

}
