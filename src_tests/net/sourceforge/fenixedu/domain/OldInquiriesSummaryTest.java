package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;

public class OldInquiriesSummaryTest extends DomainTestBase {
	
	private OldInquiriesSummary oldOISToDelete = null;
	
	private void setUpDelete() {
		oldOISToDelete = new OldInquiriesSummary();
		
		ExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.addAssociatedOldInquiriesSummaries(oldOISToDelete);
		
		Degree degree = new Degree();
		degree.addAssociatedOldInquiriesSummaries(oldOISToDelete);
	}
		
	public void testDelete() {
		
		setUpDelete();
		
		oldOISToDelete.delete();
		
		assertCorrectDeletion(oldOISToDelete);
	}
	
	protected static void assertCorrectDeletion(OldInquiriesSummary ois) {
		assertFalse("Deleted OldInquiriesSummary should not have ExecutionPeriod", ois.hasExecutionPeriod());
		assertFalse("Deleted OldInquiriesSummary should not have Degree", ois.hasDegree());
	}
}
