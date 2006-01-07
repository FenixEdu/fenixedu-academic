package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;

public class OldInquiriesCoursesResTest extends DomainTestBase {
	
	private OldInquiriesCoursesRes oldICRToDelete = null;

	private void setUpDelete() {
		oldICRToDelete = new OldInquiriesCoursesRes();

		ExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.addAssociatedOldInquiriesCoursesRes(oldICRToDelete);
		
		Degree degree = new Degree();
		degree.addAssociatedOldInquiriesCoursesRes(oldICRToDelete);
	}
		
	public void testDelete() {
		
		setUpDelete();
		
		oldICRToDelete.delete();
		
		assertCorrectDeletion(oldICRToDelete);
	}
	
	protected static void assertCorrectDeletion(OldInquiriesCoursesRes oicr) {
		assertFalse("Deleted OldInquiriesCoursesRes should not have ExecutionPeriod", oicr.hasExecutionPeriod());
		assertFalse("Deleted OldInquiriesCoursesRes should not have Degree", oicr.hasDegree());
	}
}
