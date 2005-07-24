package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByNumberOfCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionPeriodToApply;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;



public class RestrictionTest extends DomainTestBase {

	private IRestrictionByCurricularCourse newRestrictionDoneCurricularCourse;
	private IRestrictionByCurricularCourse newRestrictionDoneOrHasBeenEnroledInCurricularCourse;
	private IRestrictionByCurricularCourse newRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse;
	private IRestrictionByCurricularCourse newRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
	private IRestrictionByCurricularCourse newRestrictionNotDoneCurricularCourse;
	private IRestrictionByCurricularCourse newRestrictionNotEnroledCurricularCourse;
	private IRestrictionByNumberOfCurricularCourses newRestrictionByNumberOfDoneCurricularCourses;
	private IRestrictionPeriodToApply newRestrictionPeriodToApply;
	
	private IPrecedence newPrecedence;
	private ICurricularCourse newPrecedentCurricularCourse;
	private Integer newNumber;
		
	private IRestriction restriction;
	private IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse restrictionByCurricularCourse;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		setUpCreate();
		
		setUpDelete();
    }
	
	
	
	private void setUpCreate() {
		newPrecedence = new Precedence();
		newPrecedentCurricularCourse = new CurricularCourse();
		newNumber = 1;
	}
	
	
	
	private void setUpDelete() {
		restriction = new RestrictionByNumberOfDoneCurricularCourses();
		restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
		
		IPrecedence precedence = new Precedence();
		
		ICurricularCourse curricularCourse = new CurricularCourse();
		
		restriction.setPrecedence(precedence);
		restrictionByCurricularCourse.setPrecedence(precedence);
		restrictionByCurricularCourse.setPrecedentCurricularCourse(curricularCourse);
	}
	

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	
	
	public void testCreate() {
		newRestrictionDoneCurricularCourse = new RestrictionDoneCurricularCourse(newNumber, newPrecedence, 
				newPrecedentCurricularCourse);
		
		newRestrictionDoneOrHasBeenEnroledInCurricularCourse = new RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse(
				newNumber, newPrecedence, newPrecedentCurricularCourse);
		
		newRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse(
				newNumber, newPrecedence, newPrecedentCurricularCourse);
		
		newRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse = new RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse(
				newNumber, newPrecedence, newPrecedentCurricularCourse);
		
		newRestrictionNotDoneCurricularCourse = new RestrictionNotDoneCurricularCourse(newNumber, newPrecedence, 
				newPrecedentCurricularCourse);
		
		newRestrictionNotEnroledCurricularCourse = new RestrictionNotEnrolledInCurricularCourse(newNumber, newPrecedence, 
				newPrecedentCurricularCourse);
		
		newRestrictionByNumberOfDoneCurricularCourses = new RestrictionByNumberOfDoneCurricularCourses(newNumber, newPrecedence, 
				newPrecedentCurricularCourse);
		
		newRestrictionPeriodToApply = new RestrictionPeriodToApply(newNumber, newPrecedence, newPrecedentCurricularCourse);
		
		
		assertTrue(newRestrictionDoneCurricularCourse.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionDoneCurricularCourse.getPrecedentCurricularCourse().equals(newPrecedentCurricularCourse));
		
		assertTrue(newRestrictionDoneOrHasBeenEnroledInCurricularCourse.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionDoneOrHasBeenEnroledInCurricularCourse.getPrecedentCurricularCourse().equals(newPrecedentCurricularCourse));
		
		assertTrue(newRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse.getPrecedentCurricularCourse().equals(newPrecedentCurricularCourse));
		
		assertTrue(newRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.getPrecedentCurricularCourse().equals(newPrecedentCurricularCourse));
		
		assertTrue(newRestrictionNotDoneCurricularCourse.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionNotDoneCurricularCourse.getPrecedentCurricularCourse().equals(newPrecedentCurricularCourse));
		
		assertTrue(newRestrictionNotEnroledCurricularCourse.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionNotEnroledCurricularCourse.getPrecedentCurricularCourse().equals(newPrecedentCurricularCourse));
		
		assertTrue(newRestrictionByNumberOfDoneCurricularCourses.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionByNumberOfDoneCurricularCourses.getNumberOfCurricularCourses().equals(newNumber));
		
		assertTrue(newRestrictionPeriodToApply.getPrecedence().equals(newPrecedence));
		assertTrue(newRestrictionPeriodToApply.getPeriodToApplyRestriction().equals(PeriodToApplyRestriction.getEnum(newNumber.intValue())));
	}
	
	
	public void testDelete() {
		
		restriction.delete();
		restrictionByCurricularCourse.delete();

		assertFalse(restriction.hasPrecedence());
		assertFalse(restrictionByCurricularCourse.hasPrecedence());
		assertFalse(restrictionByCurricularCourse.hasPrecedentCurricularCourse());
	}


}
