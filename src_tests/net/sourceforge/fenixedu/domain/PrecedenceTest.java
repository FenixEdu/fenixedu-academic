package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;

public class PrecedenceTest extends DomainTestBase {

	private Precedence precedenceWithRestrictionDoneCurricularCourse;
	private Precedence precedenceWithRestrictionDoneOrHasBeenEnroledInCurricularCourse;
	private Precedence precedenceWithRestrictionHasEverBeenOrIsCurrentlyEnroledInCurricularCourse;
	private Precedence precedenceWithRestrictionHasEverBeenOrWillBeAbleToBeEnroledInCurricularCourse;
	private Precedence precedenceWithRestrictionNotDoneCurricularCourse;
	private Precedence precedenceWithRestrictionNotEnroledInCurricularCourse;
	private Precedence precedenceWithRestrictionByNumberOfDoneCurricularCourses;
	private Precedence precedenceWithRestrictionPeriodToAply;
	
	private Precedence precedenceToDelete;
	
	private Precedence destinationPrecedence;
	private Precedence sourcePrecedence;
	private Restriction restrictionToMerge1;
	private Restriction restrictionToMerge2;
	private Restriction restrictionToMerge3;
	
	private CurricularCourse curricularCourse;
	private CurricularCourse precedentCurricularCourse;
	
	private void setUpCreate() {
		
		curricularCourse = new CurricularCourse();
		precedentCurricularCourse = new CurricularCourse();
	}
	
	
	private void setUpDelete() {
		
		precedenceToDelete = new Precedence();
		
		Restriction restriction = new RestrictionByNumberOfDoneCurricularCourses();
		RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
			
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
	
	private void assertPrecedenceCorrectCreation(Precedence precedence) {
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
