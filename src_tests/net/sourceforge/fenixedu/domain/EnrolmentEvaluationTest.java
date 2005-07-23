package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;


public class EnrolmentEvaluationTest extends DomainTestBase {

	private IEnrolmentEvaluation normalEvaluation;
	private IEnrolmentEvaluation evaluationToClear;
	private IEnrolmentEvaluation evaluationWithoutExamDate;
	
	private IPerson newResponsibleFor;
	private String newGrade;
	private Date newAvailableDate;
	private Date newExamDate;
	private String newChecksum;
	
	private IEnrolmentEvaluation aprovedEvaluation;
	private IEnrolmentEvaluation notAprovedEvaluation;
	private IEnrolmentEvaluation notEvaluatedEvaluation;
	
	private IEmployee employee;
	private String observation;
	
	private IEnrolmentEvaluation evaluation;
	private IEnrolmentEvaluation improvementEvaluation;
	private IEnrolmentEvaluation notImprovementEvaluation;
	
	private IExecutionPeriod currentExecutionPeriod;
	private IAttends attendsToDelete;
	private IAttends attendsNotToDelete;
	
	private IEnrolmentEvaluation mdEvaluationToInsert;
	
	
	
	
	
	protected void setUp() throws Exception {
        super.setUp();
		
		setUpEdit();
		setUpConfirmSubmission();
		setUpDelete();
		setUpInsertStudentFinalEvaluationForMasterDegree();
    }
	
	
	
	private void setUpEdit() {
		
		normalEvaluation = new EnrolmentEvaluation();
		evaluationToClear = new EnrolmentEvaluation();
		evaluationWithoutExamDate = new EnrolmentEvaluation();
		
		newResponsibleFor = new Person();
		newGrade = "20";
		newAvailableDate = new Date();
		newExamDate = new Date();
		newChecksum = "";
	}
	
	
	private void setUpConfirmSubmission() {
		aprovedEvaluation = new EnrolmentEvaluation();
		notAprovedEvaluation = new EnrolmentEvaluation();
		notEvaluatedEvaluation = new EnrolmentEvaluation();
		
		IEnrolment aprovedEnrolment = new Enrolment();
		aprovedEnrolment.addEvaluations(aprovedEvaluation);
		
		IEnrolment notAprovedEnrolment = new Enrolment();
		notAprovedEnrolment.addEvaluations(notAprovedEvaluation);
		
		IEnrolment notEvaluatedEnrolment = new Enrolment();
		notEvaluatedEnrolment.addEvaluations(notEvaluatedEvaluation);
		
		aprovedEvaluation.setGrade("20");
		notAprovedEvaluation.setGrade("RE");
		notEvaluatedEvaluation.setGrade("NA");
		
		employee = new Employee();
		aprovedEvaluation.setEmployee(employee);
		notAprovedEvaluation.setEmployee(employee);
		notEvaluatedEvaluation.setEmployee(employee);
		
		observation = "";
	}
	
	
	private void setUpDelete() {
		
		evaluation = new EnrolmentEvaluation();

		IPerson person = new Person();
		IEmployee employee = new Employee();
		IEnrolment enrolment = new Enrolment();
		
		evaluation.setPersonResponsibleForGrade(person);
		evaluation.setEmployee(employee);
		evaluation.setEnrolment(enrolment);
		
		
		improvementEvaluation = new EnrolmentEvaluation();
		notImprovementEvaluation = new EnrolmentEvaluation();
		
		improvementEvaluation.setEnrolment(enrolment);
		notImprovementEvaluation.setEnrolment(enrolment);
		
		improvementEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		improvementEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
		
		notImprovementEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		notImprovementEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		

		//associated attends
		currentExecutionPeriod = new ExecutionPeriod();
		IExecutionPeriod notCurrentExecutionPeriod = new ExecutionPeriod();
		
		attendsToDelete = new Attends();
		attendsNotToDelete = new Attends();
		
		ICurricularCourse curricularCourse = new CurricularCourse();
		
		IExecutionCourse currentExecutionCourse = new ExecutionCourse();
		IExecutionCourse notCurrentExecutionCourse = new ExecutionCourse();
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		IStudent student = new Student();
		
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
	
	
	private void setUpInsertStudentFinalEvaluationForMasterDegree() {
		
		mdEvaluationToInsert = new EnrolmentEvaluation();
		IEnrolment mdEnrolmentToInsert = new Enrolment();
		IStudentCurricularPlan mdStudentCurricularPlanToInsert = new StudentCurricularPlan();
		IDegreeCurricularPlan mdDegreeCurricularPlanToInsert = new DegreeCurricularPlan();
		
		mdEvaluationToInsert.setEnrolment(mdEnrolmentToInsert);
		mdEnrolmentToInsert.setStudentCurricularPlan(mdStudentCurricularPlanToInsert);
		mdStudentCurricularPlanToInsert.setDegreeCurricularPlan(mdDegreeCurricularPlanToInsert);
	}

	
	
	
    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	
	
	public void testEdit() {
		
		normalEvaluation.edit(newResponsibleFor, newGrade, newAvailableDate, newExamDate, newChecksum);
		evaluationToClear.edit(null, null, null, null, null);
		evaluationWithoutExamDate.edit(newResponsibleFor, newGrade, newAvailableDate, null, newChecksum);
		
		assertTrue(normalEvaluation.getPersonResponsibleForGrade().equals(newResponsibleFor));
		assertTrue(normalEvaluation.getGrade().equals(newGrade));
		assertTrue(normalEvaluation.getGradeAvailableDate().equals(newAvailableDate));
		assertTrue(normalEvaluation.getExamDate().equals(newExamDate));
		assertTrue(normalEvaluation.getCheckSum().equals(newChecksum));
		
		assertFalse(evaluationToClear.hasPersonResponsibleForGrade());
		assertNull(evaluationToClear.getGrade());
		assertNull(evaluationToClear.getGradeAvailableDate());
		assertNull(evaluationToClear.getExamDate());
		assertNull(evaluationToClear.getCheckSum());
		
		assertTrue(evaluationWithoutExamDate.getPersonResponsibleForGrade().equals(newResponsibleFor));
		assertTrue(evaluationWithoutExamDate.getGrade().equals(newGrade));
		assertTrue(evaluationWithoutExamDate.getGradeAvailableDate().equals(newAvailableDate));
		assertTrue(evaluationWithoutExamDate.getExamDate().equals(newAvailableDate));
		assertTrue(evaluationWithoutExamDate.getCheckSum().equals(newChecksum));
	}
	
	
	public void testConfirmSubmission() {
		
		aprovedEvaluation.confirmSubmission(employee, observation);
		notAprovedEvaluation.confirmSubmission(employee, observation);
		notEvaluatedEvaluation.confirmSubmission(employee, observation);
		
		assertTrue(aprovedEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ));
		assertTrue(aprovedEvaluation.getEmployee().equals(employee));
		assertTrue(aprovedEvaluation.getObservation().equals(observation));
		assertTrue(aprovedEvaluation.getEnrolment().getEnrollmentState().equals(EnrollmentState.APROVED));
		
		assertTrue(notAprovedEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ));
		assertTrue(notAprovedEvaluation.getEmployee().equals(employee));
		assertTrue(notAprovedEvaluation.getObservation().equals(observation));
		assertTrue(notAprovedEvaluation.getEnrolment().getEnrollmentState().equals(EnrollmentState.NOT_APROVED));
		
		assertTrue(notEvaluatedEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ));
		assertTrue(notEvaluatedEvaluation.getEmployee().equals(employee));
		assertTrue(notEvaluatedEvaluation.getObservation().equals(observation));
		assertTrue(notEvaluatedEvaluation.getEnrolment().getEnrollmentState().equals(EnrollmentState.NOT_EVALUATED));
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
	
	
//	public void testInsertStudentFinalEvaluationForMasterDegree() {
//		
//		mdEvaluationToInsert.insertStudentFinalEvaluationForMasterDegree("20", newResponsibleFor, newExamDate);
//		
//		assertTrue(mdEvaluationToInsert.getGrade().equals("20"));
//		assertTrue(mdEvaluationToInsert.getPersonResponsibleForGrade().equals(newResponsibleFor));
//		assertTrue(mdEvaluationToInsert.getExamDate().equals(newExamDate));		
//		
//		mdEvaluationToInsert.insertStudentFinalEvaluationForMasterDegree("", newResponsibleFor, newExamDate);
//		
//		assertNull(mdEvaluationToInsert.getPersonResponsibleForGrade());
//		assertNull(mdEvaluationToInsert.getGrade());
//		assertNull(mdEvaluationToInsert.getGradeAvailableDate());
//		assertNull(mdEvaluationToInsert.getExamDate());
//		assertNull(mdEvaluationToInsert.getCheckSum());
//		
//		try {
//			mdEvaluationToInsert.insertStudentFinalEvaluationForMasterDegree("30", newResponsibleFor, newExamDate);
//			fail("Should not have been inserted.");
//		} catch (DomainException e) {
//			
//		}
//		
//		mdEvaluationToInsert.insertStudentFinalEvaluationForMasterDegree("RE", newResponsibleFor, newExamDate);
//		
//		assertTrue(mdEvaluationToInsert.getGrade().equals("RE"));
//		assertTrue(mdEvaluationToInsert.getPersonResponsibleForGrade().equals(newResponsibleFor));
//		assertTrue(mdEvaluationToInsert.getExamDate().equals(newExamDate));	
//	}

}
