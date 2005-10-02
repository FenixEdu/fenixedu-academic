package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;

public class OldInquiriesCoursesResTest extends DomainTestBase {
	
	private IOldInquiriesCoursesRes oldICRToDelete = null;

	private void setUpDelete() {
		oldICRToDelete = new OldInquiriesCoursesRes();

		IExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.addAssociatedOldInquiriesCoursesRes(oldICRToDelete);
		
		IDegree degree = new Degree();
		degree.addAssociatedOldInquiriesCoursesRes(oldICRToDelete);
	}
		
	public void testDelete() {
		
		setUpDelete();
		
		oldICRToDelete.delete();
		
		assertCorrectDeletion(oldICRToDelete);
	}
	
	protected static void assertCorrectDeletion(IOldInquiriesCoursesRes oicr) {
		assertFalse("Deleted OldInquiriesCoursesRes should not have ExecutionPeriod", oicr.hasExecutionPeriod());
		assertFalse("Deleted OldInquiriesCoursesRes should not have Degree", oicr.hasDegree());
	}
}
