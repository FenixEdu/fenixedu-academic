package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;

public class OldInquiriesSummaryTest extends DomainTestBase {
	
	private IOldInquiriesSummary oldOISToDelete = null;
	
	private void setUpDelete() {
		oldOISToDelete = new OldInquiriesSummary();
		
		IExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.addOldInquiriesSummary(oldOISToDelete);
		
		IDegree degree = new Degree();
		degree.addOldInquiriesSummary(oldOISToDelete);
	}
		
	public void testDelete() {
		
		setUpDelete();
		
		oldOISToDelete.delete();
		
		assertCorrectDeletion(oldOISToDelete);
	}
	
	protected static void assertCorrectDeletion(IOldInquiriesSummary ois) {
		assertFalse("Deleted OldInquiriesSummary should not have ExecutionPeriod", ois.hasExecutionPeriod());
		assertFalse("Deleted OldInquiriesSummary should not have Degree", ois.hasDegree());
	}
}
