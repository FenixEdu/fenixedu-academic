package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.IDelegate;

public class DegreeTest extends DomainTestBase {

	private IDegree degreeToDelete;
	private IDegree degreeNotToDelete;
	private List<IDelegate> delegatesToDelete;
	private List<IOldInquiriesCoursesRes> oldInquiriesCoursesResToDelete;
	private List<IOldInquiriesTeachersRes> oldInquiriesTeachersResToDelete;
	private List<IOldInquiriesSummary> oldInquiriesSummaryToDelete;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		delegatesToDelete = new ArrayList();
		oldInquiriesCoursesResToDelete = new ArrayList();
		oldInquiriesTeachersResToDelete = new ArrayList();
		oldInquiriesSummaryToDelete = new ArrayList();
		
		degreeToDelete = new Degree();
		degreeToDelete.setIdInternal(1);
		degreeNotToDelete = new Degree();
		degreeNotToDelete.setIdInternal(2);
		
		IDegreeCurricularPlan dcp1 = new DegreeCurricularPlan();
		dcp1.setIdInternal(1);
		degreeNotToDelete.addDegreeCurricularPlans(dcp1);
		
		IDegreeInfo di1 = new DegreeInfo();
		di1.setIdInternal(1);
		degreeToDelete.addDegreeInfos(di1);
		IDegreeInfo di2 = new DegreeInfo();
		di2.setIdInternal(2);
		degreeNotToDelete.addDegreeInfos(di2);
		
		IExecutionPeriod ep1 = new ExecutionPeriod();
		ep1.setIdInternal(1);
		IExecutionYear ey1 = new ExecutionYear();
		ey1.setIdInternal(1);
		
		IStudent student1 = new Student();
		student1.setIdInternal(1);
		
		ITeacher teacher1 = new Teacher();
		teacher1.setIdInternal(1);
		
		IDelegate d1 = new Delegate();
		d1.setIdInternal(1);
		d1.setStudent(student1);
		d1.setExecutionYear(ey1);
		degreeToDelete.addDelegate(d1);
		delegatesToDelete.add(d1);
		
		IDelegate d2 = new Delegate();
		d2.setIdInternal(2);
		d2.setStudent(student1);
		d2.setExecutionYear(ey1);
		degreeNotToDelete.addDelegate(d2);
		
		IOldInquiriesCoursesRes oicr1 = new OldInquiriesCoursesRes();
		oicr1.setIdInternal(1);
		oicr1.setExecutionPeriod(ep1);
		IOldInquiriesCoursesRes oicr2 = new OldInquiriesCoursesRes();
		oicr2.setIdInternal(2);
		oicr2.setExecutionPeriod(ep1);
		degreeToDelete.addOldInquiriesCoursesRes(oicr1);
		oldInquiriesCoursesResToDelete.add(oicr1);
		degreeNotToDelete.addOldInquiriesCoursesRes(oicr2);
		
		IOldInquiriesTeachersRes oitr1 = new OldInquiriesTeachersRes();
		oitr1.setIdInternal(1);
		oitr1.setExecutionPeriod(ep1);
		oitr1.setTeacher(teacher1);
		IOldInquiriesTeachersRes oitr2 = new OldInquiriesTeachersRes();
		oitr2.setIdInternal(2);
		oitr2.setExecutionPeriod(ep1);
		oitr2.setTeacher(teacher1);
		degreeToDelete.addOldInquiriesTeachersRes(oitr1);
		oldInquiriesTeachersResToDelete.add(oitr1);
		degreeNotToDelete.addOldInquiriesTeachersRes(oitr2);
		
		IOldInquiriesSummary ois1 = new OldInquiriesSummary();
		ois1.setIdInternal(1);
		ois1.setExecutionPeriod(ep1);
		IOldInquiriesSummary ois2 = new OldInquiriesSummary();
		ois2.setIdInternal(2);
		ois2.setExecutionPeriod(ep1);
		degreeToDelete.addOldInquiriesSummary(ois1);
		oldInquiriesSummaryToDelete.add(ois1);
		degreeNotToDelete.addOldInquiriesSummary(ois2);

	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDeleteDegree() {
		try {
			degreeToDelete.deleteDegree();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		try {
			degreeNotToDelete.deleteDegree();
			fail ("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {
		}
		
		assertTrue(degreeToDelete.getDelegate().isEmpty());
		assertTrue(degreeToDelete.getOldInquiriesCoursesRes().isEmpty());
		assertTrue(degreeToDelete.getOldInquiriesTeachersRes().isEmpty());
		assertTrue(degreeToDelete.getOldInquiriesSummary().isEmpty());
		assertTrue(degreeToDelete.getDegreeCurricularPlans().isEmpty());
		
		for (IDelegate delegate : delegatesToDelete) {
			assertNull(delegate.getExecutionYear());
			assertNull(delegate.getStudent());
		}
		
		for (IOldInquiriesCoursesRes oicr : oldInquiriesCoursesResToDelete) {
			assertNull (oicr.getExecutionPeriod());
		}
		
		for (IOldInquiriesTeachersRes oitr : oldInquiriesTeachersResToDelete) {
			assertNull (oitr.getExecutionPeriod());
			assertNull (oitr.getTeacher());
		}
		
		for (IOldInquiriesSummary ois : oldInquiriesSummaryToDelete) {
			assertNull (ois.getExecutionPeriod());
		}
		
		assertFalse(degreeNotToDelete.getDelegate().isEmpty());
		assertFalse(degreeNotToDelete.getOldInquiriesCoursesRes().isEmpty());
		assertFalse(degreeNotToDelete.getOldInquiriesTeachersRes().isEmpty());
		assertFalse(degreeNotToDelete.getOldInquiriesSummary().isEmpty());
		assertFalse(degreeNotToDelete.getDegreeCurricularPlans().isEmpty());
		assertFalse(degreeNotToDelete.getDegreeInfos().isEmpty());
		
		for (IDelegate delegate : degreeNotToDelete.getDelegate()) {
			assertNotNull(delegate.getExecutionYear());
			assertNotNull(delegate.getStudent());
		}
		
		for (IOldInquiriesCoursesRes oicr : degreeNotToDelete.getOldInquiriesCoursesRes()) {
			assertNotNull (oicr.getExecutionPeriod());
		}
		
		for (IOldInquiriesTeachersRes oitr : degreeNotToDelete.getOldInquiriesTeachersRes()) {
			assertNotNull (oitr.getExecutionPeriod());
			assertNotNull (oitr.getTeacher());
		}
		
		for (IOldInquiriesSummary ois : degreeNotToDelete.getOldInquiriesSummary()) {
			assertNotNull (ois.getExecutionPeriod());
		}
	}
}
