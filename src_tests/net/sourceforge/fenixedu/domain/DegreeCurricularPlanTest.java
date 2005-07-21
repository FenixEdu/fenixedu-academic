package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
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
		
		setUpCreate();
		setUpEdit();
		setUpDelete();
    }
	
	
	private void setUpCreate() {
		
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
		
		IDegree degree = new Degree();
		degree.setConcreteClassForDegreeCurricularPlans(DegreeCurricularPlan.class.getName());
		
		newDegreeCurricularPlan = new DegreeCurricularPlan(degree, newName, newState, newInicialDate, newEndDate, newDegreeDuration,
															newMinimalYearForOptionalCourses, newNeededCredits, newMarkType,
															newNumerusClausus, newAnnotation);
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
		
		IEnrolmentPeriod enrolmentPeriod1 = new EnrolmentPeriodInCurricularCourses();
		IEnrolmentPeriod enrolmentPeriod2 = new EnrolmentPeriodInCurricularCourses();
		enrolmentPeriod1.setDegreeCurricularPlan(dcpWithAll);
		enrolmentPeriod2.setDegreeCurricularPlan(dcpWithEnrolmentPeriods);
		
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
	
	
	
	

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	
	public void testCreate() {
		
		assertTrue(newDegreeCurricularPlan.hasDegree());
		assertTrue(newDegreeCurricularPlan.getName().equals(newName));
		assertTrue(newDegreeCurricularPlan.getState().equals(newState));
		assertTrue(newDegreeCurricularPlan.getInitialDate().equals(newInicialDate));
		assertTrue(newDegreeCurricularPlan.getEndDate().equals(newEndDate));
		assertTrue(newDegreeCurricularPlan.getDegreeDuration().equals(newDegreeDuration));
		assertTrue(newDegreeCurricularPlan.getMinimalYearForOptionalCourses().equals(newMinimalYearForOptionalCourses));
		assertTrue(newDegreeCurricularPlan.getNeededCredits().equals(newNeededCredits));
		assertTrue(newDegreeCurricularPlan.getMarkType().equals(newMarkType));
		assertTrue(newDegreeCurricularPlan.getNumerusClausus().equals(newNumerusClausus));
		assertTrue(newDegreeCurricularPlan.getAnotation().equals(newAnnotation));
		assertTrue(newDegreeCurricularPlan.getConcreteClassForStudentCurricularPlans().equals(DegreeCurricularPlan.class.getName()));
	}
	
	public void testEdit() {
		
		degreeCurricularPlanToEdit.edit(newName, newState, newInicialDate, newEndDate, newDegreeDuration, 
				newMinimalYearForOptionalCourses, newNeededCredits, newMarkType,
				newNumerusClausus, newAnnotation);

		assertTrue(degreeCurricularPlanToEdit.getName().equals(newName));
		assertTrue(degreeCurricularPlanToEdit.getState().equals(newState));
		assertTrue(degreeCurricularPlanToEdit.getInitialDate().equals(newInicialDate));
		assertTrue(degreeCurricularPlanToEdit.getEndDate().equals(newEndDate));
		assertTrue(degreeCurricularPlanToEdit.getDegreeDuration().equals(newDegreeDuration));
		assertTrue(degreeCurricularPlanToEdit.getMinimalYearForOptionalCourses().equals(newMinimalYearForOptionalCourses));
		assertTrue(degreeCurricularPlanToEdit.getNeededCredits().equals(newNeededCredits));
		assertTrue(degreeCurricularPlanToEdit.getMarkType().equals(newMarkType));
		assertTrue(degreeCurricularPlanToEdit.getNumerusClausus().equals(newNumerusClausus));
		assertTrue(degreeCurricularPlanToEdit.getAnotation().equals(newAnnotation));
	}
	
	
	public void testDelete() {
		
		
		try {
			dcpToDelete.delete();
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		assertFalse(dcpToDelete.hasDegree());
		
		
		
		
		try {
			dcpWithAll.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithAll.hasDegree());
		assertTrue(dcpWithAll.hasAnyStudentCurricularPlans());
		assertTrue(dcpWithAll.hasAnyCurricularCourseEquivalences());
		assertTrue(dcpWithAll.hasAnyEnrolmentPeriods());
		assertTrue(dcpWithAll.hasAnyCurricularCourses());
		assertTrue(dcpWithAll.hasAnyExecutionDegrees());
		assertTrue(dcpWithAll.hasAnyAreas());
		
		
		
		
		try {
			dcpWithStudentCurricularPlans.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithStudentCurricularPlans.hasDegree());
		assertTrue(dcpWithStudentCurricularPlans.hasAnyStudentCurricularPlans());
		
		
		
		
		try {
			dcpWithCurricularCourseEquivalences.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithCurricularCourseEquivalences.hasDegree());
		assertTrue(dcpWithCurricularCourseEquivalences.hasAnyCurricularCourseEquivalences());
		
		
		
		
		try {
			dcpWithEnrolmentPeriods.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithEnrolmentPeriods.hasDegree());
		assertTrue(dcpWithEnrolmentPeriods.hasAnyEnrolmentPeriods());
		
		
		
		
		try {
			dcpWithCurricularCourses.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithCurricularCourses.hasDegree());
		assertTrue(dcpWithCurricularCourses.hasAnyCurricularCourses());
		
		
		
		
		try {
			dcpWithExecutionDegrees.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithExecutionDegrees.hasDegree());
		assertTrue(dcpWithExecutionDegrees.hasAnyExecutionDegrees());
		
		
		
		
		try {
			dcpWithBranches.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		assertTrue(dcpWithBranches.hasDegree());
		assertTrue(dcpWithBranches.hasAnyAreas());
	}
}