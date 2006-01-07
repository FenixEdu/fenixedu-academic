package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;



public class CurricularCourseScopeTest extends DomainTestBase {

	private CurricularCourseScope newCurricularCourseScope;
	private CurricularSemester newCurricularSemester;
	private CurricularCourse newCurricularCourse;
	private Branch newBranch;
	
	private CurricularSemester commonCurricularSemester;
	private CurricularCourse commonCurricularCourse;
	private Branch commonBranch;
	
	private Calendar newBeginDate;
	private Calendar newEndDate;
	private String newAnnotation;
	
	CurricularCourseScope scopeToEdit;
	
	private CurricularCourseScope curricularCourseScopeToDelete;
	private CurricularCourseScope curricularCourseScopeNotToDelete;
	
	protected void setUp() throws Exception {
        super.setUp();

		
		// common initialization for 'edit' and 'create' method tests
		newCurricularSemester = new CurricularSemester();
		newCurricularCourse = new CurricularCourse();
		newBranch = new Branch();
		
		newBeginDate = new GregorianCalendar();
		newEndDate = new GregorianCalendar();
		newAnnotation = "annotation";
		
		// common initialization for 'edit' and 'end' method tests
		setUpEdit();
    }
	
	
	private void setUpCreate() {

		commonCurricularSemester = new CurricularSemester();
		commonCurricularSemester.setSemester(2);
		commonCurricularCourse = new CurricularCourse();
		commonBranch = new Branch();
		
		CurricularCourseScope otherCurricularCourseScope = new CurricularCourseScope();
		otherCurricularCourseScope.setCurricularCourse(commonCurricularCourse);
		otherCurricularCourseScope.setCurricularSemester(commonCurricularSemester);
		otherCurricularCourseScope.setBranch(commonBranch);
	}
	
	
	
	private void setUpEdit() {
		
		scopeToEdit = new CurricularCourseScope();
		
	}
	
	
	private void setUpDelete() {
		
		curricularCourseScopeToDelete = new CurricularCourseScope();
		curricularCourseScopeNotToDelete = new CurricularCourseScope();
		
		CurricularCourse cc1 = new CurricularCourse();
		
		CurricularSemester cs1 = new CurricularSemester();
		
		WrittenEvaluation we1 = new WrittenEvaluation();
		WrittenEvaluation we2 = new WrittenEvaluation();
		
		curricularCourseScopeToDelete.setCurricularCourse(cc1);
		curricularCourseScopeToDelete.setCurricularSemester(cs1);
		
		curricularCourseScopeNotToDelete.setCurricularCourse(cc1);
		curricularCourseScopeNotToDelete.setCurricularSemester(cs1);
		curricularCourseScopeNotToDelete.addAssociatedWrittenEvaluations(we1);
		curricularCourseScopeNotToDelete.addAssociatedWrittenEvaluations(we2);
	}


	public void testCreate() {

		setUpCreate();
		
		try {
			newCurricularCourseScope = new CurricularCourseScope(newBranch, newCurricularCourse, newCurricularSemester,
																newBeginDate, newEndDate, newAnnotation);
		} catch (DomainException e) {
			fail("Should have been created.");
		}
		
		
		assertTrue("Failed to assign CurricularSemester", newCurricularCourseScope.hasCurricularSemester());
		assertTrue("Failed to assign CurricularCourse", newCurricularCourseScope.hasCurricularCourse());
		assertTrue("Failed to assign Branch", newCurricularCourseScope.hasBranch());
		assertTrue("Failed to assign beginDate", newCurricularCourseScope.getBeginDate().equals(newBeginDate));
		assertTrue("Failed to assign endDate", newCurricularCourseScope.getEndDate().equals(newEndDate));
		assertTrue("Failed to assign anotation", newCurricularCourseScope.getAnotation().equals(newAnnotation));
		
		
		try {
			new CurricularCourseScope(commonBranch, commonCurricularCourse, commonCurricularSemester, null, null, null);
			fail("Should not have been created.");
		} catch (DomainException e) {
		}
		
	}
	
	
	public void testEdit() {
		
		scopeToEdit.edit(newBranch, newCurricularSemester, newBeginDate, null, newAnnotation);
		
		assertTrue("Edited Branch does not match expected", scopeToEdit.getBranch().equals(newBranch));
		assertTrue("Edited CurricularSemester does not match expected", scopeToEdit.getCurricularSemester().equals(newCurricularSemester));
		assertTrue("Edited beginDate does not match expected", scopeToEdit.getBeginDate().equals(newBeginDate));
		assertNull("Edited endDate does not match expected", scopeToEdit.getEndDate());
		assertTrue("Edited anotation does not match expected", scopeToEdit.getAnotation().equals(newAnnotation));
	}
	
	
	public void testEnd() {
		
		scopeToEdit.end(newEndDate);
		
		assertTrue("Failed to set endDate", scopeToEdit.getEndDate().equals(newEndDate));
	}
	
	
	public void testDelete() {
		
		setUpDelete();
		
		try {
			curricularCourseScopeNotToDelete.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		
		assertTrue("Should not have dereferenced from CurricularCourse", curricularCourseScopeNotToDelete.hasCurricularCourse());
		assertTrue("Should not have dereferenced from CurricularSemester", curricularCourseScopeNotToDelete.hasCurricularSemester());
		assertTrue("Should not have dereferenced from WrittenEvaluations", curricularCourseScopeNotToDelete.hasAnyAssociatedWrittenEvaluations());
		
		try {
			curricularCourseScopeToDelete.delete();
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		
		assertFalse("Failed to dereference CurricularCourse", curricularCourseScopeToDelete.hasCurricularCourse());
		assertFalse("Failed to dereference CurricularSemester", curricularCourseScopeToDelete.hasCurricularSemester());
		assertFalse("Failed to dereference WrittenEvaluations", curricularCourseScopeToDelete.hasAnyAssociatedWrittenEvaluations());
	}

}
