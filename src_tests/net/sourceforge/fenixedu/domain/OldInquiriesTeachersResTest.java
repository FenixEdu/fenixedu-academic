package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;

public class OldInquiriesTeachersResTest extends DomainTestBase {
	
	private IOldInquiriesTeachersRes oldITRToDelete = null;
	private IExecutionPeriod executionPeriod = null;
	private IDegree degree = null;
	private ITeacher teacher = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		oldITRToDelete = new OldInquiriesTeachersRes();
		oldITRToDelete.setIdInternal(1);
		
		executionPeriod = new ExecutionPeriod();
		executionPeriod.setIdInternal(1);
		executionPeriod.addOldInquiriesTeachersRes(oldITRToDelete);
		
		degree = new Degree();
		degree.setIdInternal(1);
		degree.addOldInquiriesTeachersRes(oldITRToDelete);
		
		teacher = new Teacher();
		teacher.setIdInternal(1);
		teacher.addOldInquiriesTeacherRes(oldITRToDelete);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete() {
		oldITRToDelete.delete();
		
		assertFalse(oldITRToDelete.hasExecutionPeriod());
		assertFalse(oldITRToDelete.hasDegree());
		assertFalse(oldITRToDelete.hasTeacher());
		
		assertFalse(executionPeriod.hasOldInquiriesTeachersRes(oldITRToDelete));
		assertFalse(degree.hasOldInquiriesTeachersRes(oldITRToDelete));
		assertFalse(teacher.hasOldInquiriesTeacherRes(oldITRToDelete));
	}
}
