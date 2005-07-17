package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

public class EnrolmentTest extends DomainTestBase {

	// A + B -> C
	// A -> D
	
	private IEnrolment enrolmentA;
	private IEnrolment enrolmentB;
	private IEnrolment enrolmentC;
	private IEnrolment enrolmentD;
	private IEnrolment enrolmentE;		//enrolment able to unenroll
	private IEnrolment enrolmentF;		//enrolment unable to unenroll
	private IEnrolment enrolmentWithImprovement;
	private IEnrolment enrolmentWithoutImprovement;
	private List<IEnrolmentEvaluation> evaluations;
	private List<ICreditsInAnySecundaryArea> creditsInAnySecundaryAreas;
	private List<ICreditsInScientificArea> creditsInScientificAreas;
	private List<IEquivalentEnrolmentForEnrolmentEquivalence> equivalentEnrolmentsA;
	private List<IEquivalentEnrolmentForEnrolmentEquivalence> equivalentEnrolmentsB;
	private List<IEnrolmentEquivalence> enrolmentEquivalencesC;
	private List<IEnrolmentEquivalence> enrolmentEquivalencesD;
	private IEnrolmentEvaluation improvementEvaluation;
	
	
	
	
	
	protected void setUp() throws Exception {
        super.setUp();
		
		enrolmentA = new Enrolment();
		enrolmentB = new Enrolment();
		enrolmentC = new Enrolment();
		enrolmentD = new Enrolment();
		enrolmentA.setIdInternal(1);
		enrolmentB.setIdInternal(2);
		enrolmentC.setIdInternal(3);
		enrolmentD.setIdInternal(4);
		
				
		/*
		 *  EnrolmentEvaluation
		 */
		IEnrolmentEvaluation ee1 = new EnrolmentEvaluation();
		IEnrolmentEvaluation ee2 = new EnrolmentEvaluation();
		ee1.setIdInternal(1);
		ee2.setIdInternal(2);
		
		IPerson person = new Person();
		IEmployee employee = new Employee();
		person.setIdInternal(1);
		employee.setIdInternal(1);
		
		ee1.setPersonResponsibleForGrade(person);
		ee2.setPersonResponsibleForGrade(person);
		ee1.setEmployee(employee);
		ee2.setEmployee(employee);
		
		evaluations = new ArrayList<IEnrolmentEvaluation>();
		evaluations.add(ee1);
		evaluations.add(ee2);
		enrolmentA.addEvaluations(ee1);
		enrolmentA.addEvaluations(ee2);
		
		
		
		/*
		 * ExecutionPeriod
		 */
		IExecutionPeriod ep1 = new ExecutionPeriod();
		ep1.setIdInternal(1);
		enrolmentA.setExecutionPeriod(ep1);
		
		
		/*
		 * StudentCurricularPlan
		 */
		IStudentCurricularPlan scp = new StudentCurricularPlan();
		scp.setIdInternal(1);
		enrolmentA.setStudentCurricularPlan(scp);
		
		
		/*
		 * CreditsInAnySecundaryArea
		 */
		ICreditsInAnySecundaryArea ciasa1 = new CreditsInAnySecundaryArea();
		ICreditsInAnySecundaryArea ciasa2 = new CreditsInAnySecundaryArea();
		ciasa1.setIdInternal(1);
		ciasa2.setIdInternal(2);
		
		ciasa1.setStudentCurricularPlan(scp);
		ciasa2.setStudentCurricularPlan(scp);
		
		creditsInAnySecundaryAreas = new ArrayList<ICreditsInAnySecundaryArea>();
		creditsInAnySecundaryAreas.add(ciasa1);
		creditsInAnySecundaryAreas.add(ciasa2);
		enrolmentA.addCreditsInAnySecundaryAreas(ciasa1);
		enrolmentA.addCreditsInAnySecundaryAreas(ciasa2);
		
		
		/*
		 * CreditsInScientificArea
		 */
		ICreditsInScientificArea cisa1 = new CreditsInScientificArea();
		ICreditsInScientificArea cisa2 = new CreditsInScientificArea();
		cisa1.setIdInternal(1);
		cisa2.setIdInternal(2);
		
		cisa1.setStudentCurricularPlan(scp);
		cisa2.setStudentCurricularPlan(scp);
		
		creditsInScientificAreas = new ArrayList<ICreditsInScientificArea>();
		creditsInScientificAreas.add(cisa1);
		creditsInScientificAreas.add(cisa2);
		enrolmentA.addCreditsInScientificAreas(cisa1);
		enrolmentA.addCreditsInScientificAreas(cisa2);
		
		
		/*
		 * CurricularCourse
		 */
		ICurricularCourse cc = new CurricularCourse();
		cc.setIdInternal(1);
		enrolmentA.setCurricularCourse(cc);
		
		
		
		
		
		
		IEquivalentEnrolmentForEnrolmentEquivalence eeee1 = new EquivalentEnrolmentForEnrolmentEquivalence();
		IEquivalentEnrolmentForEnrolmentEquivalence eeee2 = new EquivalentEnrolmentForEnrolmentEquivalence();
		IEquivalentEnrolmentForEnrolmentEquivalence eeee3 = new EquivalentEnrolmentForEnrolmentEquivalence();
		IEnrolmentEquivalence eeq1 = new EnrolmentEquivalence();
		IEnrolmentEquivalence eeq2 = new EnrolmentEquivalence();
		eeee1.setIdInternal(1);
		eeee2.setIdInternal(2);
		eeee3.setIdInternal(3);
		eeq1.setIdInternal(1);
		eeq2.setIdInternal(2);

		equivalentEnrolmentsA = new ArrayList<IEquivalentEnrolmentForEnrolmentEquivalence>();
		equivalentEnrolmentsB = new ArrayList<IEquivalentEnrolmentForEnrolmentEquivalence>();
		equivalentEnrolmentsA.add(eeee1);
		equivalentEnrolmentsB.add(eeee2);
		equivalentEnrolmentsA.add(eeee3);
		enrolmentA.addEquivalentEnrolmentForEnrolmentEquivalences(eeee1);
		enrolmentB.addEquivalentEnrolmentForEnrolmentEquivalences(eeee2);
		enrolmentA.addEquivalentEnrolmentForEnrolmentEquivalences(eeee3);
		
		eeee1.setEnrolmentEquivalence(eeq1);
		eeee2.setEnrolmentEquivalence(eeq1);
		eeee3.setEnrolmentEquivalence(eeq2);
		eeq1.setEnrolment(enrolmentC);
		eeq2.setEnrolment(enrolmentD);
		
		enrolmentEquivalencesC = new ArrayList<IEnrolmentEquivalence>();
		enrolmentEquivalencesD = new ArrayList<IEnrolmentEquivalence>();
		enrolmentEquivalencesC.add(eeq1);
		enrolmentEquivalencesD.add(eeq2);

		
		enrolmentE = new Enrolment();
		enrolmentF = new Enrolment();
		enrolmentE.setIdInternal(5);
		enrolmentF.setIdInternal(6);
		
		IEnrolmentEvaluation ee3 = new EnrolmentEvaluation();
		IEnrolmentEvaluation ee4 = new EnrolmentEvaluation();
		ee3.setIdInternal(3);
		ee4.setIdInternal(4);
		
		ee3.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		ee3.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		
		ee4.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
		ee4.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		ee4.setGrade("20");
		
		enrolmentE.addEvaluations(ee3);
		enrolmentF.addEvaluations(ee4);
		
		
		
		enrolmentWithImprovement = new Enrolment();
		enrolmentWithoutImprovement = new Enrolment();
		enrolmentWithImprovement.setIdInternal(7);
		enrolmentWithoutImprovement.setIdInternal(8);
		
		IEnrolmentEvaluation normalEvaluation = new EnrolmentEvaluation();
		IEnrolmentEvaluation normalEvaluationToImprove = new EnrolmentEvaluation();
		improvementEvaluation = new EnrolmentEvaluation();
		normalEvaluation.setIdInternal(5);
		normalEvaluationToImprove.setIdInternal(6);
		improvementEvaluation.setIdInternal(7);
		
		normalEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		normalEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		
		normalEvaluationToImprove.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
		normalEvaluationToImprove.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		
		improvementEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		improvementEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
		
		normalEvaluation.setEnrolment(enrolmentWithoutImprovement);
		normalEvaluationToImprove.setEnrolment(enrolmentWithImprovement);
		improvementEvaluation.setEnrolment(enrolmentWithImprovement);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDelete() {
		
		enrolmentD.delete();
		
		
		assertFalse(enrolmentD.hasAnyEnrolmentEquivalences());
		
		for (IEnrolmentEquivalence equivalence : enrolmentEquivalencesD) {
			assertFalse(equivalence.hasAnyEquivalenceRestrictions());
			assertFalse(equivalence.hasEnrolment());
		}
		
		assertTrue(enrolmentA.hasAnyEvaluations());
		assertTrue(enrolmentA.hasExecutionPeriod());
		assertTrue(enrolmentA.hasStudentCurricularPlan());
		assertTrue(enrolmentA.hasAnyCreditsInAnySecundaryAreas());
		assertTrue(enrolmentA.hasAnyCreditsInScientificAreas());
		assertTrue(enrolmentA.hasCurricularCourse());
		assertFalse(enrolmentA.hasAnyEnrolmentEquivalences());
		assertTrue(enrolmentA.hasAnyEquivalentEnrolmentForEnrolmentEquivalences());
		

		
		
			
		enrolmentA.delete();
		
		assertFalse(enrolmentA.hasAnyEvaluations());
		assertFalse(enrolmentA.hasExecutionPeriod());
		assertFalse(enrolmentA.hasStudentCurricularPlan());
		assertFalse(enrolmentA.hasAnyCreditsInAnySecundaryAreas());
		assertFalse(enrolmentA.hasAnyCreditsInScientificAreas());
		assertFalse(enrolmentA.hasCurricularCourse());
		assertFalse(enrolmentA.hasAnyEnrolmentEquivalences());
		assertFalse(enrolmentA.hasAnyEquivalentEnrolmentForEnrolmentEquivalences());
		
		for (IEnrolmentEvaluation eval : evaluations) {
			assertFalse(eval.hasPersonResponsibleForGrade());
			assertFalse(eval.hasEmployee());
		}
		
		for (ICreditsInAnySecundaryArea credits : creditsInAnySecundaryAreas)
			assertFalse(credits.hasStudentCurricularPlan());
		
		for (ICreditsInScientificArea credits : creditsInScientificAreas)
			assertFalse(credits.hasStudentCurricularPlan());
		
		assertFalse(enrolmentC.hasAnyEnrolmentEquivalences());
		assertFalse(enrolmentC.hasAnyEquivalentEnrolmentForEnrolmentEquivalences());
		
		for (IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolment : equivalentEnrolmentsA) {
			assertFalse(equivalentEnrolment.hasEquivalentEnrolment());
			assertFalse(equivalentEnrolment.hasEnrolmentEquivalence());
		}
		
		for (IEnrolmentEquivalence equivalence : enrolmentEquivalencesC) {
			assertFalse(equivalence.hasEnrolment());
			assertFalse(equivalence.hasAnyEquivalenceRestrictions());
		}
	}
	
	
	
	public void testUnEnroll() {
		try {
			enrolmentE.unEnroll();
			
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		
		
		try {
			enrolmentF.unEnroll();
			fail("Should not have been deleted.");
		} catch (DomainException e) {
			
		}
	}
	
	
	public void testGetImprovementEvaluation() {
		
		assertNull(enrolmentWithoutImprovement.getImprovementEvaluation());
		assertTrue(enrolmentWithImprovement.getImprovementEvaluation().equals(improvementEvaluation));	
	}

}
