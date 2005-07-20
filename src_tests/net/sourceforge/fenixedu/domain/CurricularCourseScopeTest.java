package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;



public class CurricularCourseScopeTest extends DomainTestBase {

	private ICurricularCourseScope newCurricularCourseScope;
	private ICurricularSemester newCurricularSemester;
	private ICurricularCourse newCurricularCourse;
	private IBranch newBranch;
	
	private ICurricularCourseScope unableToCreateCurricularCourseScope;
	private ICurricularSemester commonCurricularSemester;
	private ICurricularCourse commonCurricularCourse;
	private IBranch commonBranch;
	
	private Calendar newBeginDate;
	private Calendar newEndDate;
	private String newAnnotation;
	
	private ICurricularCourseScope curricularCourseScopeToDelete;
	private ICurricularCourseScope curricularCourseScopeNotToDelete;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		setUpCreate();
		
		setUpDelete();
    }
	
	
	private void setUpCreate() {
		
		newCurricularSemester = new CurricularSemester();
		newCurricularCourse = new CurricularCourse();
		newBranch = new Branch();
		
		newBeginDate = new GregorianCalendar();
		newEndDate = new GregorianCalendar();
		newAnnotation = "annotation";
		

		commonCurricularSemester = new CurricularSemester();
		commonCurricularCourse = new CurricularCourse();
		commonBranch = new Branch();
		
		ICurricularCourseScope otherCurricularCourseScope = new CurricularCourseScope();
		otherCurricularCourseScope.setCurricularCourse(commonCurricularCourse);
		otherCurricularCourseScope.setCurricularSemester(commonCurricularSemester);
		otherCurricularCourseScope.setBranch(commonBranch);
		

	}
	
	
	private void setUpDelete() {
		
		curricularCourseScopeToDelete = new CurricularCourseScope();
		curricularCourseScopeToDelete.setIdInternal(1);
		curricularCourseScopeNotToDelete = new CurricularCourseScope();
		curricularCourseScopeNotToDelete.setIdInternal(2);
		
		ICurricularCourse cc1 = new CurricularCourse();
		
		ICurricularSemester cs1 = new CurricularSemester();
		
		IWrittenEvaluation we1 = new WrittenEvaluation();
		IWrittenEvaluation we2 = new WrittenEvaluation();
		
		curricularCourseScopeToDelete.setCurricularCourse(cc1);
		curricularCourseScopeToDelete.setCurricularSemester(cs1);
		
		curricularCourseScopeNotToDelete.setCurricularCourse(cc1);
		curricularCourseScopeNotToDelete.setCurricularSemester(cs1);
		curricularCourseScopeNotToDelete.addAssociatedWrittenEvaluations(we1);
		curricularCourseScopeNotToDelete.addAssociatedWrittenEvaluations(we2);
	}

	
	
	
	
    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	
	
	
	
	public void testCreate() {
		
		try {
			newCurricularCourseScope = new CurricularCourseScope(newBranch, newCurricularCourse, newCurricularSemester,
																newBeginDate, newEndDate, newAnnotation);
		} catch (RuntimeException e) {
			fail("Should have been deleted.");
		}
		
		
		assertTrue(newCurricularCourseScope.hasCurricularSemester());
		assertTrue(newCurricularCourseScope.hasCurricularCourse());
		assertTrue(newCurricularCourseScope.hasBranch());
		assertTrue(newCurricularCourseScope.getBeginDate().equals(newBeginDate));
		assertTrue(newCurricularCourseScope.getEndDate().equals(newEndDate));
		assertTrue(newCurricularCourseScope.getAnotation().equals(newAnnotation));
		
		
		try {
			unableToCreateCurricularCourseScope = new CurricularCourseScope(commonBranch, commonCurricularCourse, 
																			commonCurricularSemester, null, null, null);
			fail("Should not have been created.");
		} catch (RuntimeException e) {
			
		}
		
	}
	
	public void testDelete() {
		try {
			curricularCourseScopeNotToDelete.delete();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		
		try {
			curricularCourseScopeToDelete.delete();
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		
		
		assertFalse(curricularCourseScopeToDelete.hasCurricularCourse());
		assertFalse(curricularCourseScopeToDelete.hasCurricularSemester());
		assertFalse(curricularCourseScopeToDelete.hasAnyAssociatedWrittenEvaluations());
		
		assertTrue(curricularCourseScopeNotToDelete.hasCurricularCourse());
		assertTrue(curricularCourseScopeNotToDelete.hasCurricularSemester());
		assertTrue(curricularCourseScopeNotToDelete.hasAnyAssociatedWrittenEvaluations());
	}

}
