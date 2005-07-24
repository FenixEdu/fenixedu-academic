package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;

public class OldInquiriesSummaryTest extends DomainTestBase {
	
	private IOldInquiriesSummary oldOISToDelete = null;
	private IExecutionPeriod executionPeriod = null;
	private IDegree degree = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		setUpDelete();
	}

	private void setUpDelete() {
		oldOISToDelete = new OldInquiriesSummary();
		
		executionPeriod = new ExecutionPeriod();
		executionPeriod.addOldInquiriesSummary(oldOISToDelete);
		
		degree = new Degree();
		degree.addOldInquiriesSummary(oldOISToDelete);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete() {
		oldOISToDelete.delete();
		
		assertFalse(oldOISToDelete.hasExecutionPeriod());
		assertFalse(oldOISToDelete.hasDegree());
		
		assertFalse(executionPeriod.hasOldInquiriesSummary(oldOISToDelete));
		assertFalse(degree.hasOldInquiriesSummary(oldOISToDelete));
	}
}
