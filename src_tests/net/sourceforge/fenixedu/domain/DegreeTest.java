package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.IDelegate;

public class DegreeTest extends DomainTestBase {

	private IDegree degreeToDelete;
	private IDegree degreeNotToDelete;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		degreeToDelete = new Degree();
		degreeToDelete.setIdInternal(1);
		degreeNotToDelete = new Degree();
		degreeNotToDelete.setIdInternal(2);
		
		IDegreeInfo di1 = new DegreeInfo();
		di1.setIdInternal(1);
		degreeToDelete.addDegreeInfos(di1);
		IDegreeInfo di2 = new DegreeInfo();
		di2.setIdInternal(2);
		degreeNotToDelete.addDegreeInfos(di2);
		
		IDelegate d1 = new Delegate();
		d1.setIdInternal(1);
		degreeToDelete.addDelegate(d1);
		
		IDelegate d2 = new Delegate();
		d2.setIdInternal(2);
		degreeNotToDelete.addDelegate(d2);
		
		IOldInquiriesCoursesRes oicr1 = new OldInquiriesCoursesRes();
		oicr1.setIdInternal(1);
		IOldInquiriesTeachersRes oitr = new OldInquiriesTeachersRes();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDeleteDegree() {
		try {
			degreeToDelete.deleteDegree();
		} catch (DomainException e) {
			fail("Unexpected DomainException");
		}
		
		try {
			degreeNotToDelete.deleteDegree();
			fail ("Expected DomainException");
		} catch (DomainException e) {
		}
		
		assertTrue(degreeToDelete.getDelegate().isEmpty());
		assertTrue(degreeToDelete.getOldInquiriesCoursesRes().isEmpty());
		assertTrue(degreeToDelete.getOldInquiriesTeachersRes().isEmpty());
		assertTrue(degreeToDelete.getOldInquiriesSummary().isEmpty());
		assertTrue(degreeToDelete.getDegreeCurricularPlans().isEmpty());
		
		assertFalse(degreeNotToDelete.getDelegate().isEmpty());
		assertFalse(degreeNotToDelete.getOldInquiriesCoursesRes().isEmpty());
		assertFalse(degreeNotToDelete.getOldInquiriesTeachersRes().isEmpty());
		assertFalse(degreeNotToDelete.getOldInquiriesSummary().isEmpty());
		assertFalse(degreeNotToDelete.getDegreeCurricularPlans().isEmpty());
		assertFalse(degreeNotToDelete.getDegreeInfos().isEmpty());
	}
}
