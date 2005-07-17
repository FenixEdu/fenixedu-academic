package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;

public class OldInquiriesCoursesResTest extends DomainTestBase {
	
	private IOldInquiriesCoursesRes oldICRToDelete = null;
	private IExecutionPeriod executionPeriod = null;
	private IDegree degree = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		oldICRToDelete = new OldInquiriesCoursesRes();
		oldICRToDelete.setIdInternal(1);
		
		executionPeriod = new ExecutionPeriod();
		executionPeriod.setIdInternal(1);
		executionPeriod.addOldInquiriesCoursesRes(oldICRToDelete);
		
		degree = new Degree();
		degree.setIdInternal(1);
		degree.addOldInquiriesCoursesRes(oldICRToDelete);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete() {
		oldICRToDelete.delete();
		
		assertFalse(oldICRToDelete.hasExecutionPeriod());
		assertFalse(oldICRToDelete.hasDegree());
		
		assertFalse(executionPeriod.hasOldInquiriesCoursesRes(oldICRToDelete));
		assertFalse(degree.hasOldInquiriesCoursesRes(oldICRToDelete));
	}
}
