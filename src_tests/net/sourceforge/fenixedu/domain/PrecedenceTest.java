package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;

public class PrecedenceTest extends DomainTestBase {

	private IPrecedence precedenceWithRestrictionDoneCurricularCourse;
	private IPrecedence precedenceWithRestrictionDoneOrHasBeenEnroledInCurricularCourse;
	private IPrecedence precedenceWithRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse;
	private IPrecedence precedenceWithRestrictionHasEverBeenOrWillBeAbleToBeEnroledInCurricularCourse;
	private IPrecedence precedenceWithRestrictionNotDoneCurricularCourse;
	private IPrecedence precedenceWithRestrictionNotEnroledInCurricularCourse;
	private IPrecedence precedenceWithRestrictionByNumberOfDoneCurricularCourses;
	private IPrecedence precedenceWithRestrictionPeriodToAply;
	
	private IPrecedence precedenceToDelete;
	
	private IPrecedence destinyPrecedence;
	private IPrecedence sourcePrecedence;
	
	private IRestriction restrictionToMerge1;
	private IRestriction restrictionToMerge2;
	private IRestriction restrictionToMerge3;
	
	private ICurricularCourse curricularCourse;
	private ICurricularCourse precedentCurricularCourse;
		
	
	protected void setUp() throws Exception {
        super.setUp();
	
		setUpCreate();
		setUpDelete();
		setUpMergePrecedences();
    }

	
	private void setUpCreate() {
		
		curricularCourse = new CurricularCourse();
		precedentCurricularCourse = new CurricularCourse();
	}
	
	
	private void setUpDelete() {
		
		precedenceToDelete = new Precedence();
		
		IRestriction restriction = new RestrictionByNumberOfDoneCurricularCourses();
		IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
			
		precedenceToDelete.addRestrictions(restriction);
		precedenceToDelete.addRestrictions(restrictionByCurricularCourse);
		restrictionByCurricularCourse.setPrecedentCurricularCourse(curricularCourse);
		precedenceToDelete.setCurricularCourse(curricularCourse);
	}
	
	private void setUpMergePrecedences() {
		
		destinyPrecedence = new Precedence();
		sourcePrecedence = new Precedence();
		
		restrictionToMerge1 = new RestrictionDoneCurricularCourse();
		restrictionToMerge2 = new RestrictionDoneCurricularCourse();
		restrictionToMerge3 = new RestrictionDoneCurricularCourse();

		destinyPrecedence.setCurricularCourse(curricularCourse);
		sourcePrecedence.setCurricularCourse(curricularCourse);
		
		restrictionToMerge1.setPrecedence(destinyPrecedence);
		restrictionToMerge2.setPrecedence(sourcePrecedence);
		restrictionToMerge3.setPrecedence(sourcePrecedence);
	}
	
	
	
    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	
	public void testCreate() {
		precedenceWithRestrictionDoneCurricularCourse = new Precedence(curricularCourse, 
				RestrictionDoneCurricularCourse.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionDoneOrHasBeenEnroledInCurricularCourse = new Precedence(curricularCourse, 
				RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse = new Precedence(curricularCourse,
				RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionHasEverBeenOrWillBeAbleToBeEnroledInCurricularCourse = new Precedence(curricularCourse,
				RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionNotDoneCurricularCourse = new Precedence(curricularCourse, 
				RestrictionNotDoneCurricularCourse.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionNotEnroledInCurricularCourse = new Precedence(curricularCourse, 
				RestrictionNotEnrolledInCurricularCourse.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionByNumberOfDoneCurricularCourses = new Precedence(curricularCourse, 
				RestrictionByNumberOfDoneCurricularCourses.class.getName(), precedentCurricularCourse, 1);
		
		precedenceWithRestrictionPeriodToAply = new Precedence(curricularCourse, 
				RestrictionPeriodToApply.class.getName(), precedentCurricularCourse, 1);
		
		
		assertTrue(precedenceWithRestrictionDoneCurricularCourse.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionDoneCurricularCourse.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionDoneOrHasBeenEnroledInCurricularCourse.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionDoneOrHasBeenEnroledInCurricularCourse.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionHasEverBeenOrWillBeAbleToBeEnroledInCurricularCourse.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionHasEverBeenOrWillBeAbleToBeEnroledInCurricularCourse.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionNotDoneCurricularCourse.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionNotDoneCurricularCourse.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionNotEnroledInCurricularCourse.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionNotEnroledInCurricularCourse.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionByNumberOfDoneCurricularCourses.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionByNumberOfDoneCurricularCourses.hasAnyRestrictions());
		
		assertTrue(precedenceWithRestrictionPeriodToAply.hasCurricularCourse());
		assertTrue(precedenceWithRestrictionPeriodToAply.hasAnyRestrictions());
		
	}
	
	public void testDelete() {
		
		precedenceToDelete.delete();
		
		assertFalse(precedenceToDelete.hasCurricularCourse());
		assertFalse(precedenceToDelete.hasAnyRestrictions());
	}
	
	
	public void testMergePrecedences() {
		
		destinyPrecedence.mergePrecedences(sourcePrecedence);
		
		assertTrue(restrictionToMerge1.getPrecedence().equals(destinyPrecedence));
		assertTrue(restrictionToMerge2.getPrecedence().equals(destinyPrecedence));
		assertTrue(restrictionToMerge3.getPrecedence().equals(destinyPrecedence));
		
		assertTrue(destinyPrecedence.hasCurricularCourse());
		assertFalse(sourcePrecedence.hasCurricularCourse());
		assertFalse(sourcePrecedence.hasAnyRestrictions());
	}
}
