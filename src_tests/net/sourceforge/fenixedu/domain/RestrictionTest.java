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

	public void testCreate() {
		
		setUpCreate();
		
		
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
		
		assertRestrictionByCurricularCourseCreationCorrect(newRestrictionDoneCurricularCourse, newPrecedence, newPrecedentCurricularCourse, "Failed to create RestrictionDoneCurricularCourse");

		assertRestrictionByCurricularCourseCreationCorrect(newRestrictionDoneOrHasBeenEnroledInCurricularCourse, newPrecedence, newPrecedentCurricularCourse, "Failed to create RestrictionDoneOrHasBeenEnroledInCurricularCourse");
		
		assertRestrictionByCurricularCourseCreationCorrect(newRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse, newPrecedence, newPrecedentCurricularCourse, "Failed to create RestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse");
		
		assertRestrictionByCurricularCourseCreationCorrect(newRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse, newPrecedence, newPrecedentCurricularCourse, "Failed to create RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse");

		assertRestrictionByCurricularCourseCreationCorrect(newRestrictionNotDoneCurricularCourse, newPrecedence, newPrecedentCurricularCourse, "Failed to create RestrictionNotDoneCurricularCourse");

		assertRestrictionByCurricularCourseCreationCorrect(newRestrictionNotEnroledCurricularCourse, newPrecedence, newPrecedentCurricularCourse, "Failed to create RestrictionNotEnroledCurricularCourse");

		assertRestrictionByNumberOfCurricularCoursesCreationCorrect(newRestrictionByNumberOfDoneCurricularCourses, newPrecedence, newNumber, "Failed to create RestrictionByNumberOfDoneCurricularCourses");
		
		assertRestrictionPeriodToApplyCreationCorrect (newRestrictionPeriodToApply, newPrecedence, newNumber,  "Failed to create RestrictionPeriodToApply");
	}
	
	
	private void assertRestrictionPeriodToApplyCreationCorrect(IRestrictionPeriodToApply restriction, IPrecedence precedence, Integer number, String message) {
		assertTrue(message, restriction.getPrecedence().equals(precedence));
		assertTrue(message, restriction.getPeriodToApplyRestriction().equals(PeriodToApplyRestriction.getEnum(number.intValue())));		
	}

	private void assertRestrictionByNumberOfCurricularCoursesCreationCorrect (IRestrictionByNumberOfCurricularCourses restriction, IPrecedence precedence, Integer number, String message) {
		assertTrue(message, restriction.getPrecedence().equals(precedence));
		assertTrue(message, restriction.getNumberOfCurricularCourses().equals(number));
	}

	private void assertRestrictionByCurricularCourseCreationCorrect(IRestrictionByCurricularCourse restriction, IPrecedence precedence, ICurricularCourse curricularCourse, String message) {
		assertTrue(message, restriction.getPrecedence().equals(precedence));
		assertTrue(message, restriction.getPrecedentCurricularCourse().equals(curricularCourse));
	}



	public void testDelete() {
		
		setUpDelete();
		
		restriction.delete();
		restrictionByCurricularCourse.delete();

		assertFalse("Failed to dereference Precedence", restriction.hasPrecedence());
		assertFalse("Failed to dereference Precedence", restrictionByCurricularCourse.hasPrecedence());
		assertFalse("Failed to dereference PrecedentCurricularCourse", restrictionByCurricularCourse.hasPrecedentCurricularCourse());
	}


}
