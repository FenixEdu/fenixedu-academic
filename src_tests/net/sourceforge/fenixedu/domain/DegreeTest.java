package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
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

	private IDegree degreeToCreate;
	private String newName;
	private String newNameEn;
	private String newSigla;
	private DegreeType newDegreeType;
	private String newConcreteClassForDegreeCurricularPlans;
	
	private IDegree degreeToEditWithDegreeInfo;
	private IDegree degreeToEditWithoutDegreeInfo;
	
	private IDegree degreeToDelete;
	private IDegree degreeNotToDelete;
	private List<IDelegate> delegatesToDelete;
	private List<IOldInquiriesCoursesRes> oldInquiriesCoursesResToDelete;
	private List<IOldInquiriesTeachersRes> oldInquiriesTeachersResToDelete;
	private List<IOldInquiriesSummary> oldInquiriesSummaryToDelete;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		newName = "name";
		newNameEn = "nameEn";
		newSigla = "sigla";
		newDegreeType = DegreeType.DEGREE;
		newConcreteClassForDegreeCurricularPlans = DegreeCurricularPlan.class.getName();
		
		setUpCreate();
		
		setUpEdit();

		setUpDelete();
	}

	private void setUpDelete() {
		delegatesToDelete = new ArrayList();
		oldInquiriesCoursesResToDelete = new ArrayList();
		oldInquiriesTeachersResToDelete = new ArrayList();
		oldInquiriesSummaryToDelete = new ArrayList();
		
		degreeToDelete = new Degree();
		degreeNotToDelete = new Degree();
		
		IDegreeCurricularPlan dcp1 = new DegreeCurricularPlan();
		degreeNotToDelete.addDegreeCurricularPlans(dcp1);
		
		IDegreeInfo di1 = new DegreeInfo();
		degreeToDelete.addDegreeInfos(di1);
		
		IDegreeInfo di2 = new DegreeInfo();
		degreeNotToDelete.addDegreeInfos(di2);
		
		IExecutionPeriod ep1 = new ExecutionPeriod();
		IExecutionYear ey1 = new ExecutionYear();
		
		IStudent student1 = new Student();
		ITeacher teacher1 = new Teacher();
		IDelegate d1 = new Delegate();
		d1.setStudent(student1);
		d1.setExecutionYear(ey1);
		degreeToDelete.addDelegate(d1);
		delegatesToDelete.add(d1);
		
		IDelegate d2 = new Delegate();
		d2.setStudent(student1);
		d2.setExecutionYear(ey1);
		degreeNotToDelete.addDelegate(d2);
		
		IOldInquiriesCoursesRes oicr1 = new OldInquiriesCoursesRes();
		oicr1.setExecutionPeriod(ep1);
		
		IOldInquiriesCoursesRes oicr2 = new OldInquiriesCoursesRes();
		oicr2.setExecutionPeriod(ep1);
		degreeToDelete.addOldInquiriesCoursesRes(oicr1);
		oldInquiriesCoursesResToDelete.add(oicr1);
		degreeNotToDelete.addOldInquiriesCoursesRes(oicr2);
		
		IOldInquiriesTeachersRes oitr1 = new OldInquiriesTeachersRes();
		oitr1.setExecutionPeriod(ep1);
		oitr1.setTeacher(teacher1);
		
		IOldInquiriesTeachersRes oitr2 = new OldInquiriesTeachersRes();
		oitr2.setExecutionPeriod(ep1);
		oitr2.setTeacher(teacher1);
		degreeToDelete.addOldInquiriesTeachersRes(oitr1);
		oldInquiriesTeachersResToDelete.add(oitr1);
		degreeNotToDelete.addOldInquiriesTeachersRes(oitr2);
		
		IOldInquiriesSummary ois1 = new OldInquiriesSummary();
		ois1.setExecutionPeriod(ep1);
		
		IOldInquiriesSummary ois2 = new OldInquiriesSummary();
		ois2.setExecutionPeriod(ep1);
		degreeToDelete.addOldInquiriesSummary(ois1);
		oldInquiriesSummaryToDelete.add(ois1);
		degreeNotToDelete.addOldInquiriesSummary(ois2);
	}

	private void setUpEdit() {
		degreeToEditWithDegreeInfo = new Degree("x", "x", "x", null, null);
		degreeToEditWithoutDegreeInfo = new Degree();
	}

	private void setUpCreate() {
		
		degreeToCreate = new Degree(newName, newNameEn, newSigla, newDegreeType, newConcreteClassForDegreeCurricularPlans);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testCreate() {
		assertTrue(degreeToCreate.getNome().equals(newName));
		assertTrue(degreeToCreate.getNameEn().equals(newNameEn));
		assertTrue(degreeToCreate.getSigla().equals(newSigla));
		assertTrue(degreeToCreate.getTipoCurso().equals(newDegreeType));
		assertTrue(degreeToCreate.getConcreteClassForDegreeCurricularPlans().equals(newConcreteClassForDegreeCurricularPlans));
		assertTrue(degreeToCreate.hasAnyDegreeInfos());
	}
	
	public void testEdit() {
		degreeToEditWithDegreeInfo.edit(newName, newNameEn, newSigla, newDegreeType);
		
		assertTrue(degreeToEditWithDegreeInfo.getNome().equals(newName));
		assertTrue(degreeToEditWithDegreeInfo.getNameEn().equals(newNameEn));
		assertTrue(degreeToEditWithDegreeInfo.getSigla().equals(newSigla));
		assertTrue(degreeToEditWithDegreeInfo.getTipoCurso().equals(newDegreeType));
		assertTrue(degreeToEditWithDegreeInfo.hasAnyDegreeInfos());
		
		
		degreeToEditWithoutDegreeInfo.edit(newName, newNameEn, newSigla, newDegreeType);
		
		assertTrue(degreeToEditWithoutDegreeInfo.getNome().equals(newName));
		assertTrue(degreeToEditWithoutDegreeInfo.getNameEn().equals(newNameEn));
		assertTrue(degreeToEditWithoutDegreeInfo.getSigla().equals(newSigla));
		assertTrue(degreeToEditWithoutDegreeInfo.getTipoCurso().equals(newDegreeType));
		assertTrue(degreeToEditWithoutDegreeInfo.hasAnyDegreeInfos());
	}
	
	
	public void testDelete() {
		try {
			degreeToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		try {
			degreeNotToDelete.delete();
			fail ("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {
		}
		
		assertFalse(degreeToDelete.hasAnyDelegate());
		assertFalse(degreeToDelete.hasAnyOldInquiriesCoursesRes());
		assertFalse(degreeToDelete.hasAnyOldInquiriesTeachersRes());
		assertFalse(degreeToDelete.hasAnyOldInquiriesSummary());
		assertFalse(degreeToDelete.hasAnyDegreeCurricularPlans());
		
		for (IDelegate delegate : delegatesToDelete) {
			assertFalse(delegate.hasExecutionYear());
			assertFalse(delegate.hasStudent());
		}
		
		for (IOldInquiriesCoursesRes oicr : oldInquiriesCoursesResToDelete) {
			assertFalse(oicr.hasExecutionPeriod());
		}
		
		for (IOldInquiriesTeachersRes oitr : oldInquiriesTeachersResToDelete) {
			assertFalse(oitr.hasExecutionPeriod());
			assertFalse(oitr.hasTeacher());
		}
		
		for (IOldInquiriesSummary ois : oldInquiriesSummaryToDelete) {
			assertFalse(ois.hasExecutionPeriod());
		}
		
		assertTrue(degreeNotToDelete.hasAnyDelegate());
		assertTrue(degreeNotToDelete.hasAnyOldInquiriesCoursesRes());
		assertTrue(degreeNotToDelete.hasAnyOldInquiriesTeachersRes());
		assertTrue(degreeNotToDelete.hasAnyOldInquiriesSummary());
		assertTrue(degreeNotToDelete.hasAnyDegreeCurricularPlans());
		assertTrue(degreeNotToDelete.hasAnyDegreeInfos());
		
		for (IDelegate delegate : degreeNotToDelete.getDelegate()) {
			assertTrue(delegate.hasExecutionYear());
			assertTrue(delegate.hasStudent());
		}
		
		for (IOldInquiriesCoursesRes oicr : degreeNotToDelete.getOldInquiriesCoursesRes()) {
			assertTrue (oicr.hasExecutionPeriod());
		}
		
		for (IOldInquiriesTeachersRes oitr : degreeNotToDelete.getOldInquiriesTeachersRes()) {
			assertTrue (oitr.hasExecutionPeriod());
			assertTrue (oitr.hasTeacher());
		}
		
		for (IOldInquiriesSummary ois : degreeNotToDelete.getOldInquiriesSummary()) {
			assertTrue (ois.hasExecutionPeriod());
		}
	}
}
