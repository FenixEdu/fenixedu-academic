package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;

public class OldInquiriesSummaryTest extends DomainTestBase {
	
	private IOldInquiriesSummary oldOISToDelete = null;
	private IExecutionPeriod executionPeriod = null;
	private IDegree degree = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		oldOISToDelete = new OldInquiriesSummary();
		oldOISToDelete.setIdInternal(1);
		
		executionPeriod = new ExecutionPeriod();
		executionPeriod.setIdInternal(1);
		executionPeriod.addOldInquiriesSummary(oldOISToDelete);
		
		degree = new Degree();
		degree.setIdInternal(1);
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
