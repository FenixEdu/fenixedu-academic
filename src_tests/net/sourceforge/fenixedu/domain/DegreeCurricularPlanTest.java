package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MarkType;


public class DegreeCurricularPlanTest extends DomainTestBase {

	IDegreeCurricularPlan newDegreeCurricularPlan;
	IDegreeCurricularPlan degreeCurricularPlanToEdit;
	String newName;
	DegreeCurricularPlanState newState;
	Date newInicialDate;
	Date newEndDate;
	Integer newDegreeDuration;
	Integer newMinimalYearForOptionalCourses; 
	Double newNeededCredits;
	MarkType newMarkType; 
	Integer newNumerusClausus;
	String newAnnotation;
	
	IDegreeCurricularPlan dcpToDelete;
	IDegreeCurricularPlan dcpWithAll;
	IDegreeCurricularPlan dcpWithStudentCurricularPlans;
	IDegreeCurricularPlan dcpWithCurricularCourseEquivalences;
	IDegreeCurricularPlan dcpWithEnrolmentPeriods;
	IDegreeCurricularPlan dcpWithCurricularCourses;
	IDegreeCurricularPlan dcpWithExecutionDegrees;
	IDegreeCurricularPlan dcpWithBranches;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		// common initialization for 'create' and 'edit' method tests
		newName = "name";
		newState = DegreeCurricularPlanState.ACTIVE;
		newInicialDate = new Date();
		newEndDate = new Date();
		newDegreeDuration = 1;
		newMinimalYearForOptionalCourses = 1;
		newNeededCredits = 1.0;
		newMarkType = MarkType.TYPE20_OBJ;
		newNumerusClausus = 1;
		newAnnotation = "annotation";
	}
	
	private void setUpCreate() {
		
		IDegree degree = new Degree();
		degree.setConcreteClassForDegreeCurricularPlans(DegreeCurricularPlan.class.getName());
		
		newDegreeCurricularPlan = new DegreeCurricularPlan(degree, newName, newState, newInicialDate, newEndDate, newDegreeDuration,
															newMinimalYearForOptionalCourses, newNeededCredits, newMarkType,
															newNumerusClausus, newAnnotation, CurricularStage.OLD);
	}
	
	
	private void setUpEdit() {
		degreeCurricularPlanToEdit = new DegreeCurricularPlan();
	}
	
	
	private void setUpDelete() {
		dcpToDelete = new DegreeCurricularPlan();
		dcpWithAll = new DegreeCurricularPlan();
		dcpWithStudentCurricularPlans = new DegreeCurricularPlan();
		dcpWithCurricularCourseEquivalences = new DegreeCurricularPlan();
		dcpWithEnrolmentPeriods = new DegreeCurricularPlan();
		dcpWithCurricularCourses = new DegreeCurricularPlan();
		dcpWithExecutionDegrees = new DegreeCurricularPlan();
		dcpWithBranches = new DegreeCurricularPlan();
		
		IDegree degree = new Degree();
		degree.addDegreeCurricularPlans(dcpToDelete);
		degree.addDegreeCurricularPlans(dcpWithAll);
		degree.addDegreeCurricularPlans(dcpWithStudentCurricularPlans);
		degree.addDegreeCurricularPlans(dcpWithCurricularCourseEquivalences);
		degree.addDegreeCurricularPlans(dcpWithEnrolmentPeriods);
		degree.addDegreeCurricularPlans(dcpWithCurricularCourses);
		degree.addDegreeCurricularPlans(dcpWithExecutionDegrees);
		degree.addDegreeCurricularPlans(dcpWithBranches);
		
		IStudentCurricularPlan studentCurricularPlan1 = new StudentCurricularPlan();
		IStudentCurricularPlan studentCurricularPlan2 = new StudentCurricularPlan();
		studentCurricularPlan1.setDegreeCurricularPlan(dcpWithAll);
		studentCurricularPlan2.setDegreeCurricularPlan(dcpWithStudentCurricularPlans);
		
		ICurricularCourseEquivalence curricularCourseEquivalence1 = new CurricularCourseEquivalence();
		ICurricularCourseEquivalence curricularCourseEquivalence2 = new CurricularCourseEquivalence();
		curricularCourseEquivalence1.setDegreeCurricularPlan(dcpWithAll);
		curricularCourseEquivalence2.setDegreeCurricularPlan(dcpWithCurricularCourseEquivalences);
		
		IEnrolmentPeriod enrolmentPeriod1 = new EnrolmentPeriodInCurricularCourses(dcpWithAll, null, null, null);
		IEnrolmentPeriod enrolmentPeriod2 = new EnrolmentPeriodInCurricularCourses(dcpWithEnrolmentPeriods, null, null, null);
		
		ICurricularCourse curricularCourse1 = new CurricularCourse();
		ICurricularCourse curricularCourse2 = new CurricularCourse();
		curricularCourse1.setDegreeCurricularPlan(dcpWithAll);
		curricularCourse2.setDegreeCurricularPlan(dcpWithCurricularCourses);
		
		IExecutionDegree executionDegree1 = new ExecutionDegree();
		IExecutionDegree executionDegree2 = new ExecutionDegree();
		executionDegree1.setDegreeCurricularPlan(dcpWithAll);
		executionDegree2.setDegreeCurricularPlan(dcpWithExecutionDegrees);
		
		IBranch branch1 = new Branch();
		IBranch branch2 = new Branch();
		branch1.setDegreeCurricularPlan(dcpWithAll);
		branch2.setDegreeCurricularPlan(dcpWithBranches);
	}

	
	public void testCreate() {

		setUpCreate();
		
		assertTrue("Failed to assign property on creation: Degree", newDegreeCurricularPlan.hasDegree());
		assertTrue("Failed to assign property on creation: concreteClassForStudentCurricularPlans", newDegreeCurricularPlan.getConcreteClassForStudentCurricularPlans().equals(DegreeCurricularPlan.class.getName()));

		assertCorrectInitialization("Failed to assign property on creation: ", newDegreeCurricularPlan,newName,newState,newInicialDate,newEndDate,newDegreeDuration,
				newMinimalYearForOptionalCourses,newNeededCredits,newMarkType,newNumerusClausus,newAnnotation);	
	}
	
	public void testEdit() {

		setUpEdit();
		
		degreeCurricularPlanToEdit.edit(newName, newState, newInicialDate, newEndDate, newDegreeDuration, 
				newMinimalYearForOptionalCourses, newNeededCredits, newMarkType,
				newNumerusClausus, newAnnotation);

		assertCorrectInitialization("Failed to assign property on edit: ", degreeCurricularPlanToEdit,newName,newState,newInicialDate,newEndDate,newDegreeDuration,
				newMinimalYearForOptionalCourses,newNeededCredits,newMarkType,newNumerusClausus,newAnnotation);	
	}
	
	
	public void testDelete() {

		setUpDelete();
		
		try {
			dcpToDelete.delete();
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		assertFalse("Failed to dereference Degree", dcpToDelete.hasDegree());
		
		
		
		
		try {
			dcpWithAll.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithAll.hasDegree());
		assertTrue("Should not dereference StudentCurricularPlans", dcpWithAll.hasAnyStudentCurricularPlans());
		assertTrue("Should not dereference CurricularCourseEquivalences", dcpWithAll.hasAnyCurricularCourseEquivalences());
		assertTrue("Should not dereference EnrolmentPeriods", dcpWithAll.hasAnyEnrolmentPeriods());
		assertTrue("Should not dereference CurricularCourses", dcpWithAll.hasAnyCurricularCourses());
		assertTrue("Should not dereference ExecutionDegrees", dcpWithAll.hasAnyExecutionDegrees());
		assertTrue("Should not dereference Branches", dcpWithAll.hasAnyAreas());
		
		
		
		
		try {
			dcpWithStudentCurricularPlans.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithStudentCurricularPlans.hasDegree());
		assertTrue("Should not dereference StudentCurricularPlans", dcpWithStudentCurricularPlans.hasAnyStudentCurricularPlans());
		
		
		
		
		try {
			dcpWithCurricularCourseEquivalences.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithCurricularCourseEquivalences.hasDegree());
		assertTrue("Should not dereference CurricularCourseEquivalences", dcpWithCurricularCourseEquivalences.hasAnyCurricularCourseEquivalences());
		
		
		
		
		try {
			dcpWithEnrolmentPeriods.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithEnrolmentPeriods.hasDegree());
		assertTrue("Should not dereference EnrolmentPeriods", dcpWithEnrolmentPeriods.hasAnyEnrolmentPeriods());
		
		
		
		
		try {
			dcpWithCurricularCourses.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithCurricularCourses.hasDegree());
		assertTrue("Should not dereference CurricularCourses", dcpWithCurricularCourses.hasAnyCurricularCourses());
		
		
		
		
		try {
			dcpWithExecutionDegrees.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithExecutionDegrees.hasDegree());
		assertTrue("Should not dereference ExecutionDegrees", dcpWithExecutionDegrees.hasAnyExecutionDegrees());
		
		
		
		
		try {
			dcpWithBranches.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue("Should not dereference Degree", dcpWithBranches.hasDegree());
		assertTrue("Should not dereference Branches", dcpWithBranches.hasAnyAreas());
	}
	
	
	private void assertCorrectInitialization(String errorMessagePrefix, IDegreeCurricularPlan dcp, String name, DegreeCurricularPlanState state,
			Date initialDate, Date endDate, Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
			MarkType markType, Integer numerusClausus, String annotation) {
		assertTrue(errorMessagePrefix + ": name", dcp.getName().equals(name));
		assertTrue(errorMessagePrefix + ": state", dcp.getState().equals(state));
		assertTrue(errorMessagePrefix + ": initialDate", dcp.getInitialDate().equals(initialDate));
		assertTrue(errorMessagePrefix + ": endDate", dcp.getEndDate().equals(endDate));
		assertTrue(errorMessagePrefix + ": degreeDuration", dcp.getDegreeDuration().equals(degreeDuration));
		assertTrue(errorMessagePrefix + ": minimalYearForOptionalCourses", dcp.getMinimalYearForOptionalCourses().equals(minimalYearForOptionalCourses));
		assertTrue(errorMessagePrefix + ": neededCredits", dcp.getNeededCredits().equals(neededCredits));
		assertTrue(errorMessagePrefix + ": markType", dcp.getMarkType().equals(markType));
		assertTrue(errorMessagePrefix + ": numerusClausus", dcp.getNumerusClausus().equals(numerusClausus));
		assertTrue(errorMessagePrefix + ": annotation", dcp.getAnotation().equals(annotation));
	}
}