package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class BranchTest extends DomainTestBase {

	private Branch nonCommonBranchToDelete = null;
	private Branch commonBranchInOtherDegreeCurricularPlan = null;
	private Branch commonBranchInSameDegreeCurricularPlan = null;
	private List<CurricularCourse> curricularCoursesWithOneHavingCCScopeInCommonBranch = new ArrayList();
	private List<CurricularCourse> curricularCoursesWithNoneHavingCCScopeInCommonBranch = new ArrayList();
	private List<CurricularCourseGroup> curricularCourseGroupsToCommonBranch = new ArrayList();
	private List<OptionalCurricularCourseGroup> optionalCurricularCourseGroupsToCommonBranch = new ArrayList();
	private List<AreaCurricularCourseGroup> areaCurricularCourseGroupsToCommonBranch = new ArrayList();
	private List<StudentCurricularPlan> studentCurricularPlans = new ArrayList();
	private List<StudentCurricularPlanLEEC> studentCurricularPlansLEEC = new ArrayList();
	private List<StudentCurricularPlanLEIC> studentCurricularPlansLEIC = new ArrayList();
	private List<CurricularCourseScope> curricularCourseScopesToDelete = new ArrayList();
	private List<CurricularCourseScope> curricularCourseScopesToCommonBranch = new ArrayList();

	private Branch nonCommonBranchNotToDeleteWithProposals = null;
	
	private Branch commonBranchToDelete = null;
	private List<StudentCurricularPlan> studentCurricularPlansInCommonBranchToDelete = new ArrayList();
	private List<StudentCurricularPlanLEEC> studentCurricularPlansLEECInCommonBranchToDelete = new ArrayList();
	private List<StudentCurricularPlanLEIC> studentCurricularPlansLEICInCommonBranchToDelete = new ArrayList();
	
	private Branch commonBranchNotToDelete = null;
	
	private Branch branchToEdit = null;
	private String editedName = "";
	private String editedNameEn = "";
	private String editedCode = "";
	
	private Branch branchToCreate = null;
	private String createdBranchName = "";
	private String createdBranchNameEn = "";
	private String createdBranchCode = "";
	private DegreeCurricularPlan createdBranchDCP = null;
	
	private Branch createBranchWithNameAndType(BranchType branchType, String name) {
		Branch br = new Branch();
		br.setBranchType(branchType);
		br.setName(name);
		
		return br;
	}
	
	private void setUpDeleteCaseForNonDeletableCommonBranch() {
		commonBranchNotToDelete = createBranchWithNameAndType(BranchType.COMNBR,"");		
		
		OptionalCurricularCourseGroup opCCG = new OptionalCurricularCourseGroup();
		opCCG.setBranch(commonBranchNotToDelete);
		
		AreaCurricularCourseGroup areaCCG = new AreaCurricularCourseGroup();
		areaCCG.setBranch(commonBranchNotToDelete);
		
		StudentCurricularPlan scpW = new StudentCurricularPlan();
		scpW.setBranch(commonBranchNotToDelete);
		
		StudentCurricularPlanLEIC scpWLEIC = new StudentCurricularPlanLEIC();
		scpWLEIC.setSecundaryBranch(commonBranchNotToDelete);
		
		StudentCurricularPlanLEEC scpWLEEC = new StudentCurricularPlanLEEC();
		scpWLEEC.setSecundaryBranch(commonBranchNotToDelete);
		
		DegreeCurricularPlan dcpW = new DegreeCurricularPlan();
		dcpW.addAreas(commonBranchNotToDelete);
		
		CurricularCourseScope ccsW = new CurricularCourseScope();
		ccsW.setBranch(commonBranchNotToDelete);
	}

	private void setUpDeleteCaseForCommonBranch() {
		commonBranchToDelete = createBranchWithNameAndType(BranchType.COMNBR,"");
		
		StudentCurricularPlan scpD = new StudentCurricularPlan();
		scpD.setBranch(commonBranchToDelete);
		studentCurricularPlansInCommonBranchToDelete.add(scpD);
		
		StudentCurricularPlanLEIC scpDLEIC = new StudentCurricularPlanLEIC();
		scpDLEIC.setSecundaryBranch(commonBranchToDelete);
		studentCurricularPlansLEICInCommonBranchToDelete.add(scpDLEIC);
		
		StudentCurricularPlanLEEC scpDLEEC = new StudentCurricularPlanLEEC();
		scpDLEEC.setSecundaryBranch(commonBranchToDelete);
		studentCurricularPlansLEECInCommonBranchToDelete.add(scpDLEEC);
		
		DegreeCurricularPlan dcpD = new DegreeCurricularPlan();
		dcpD.addAreas(commonBranchToDelete);
	}

	private void setUpDeleteCaseForNonDeletableBranchWithProposals() {
		nonCommonBranchNotToDeleteWithProposals = createBranchWithNameAndType(BranchType.SECNBR,"Branch not to delete because of associated final degree work proposals");
		
		Proposal proposal1 = new Proposal();
		nonCommonBranchNotToDeleteWithProposals.addAssociatedFinalDegreeWorkProposals(proposal1);
		
		OptionalCurricularCourseGroup opCCG = new OptionalCurricularCourseGroup();
		opCCG.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		AreaCurricularCourseGroup areaCCG = new AreaCurricularCourseGroup();
		areaCCG.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		StudentCurricularPlan scpW = new StudentCurricularPlan();
		scpW.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		StudentCurricularPlanLEIC scpWLEIC = new StudentCurricularPlanLEIC();
		scpWLEIC.setSecundaryBranch(nonCommonBranchNotToDeleteWithProposals);
		
		StudentCurricularPlanLEEC scpWLEEC = new StudentCurricularPlanLEEC();
		scpWLEEC.setSecundaryBranch(nonCommonBranchNotToDeleteWithProposals);
		
		DegreeCurricularPlan dcpW = new DegreeCurricularPlan();
		dcpW.addAreas(nonCommonBranchNotToDeleteWithProposals);
		
		CurricularCourseScope ccsW = new CurricularCourseScope();
		ccsW.setBranch(nonCommonBranchNotToDeleteWithProposals);
	}

	private void setUpDeleteCaseForNonCommonBranch() {
		nonCommonBranchToDelete = createBranchWithNameAndType(BranchType.SECNBR,"Generic Branch To Delete");
		
		commonBranchInOtherDegreeCurricularPlan = createBranchWithNameAndType(BranchType.COMNBR,"");
		
		commonBranchInSameDegreeCurricularPlan = createBranchWithNameAndType(BranchType.COMNBR,"");
		
		DegreeCurricularPlan dcp = new DegreeCurricularPlan();
		nonCommonBranchToDelete.setDegreeCurricularPlan(dcp);
		commonBranchInSameDegreeCurricularPlan.setDegreeCurricularPlan(dcp);
		
		DegreeCurricularPlan otherDCP = new DegreeCurricularPlan();
		commonBranchInOtherDegreeCurricularPlan.setDegreeCurricularPlan(otherDCP);
		
		CurricularCourse cc1 = new CurricularCourse();
		CurricularCourse cc2 = new CurricularCourse();
		CurricularCourse cc3 = new CurricularCourse();
		
		CurricularCourseScope ccs1 = new CurricularCourseScope();
		ccs1.setBranch(nonCommonBranchToDelete);
		ccs1.setCurricularCourse(cc1);
		curricularCoursesWithNoneHavingCCScopeInCommonBranch.add(cc1);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc1);
		curricularCourseScopesToCommonBranch.add(ccs1);

		CurricularCourseScope ccs2 = new CurricularCourseScope();
		ccs2.setBranch(nonCommonBranchToDelete);
		ccs2.setCurricularCourse(cc2);
		curricularCoursesWithNoneHavingCCScopeInCommonBranch.add(cc2);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc2);
		curricularCourseScopesToCommonBranch.add(ccs2);

		CurricularCourseScope ccs3 = new CurricularCourseScope();
		ccs3.setBranch(nonCommonBranchToDelete);
		ccs3.setCurricularCourse(cc3);
		curricularCourseScopesToDelete.add(ccs3);

		CurricularCourseScope ccs4 = new CurricularCourseScope();
		ccs4.setBranch(commonBranchInSameDegreeCurricularPlan);
		ccs4.setCurricularCourse(cc3);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc3);
		
		CurricularCourseScope ccs5 = new CurricularCourseScope();
		ccs5.setBranch(commonBranchInOtherDegreeCurricularPlan);
		ccs5.setCurricularCourse(cc2);

		CurricularCourseGroup ccg1 = new OptionalCurricularCourseGroup();
		nonCommonBranchToDelete.addCurricularCourseGroups(ccg1);
		curricularCourseGroupsToCommonBranch.add(ccg1);
		optionalCurricularCourseGroupsToCommonBranch.add((OptionalCurricularCourseGroup)ccg1);
		
		CurricularCourseGroup ccg2 = new AreaCurricularCourseGroup();
		nonCommonBranchToDelete.addCurricularCourseGroups(ccg2);
		curricularCourseGroupsToCommonBranch.add(ccg2);
		areaCurricularCourseGroupsToCommonBranch.add((AreaCurricularCourseGroup)ccg2);
		
		
		StudentCurricularPlan scp1 = new StudentCurricularPlan();
		scp1.setBranch(nonCommonBranchToDelete);
		studentCurricularPlans.add(scp1);
		
		StudentCurricularPlanLEIC scp2 = new StudentCurricularPlanLEIC();
		scp2.setBranch(nonCommonBranchToDelete);
		scp2.setSecundaryBranch(nonCommonBranchToDelete);
		studentCurricularPlansLEIC.add(scp2);
		
		StudentCurricularPlanLEEC scp3 = new StudentCurricularPlanLEEC();
		scp3.setBranch(nonCommonBranchToDelete);
		scp3.setSecundaryBranch(nonCommonBranchToDelete);
		studentCurricularPlansLEEC.add(scp3);
	}
	
	private void setUpEditCase() {
		branchToEdit = new Branch();
		branchToEdit.setName("");
		branchToEdit.setNameEn("");
		branchToEdit.setCode("");
		
		editedName = "Ramo A";
		editedNameEn = "Branch A";
		editedCode = "1010";
	}

	private void setUpCreateCase() {
		createdBranchName = "novo Ramo";
		createdBranchNameEn = "new Branch";
		createdBranchCode = "007";
		createdBranchDCP = new DegreeCurricularPlan();
	}
	
	public void testDelete() {
		
		setUpDeleteCaseForNonCommonBranch();
		setUpDeleteCaseForNonDeletableBranchWithProposals();
		setUpDeleteCaseForCommonBranch();
		setUpDeleteCaseForNonDeletableCommonBranch();
		
		try {
			nonCommonBranchToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Failed to dereference DegreeCurricularPlan", nonCommonBranchToDelete.hasDegreeCurricularPlan());
		assertFalse("Failed to dereference CurricularCourseScopes", nonCommonBranchToDelete.hasAnyScopes());
		assertFalse("Failed to dereference CurricularCourseGroups", nonCommonBranchToDelete.hasAnyCurricularCourseGroups());
		assertFalse("Failed to dereference StudentCurricularPlans", nonCommonBranchToDelete.hasAnyStudentCurricularPlans());
		assertFalse("Failed to dereference secondary StudentCurricularPlans: LEEC", nonCommonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertFalse("Failed to dereference secundary StudentCurricularPlans: LEIC", nonCommonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
		
		for (CurricularCourseScope ccs : curricularCourseScopesToCommonBranch) {
			assertTrue("Failed to switch CurricularCourseScope's Branch to common branch", ccs.getBranch().representsCommonBranch());
		}

		for (CurricularCourseScope ccs : curricularCourseScopesToDelete) {
			assertFalse("Failed to dereference CurricularCourseScope's Branch", ccs.hasBranch());
			assertFalse("Failed to dereference CurricularCourseScope's CurricularCourse", ccs.hasCurricularCourse());
			
			for (CurricularCourse cc : curricularCoursesWithOneHavingCCScopeInCommonBranch) {
				assertFalse("CurricularCourse should not have CurricularCourseScopes that will be deleted.", cc.hasScopes(ccs));
			}
		}
		
		for (CurricularCourseGroup ccg : curricularCourseGroupsToCommonBranch) {
			assertTrue("Failed to switch CurricularCourseGroup's Branch to common branch", ccg.getBranch().representsCommonBranch());
		}
		
		for (StudentCurricularPlan scp : studentCurricularPlans) {
			assertFalse("Failed to dereference StudentCurricularPlan from Branch", scp.hasBranch());
		}
		
		for (StudentCurricularPlanLEIC scp : studentCurricularPlansLEIC) {
			assertFalse("Failed to dereference StudentCurricularPlanLEIC from Branch", scp.hasBranch());
			assertFalse("Failed to dereference StudentCurricularPlanLEIC from secondary Branch", scp.hasSecundaryBranch());
		}
		
		for (StudentCurricularPlanLEEC scp : studentCurricularPlansLEEC) {
			assertFalse("Failed to dereference StudentCurricularPlanLEEC from Branch", scp.hasBranch());
			assertFalse("Failed to dereference StudentCurricularPlanLEEC from secondary Branch", scp.hasSecundaryBranch());
		}
		
		
		
		

		
		try {
			commonBranchToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Failed to dereference DegreeCurricularPlan", commonBranchToDelete.hasDegreeCurricularPlan());
		assertFalse("Failed to dereference StudentCurricularPlans", commonBranchToDelete.hasAnyStudentCurricularPlans());
		assertFalse("Failed to dereference secondary StudentCurricularPlans: LEEC", commonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertFalse("Failed to dereference secondary StudentCurricularPlans: LEIC", commonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
		

		try {
			nonCommonBranchNotToDeleteWithProposals.delete();
			fail("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {
		}
		
		assertTrue("Should not have dereferenced DegreeCurricularPlan", nonCommonBranchNotToDeleteWithProposals.hasDegreeCurricularPlan());
		assertTrue("Should not have dereferenced CurricularCourseScopes", nonCommonBranchNotToDeleteWithProposals.hasAnyScopes());
		assertTrue("Should not have dereferenced CurricularCourseGroups", nonCommonBranchNotToDeleteWithProposals.hasAnyCurricularCourseGroups());
		assertTrue("Should not have dereferenced StudentCurricularPlans", nonCommonBranchNotToDeleteWithProposals.hasAnyStudentCurricularPlans());
		assertTrue("Should not have dereferenced secondary StudentCurricularPlans: LEEC", nonCommonBranchNotToDeleteWithProposals.hasAnySecundaryStudentCurricularPlansLEEC());
		assertTrue("Should not have dereferenced secondary StudentCurricularPlans: LEIC", nonCommonBranchNotToDeleteWithProposals.hasAnySecundaryStudentCurricularPlansLEIC());
		assertTrue("Should not have dereferenced FinalDegreeWorkProposals", nonCommonBranchNotToDeleteWithProposals.hasAnyAssociatedFinalDegreeWorkProposals());
	
		
		try {
			commonBranchNotToDelete.delete();
			fail("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {

		}
		
		assertTrue("Should not have dereferenced DegreeCurricularPlan", commonBranchNotToDelete.hasDegreeCurricularPlan());
		assertTrue("Should not have dereferenced CurricularCourseScopes", commonBranchNotToDelete.hasAnyScopes());
		assertTrue("Should not have dereferenced CurricularCourseGroups", commonBranchNotToDelete.hasAnyCurricularCourseGroups());
		assertTrue("Should not have dereferenced StudentCurricularPlans", commonBranchNotToDelete.hasAnyStudentCurricularPlans());
		assertTrue("Should not have dereferenced secondary StudentCurricularPlans: LEEC", commonBranchNotToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertTrue("Should not have dereferenced secondary StudentCurricularPlans: LEIC", commonBranchNotToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
	}
	
	public void testEdit() {
		
		setUpEditCase();
		
		branchToEdit.edit(editedName,editedNameEn,editedCode);
		
		assertEquals("Edited name does not match", branchToEdit.getName(),editedName);
		assertEquals("Edited nameEn does not match", branchToEdit.getNameEn(),editedNameEn);
		assertEquals("Edited code does not match", branchToEdit.getCode(),editedCode);
	}
	
	public void testCreate() {
		
		setUpCreateCase();
		
		branchToCreate = new Branch(createdBranchName,createdBranchNameEn,createdBranchCode,createdBranchDCP);
		
		assertEquals ("Failed to assign name", branchToCreate.getName(),createdBranchName);
		assertEquals ("Failed to assign nameEn", branchToCreate.getNameEn(),createdBranchNameEn);
		assertEquals ("Failed to assign code", branchToCreate.getCode(),createdBranchCode);
		assertEquals ("Failed to assign DegreeCurricularPlan", branchToCreate.getDegreeCurricularPlan(),createdBranchDCP);
	}
}


