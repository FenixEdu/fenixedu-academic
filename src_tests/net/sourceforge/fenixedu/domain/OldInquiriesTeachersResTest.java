package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;

public class OldInquiriesTeachersResTest extends DomainTestBase {
	
	private OldInquiriesTeachersRes oldITRToDelete = null;
	
	private void setUpDelete() {
		
		oldITRToDelete = new OldInquiriesTeachersRes();
		
		ExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.addAssociatedOldInquiriesTeachersRes(oldITRToDelete);
		
		Degree degree = new Degree();
		degree.addAssociatedOldInquiriesTeachersRes(oldITRToDelete);
		
//		Teacher teacher = new Teacher();
//		teacher.addAssociatedOldInquiriesTeachersRes(oldITRToDelete);
	}
	
	public void testDelete() {
		
		setUpDelete();
		
		oldITRToDelete.delete();
		
		assertCorrectDeletion(oldITRToDelete);
	}
	
	protected static void assertCorrectDeletion(OldInquiriesTeachersRes oitr) {
		assertFalse("Deleted OldInquiriesTeachersRes should not have ExecutionPeriod", oitr.hasExecutionPeriod());
		assertFalse("Deleted OldInquiriesTeachersRes should not have Degree", oitr.hasDegree());
		assertFalse("Deleted OldInquiriesTeachersRes should not have Teacher", oitr.hasTeacher());
	}
}
