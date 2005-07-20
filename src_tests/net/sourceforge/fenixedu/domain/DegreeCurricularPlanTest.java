package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
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
	
	protected void setUp() throws Exception {
        super.setUp();
		
		setUpCreate();
		
		setUpEdit();
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

}