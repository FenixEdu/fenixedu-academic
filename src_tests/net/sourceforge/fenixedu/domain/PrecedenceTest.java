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
	
	private IPrecedence destinationPrecedence;
	private IPrecedence sourcePrecedence;
	private IRestriction restrictionToMerge1;
	private IRestriction restrictionToMerge2;
	private IRestriction restrictionToMerge3;
	
	private ICurricularCourse curricularCourse;
	private ICurricularCourse precedentCurricularCourse;
	
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
		
		destinationPrecedence = new Precedence();
		sourcePrecedence = new Precedence();
		
		restrictionToMerge1 = new RestrictionDoneCurricularCourse();
		restrictionToMerge2 = new RestrictionDoneCurricularCourse();
		restrictionToMerge3 = new RestrictionDoneCurricularCourse();

		destinationPrecedence.setCurricularCourse(new CurricularCourse());
		sourcePrecedence.setCurricularCourse(new CurricularCourse());
		
		restrictionToMerge1.setPrecedence(destinationPrecedence);
		restrictionToMerge2.setPrecedence(sourcePrecedence);
		restrictionToMerge3.setPrecedence(sourcePrecedence);
	}
	
	public void testCreate() {
		
		setUpCreate();
		
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
		
		
		assertPrecedenceCorrectCreation(precedenceWithRestrictionDoneCurricularCourse);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionDoneOrHasBeenEnroledInCurricularCourse);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionHasEverBeenOrWillBeAbleToBeEnroledInCurricularCourse);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionNotDoneCurricularCourse);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionNotEnroledInCurricularCourse);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionByNumberOfDoneCurricularCourses);
		assertPrecedenceCorrectCreation(precedenceWithRestrictionPeriodToAply);		
	}
	
	private void assertPrecedenceCorrectCreation(IPrecedence precedence) {
		assertTrue("Creation failed: Precedence has no CurricularCourse", precedence.hasCurricularCourse());
		assertTrue("Creation failed: Precedence has no Restrictions", precedence.hasAnyRestrictions());
	}

	public void testDelete() {
		
		setUpDelete();
		
		precedenceToDelete.delete();
		
		assertFalse("Failed to dereference CurricularCourse", precedenceToDelete.hasCurricularCourse());
		assertFalse("Failed to dereference Restrictions", precedenceToDelete.hasAnyRestrictions());
	}
	
	
	public void testMergePrecedences() {
		
		setUpMergePrecedences();
		
		destinationPrecedence.mergePrecedences(sourcePrecedence);
		
		assertTrue("Restriction should not have changed Precedence", restrictionToMerge1.getPrecedence().equals(destinationPrecedence));
		assertTrue("Failed to change Restriction to destination Precedence", restrictionToMerge2.getPrecedence().equals(destinationPrecedence));
		assertTrue("Failed to change Restriction to destination Precedence", restrictionToMerge3.getPrecedence().equals(destinationPrecedence));
		
		assertTrue("Destination Precedence should have CurricularCourse", destinationPrecedence.hasCurricularCourse());
		assertFalse("Failed to dereference CurricularCourse from source Precedence", sourcePrecedence.hasCurricularCourse());
		assertFalse("Failed to move Restrictions from source to destination Precedence", sourcePrecedence.hasAnyRestrictions());
	}
}
