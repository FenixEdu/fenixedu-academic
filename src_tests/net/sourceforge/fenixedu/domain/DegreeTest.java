package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.Delegate;

public class DegreeTest extends DomainTestBase {

	private Degree degreeToCreate;
	private String newName;
	private String newNameEn;
	private String newSigla;
	private DegreeType newDegreeType;
	private String newConcreteClassForDegreeCurricularPlans;
	
	private Degree degreeToEditWithDegreeInfo;
	private Degree degreeToEditWithoutDegreeInfo;
	
	private Degree degreeToDelete;
	private Degree degreeNotToDelete;
	private List<Delegate> delegatesToDelete;
	private List<OldInquiriesCoursesRes> oldInquiriesCoursesResToDelete;
	private List<OldInquiriesTeachersRes> oldInquiriesTeachersResToDelete;
	private List<OldInquiriesSummary> oldInquiriesSummaryToDelete;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//common initialization for 'create' and 'edit' method tests
		newName = "name";
		newNameEn = "nameEn";
		newSigla = "sigla";
		newDegreeType = DegreeType.DEGREE;
		newConcreteClassForDegreeCurricularPlans = DegreeCurricularPlan.class.getName();
	}

	private void setUpDelete() {
		delegatesToDelete = new ArrayList();
		oldInquiriesCoursesResToDelete = new ArrayList();
		oldInquiriesTeachersResToDelete = new ArrayList();
		oldInquiriesSummaryToDelete = new ArrayList();
		
		degreeToDelete = new Degree();
		degreeNotToDelete = new Degree();
		
		DegreeCurricularPlan dcp1 = new DegreeCurricularPlan();
		degreeNotToDelete.addDegreeCurricularPlans(dcp1);
		
//		DegreeInfo di1 = new DegreeInfo();
//		degreeToDelete.addDegreeInfos(di1);
//		
//		DegreeInfo di2 = new DegreeInfo();
//		degreeNotToDelete.addDegreeInfos(di2);
//		
//		ExecutionPeriod ep1 = new ExecutionPeriod();
//		ExecutionYear ey1 = new ExecutionYear();
//		
//		Student student1 = new Student();
//		Teacher teacher1 = new Teacher();
//		Delegate d1 = new Delegate();
//		d1.setStudent(student1);
//		d1.setExecutionYear(ey1);
//		degreeToDelete.addDelegate(d1);
//		delegatesToDelete.add(d1);
//		
//		Delegate d2 = new Delegate();
//		d2.setStudent(student1);
//		d2.setExecutionYear(ey1);
//		degreeNotToDelete.addDelegate(d2);
//		
//		OldInquiriesCoursesRes oicr1 = new OldInquiriesCoursesRes();
//		oicr1.setExecutionPeriod(ep1);
//		
//		OldInquiriesCoursesRes oicr2 = new OldInquiriesCoursesRes();
//		oicr2.setExecutionPeriod(ep1);
//		degreeToDelete.addAssociatedOldInquiriesCoursesRes(oicr1);
//		oldInquiriesCoursesResToDelete.add(oicr1);
//		degreeNotToDelete.addAssociatedOldInquiriesCoursesRes(oicr2);
//		
//		OldInquiriesTeachersRes oitr1 = new OldInquiriesTeachersRes();
//		oitr1.setExecutionPeriod(ep1);
//		oitr1.setTeacher(teacher1);
//		
//		OldInquiriesTeachersRes oitr2 = new OldInquiriesTeachersRes();
//		oitr2.setExecutionPeriod(ep1);
//		oitr2.setTeacher(teacher1);
//		degreeToDelete.addAssociatedOldInquiriesTeachersRes(oitr1);
//		oldInquiriesTeachersResToDelete.add(oitr1);
//		degreeNotToDelete.addAssociatedOldInquiriesTeachersRes(oitr2);
//		
//		OldInquiriesSummary ois1 = new OldInquiriesSummary();
//		ois1.setExecutionPeriod(ep1);
//		
//		OldInquiriesSummary ois2 = new OldInquiriesSummary();
//		ois2.setExecutionPeriod(ep1);
//		degreeToDelete.addAssociatedOldInquiriesSummaries(ois1);
//		oldInquiriesSummaryToDelete.add(ois1);
//		degreeNotToDelete.addAssociatedOldInquiriesSummaries(ois2);
	}

	private void setUpEdit() {
		degreeToEditWithDegreeInfo = new Degree("x", "x", "x", (DegreeType) null, (GradeScale) null, (String) null);
		degreeToEditWithoutDegreeInfo = new Degree();
	}

	private void setUpCreate() {
		
		degreeToCreate = new Degree(newName, newNameEn, newSigla, newDegreeType, null, newConcreteClassForDegreeCurricularPlans);
	}

	
	public void testCreate() {
		
		setUpCreate();
		
		assertCorrectInitialization("Failed to assign property on creation", degreeToCreate,newName,newNameEn,newSigla,newDegreeType);
		assertTrue("Failed to assign property on creation: concreteClassForDegreeCurricularPlans", degreeToCreate.getConcreteClassForDegreeCurricularPlans().equals(newConcreteClassForDegreeCurricularPlans));
	}
	
	public void testEdit() {
		
		setUpEdit();
		
		
		degreeToEditWithDegreeInfo.edit(newName, newNameEn, newSigla, newDegreeType, null);

		assertCorrectInitialization("Edition failed for property", degreeToEditWithDegreeInfo,newName,newNameEn,newSigla,newDegreeType);

		
		degreeToEditWithoutDegreeInfo.edit(newName, newNameEn, newSigla, newDegreeType, null);

		assertCorrectInitialization("Edition failed for property", degreeToEditWithoutDegreeInfo,newName,newNameEn,newSigla,newDegreeType);
	}
	
	
	public void testDelete() {
		
		setUpDelete();
		
		try {
			degreeToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Failed to dereference Delegate", degreeToDelete.hasAnyDelegate());
		assertFalse("Failed to dereference OldInquiriesCoursesRes", degreeToDelete.hasAnyAssociatedOldInquiriesCoursesRes());
		assertFalse("Failed to dereference OldInquiriesTeachersRes", degreeToDelete.hasAnyAssociatedOldInquiriesTeachersRes());
		assertFalse("Failed to dereference OldInquiriesSummary", degreeToDelete.hasAnyAssociatedOldInquiriesSummaries());
		assertFalse("Deleted Degree should not have DegreeCurricularPlans", degreeToDelete.hasAnyDegreeCurricularPlans());
		
		for (Delegate delegate : delegatesToDelete) {
			DelegateTest.assertCorrectDeletion(delegate);
		}
		
		for (OldInquiriesCoursesRes oicr : oldInquiriesCoursesResToDelete) {
			OldInquiriesCoursesResTest.assertCorrectDeletion(oicr);
		}
		
		for (OldInquiriesTeachersRes oitr : oldInquiriesTeachersResToDelete) {
			OldInquiriesTeachersResTest.assertCorrectDeletion(oitr);
		}
		
		for (OldInquiriesSummary ois : oldInquiriesSummaryToDelete) {
			OldInquiriesSummaryTest.assertCorrectDeletion(ois);
		}
		
		
		
		try {
			degreeNotToDelete.delete();
			fail ("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {
		}

		assertTrue("Should not have dereferenced Delegates", degreeNotToDelete.hasAnyDelegate());
		assertTrue("Should not have dereferenced OldInquiriesCoursesRes", degreeNotToDelete.hasAnyAssociatedOldInquiriesCoursesRes());
		assertTrue("Should not have dereferenced OldInquiriesTeachersRes", degreeNotToDelete.hasAnyAssociatedOldInquiriesTeachersRes());
		assertTrue("Should not have dereferenced OldInquiriesSummary", degreeNotToDelete.hasAnyAssociatedOldInquiriesSummaries());
		assertTrue("Should not have dereferenced DegreeCurricularPlans", degreeNotToDelete.hasAnyDegreeCurricularPlans());
		assertTrue("Should not have dereferenced DegreeInfos", degreeNotToDelete.hasAnyDegreeInfos());
		
		for (Delegate delegate : degreeNotToDelete.getDelegate()) {
			assertTrue("Should not have dereferenced Delegate from ExecutionYear", delegate.hasExecutionYear());
			assertTrue("Should not have dereferenced Delegate from Student", delegate.hasStudent());
		}
		
		for (OldInquiriesCoursesRes oicr : degreeNotToDelete.getAssociatedOldInquiriesCoursesRes()) {
			assertTrue ("Should not have dereferenced OldInquiriesCoursesRes from ExecutionPeriod", oicr.hasExecutionPeriod());
		}
		
		for (OldInquiriesTeachersRes oitr : degreeNotToDelete.getAssociatedOldInquiriesTeachersRes()) {
			assertTrue ("Should not have dereferenced OldInquiriesTeachersRes from ExecutionPeriod", oitr.hasExecutionPeriod());
			assertTrue ("Should not have dereferenced OldInquiriesTeachersRes from Teacher", oitr.hasTeacher());
		}
		
		for (OldInquiriesSummary ois : degreeNotToDelete.getAssociatedOldInquiriesSummaries()) {
			assertTrue ("Should not have dereferenced OldInquiriesSummary from ExecutionPeriod", ois.hasExecutionPeriod());
		}
	}
	
	private void assertCorrectInitialization(String errorMessagePrefix, Degree degree, String name, String nameEn, String code, DegreeType type) {
		assertTrue(errorMessagePrefix + ": name", degree.getNome().equals(name));
		assertTrue(errorMessagePrefix + ": nameEn", degree.getNameEn().equals(nameEn));
		assertTrue(errorMessagePrefix + ": code", degree.getSigla().equals(code));
		assertTrue(errorMessagePrefix + ": DegreeType", degree.getTipoCurso().equals(type));
		assertTrue(errorMessagePrefix + ": DegreeInfos", degree.hasAnyDegreeInfos());
	}
}
