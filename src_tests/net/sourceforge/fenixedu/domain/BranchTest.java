package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class BranchTest extends DomainTestBase {

	private IBranch nonCommonBranchToDelete = null;
	private IBranch commonBranchInOtherDegreeCurricularPlan = null;
	private IBranch commonBranchInSameDegreeCurricularPlan = null;
	private List<ICurricularCourse> curricularCoursesWithOneHavingCCScopeInCommonBranch = new ArrayList();
	private List<ICurricularCourse> curricularCoursesWithNoneHavingCCScopeInCommonBranch = new ArrayList();
	private List<ICurricularCourseGroup> curricularCourseGroupsToCommonBranch = new ArrayList();
	private List<IOptionalCurricularCourseGroup> optionalCurricularCourseGroupsToCommonBranch = new ArrayList();
	private List<IAreaCurricularCourseGroup> areaCurricularCourseGroupsToCommonBranch = new ArrayList();
	private List<IStudentCurricularPlan> studentCurricularPlans = new ArrayList();
	private List<IStudentCurricularPlanLEEC> studentCurricularPlansLEEC = new ArrayList();
	private List<IStudentCurricularPlanLEIC> studentCurricularPlansLEIC = new ArrayList();
	private List<ICurricularCourseScope> curricularCourseScopesToDelete = new ArrayList();
	private List<ICurricularCourseScope> curricularCourseScopesToCommonBranch = new ArrayList();

	private IBranch nonCommonBranchNotToDeleteWithProposals = null;
	
	private IBranch commonBranchToDelete = null;
	private List<IStudentCurricularPlan> studentCurricularPlansInCommonBranchToDelete = new ArrayList();
	private List<IStudentCurricularPlanLEEC> studentCurricularPlansLEECInCommonBranchToDelete = new ArrayList();
	private List<IStudentCurricularPlanLEIC> studentCurricularPlansLEICInCommonBranchToDelete = new ArrayList();
	
	private IBranch commonBranchNotToDelete = null;
	
	private IBranch branchToEdit = null;
	private String editedName = "";
	private String editedNameEn = "";
	private String editedCode = "";
	
	private IBranch branchToCreate = null;
	private String createdBranchName = "";
	private String createdBranchNameEn = "";
	private String createdBranchCode = "";
	private IDegreeCurricularPlan createdBranchDCP = null;
	
	private IBranch createBranchWithNameAndType(BranchType branchType, String name) {
		IBranch br = new Branch();
		br.setBranchType(branchType);
		br.setName(name);
		
		return br;
	}
	
	private void setUpDeleteCaseForNonDeletableCommonBranch() {
		commonBranchNotToDelete = createBranchWithNameAndType(BranchType.COMNBR,"");		
		
		IOptionalCurricularCourseGroup opCCG = new OptionalCurricularCourseGroup();
		opCCG.setBranch(commonBranchNotToDelete);
		
		IAreaCurricularCourseGroup areaCCG = new AreaCurricularCourseGroup();
		areaCCG.setBranch(commonBranchNotToDelete);
		
		IStudentCurricularPlan scpW = new StudentCurricularPlan();
		scpW.setBranch(commonBranchNotToDelete);
		
		IStudentCurricularPlanLEIC scpWLEIC = new StudentCurricularPlanLEIC();
		scpWLEIC.setSecundaryBranch(commonBranchNotToDelete);
		
		IStudentCurricularPlanLEEC scpWLEEC = new StudentCurricularPlanLEEC();
		scpWLEEC.setSecundaryBranch(commonBranchNotToDelete);
		
		IDegreeCurricularPlan dcpW = new DegreeCurricularPlan();
		dcpW.addAreas(commonBranchNotToDelete);
		
		ICurricularCourseScope ccsW = new CurricularCourseScope();
		ccsW.setBranch(commonBranchNotToDelete);
	}

	private void setUpDeleteCaseForCommonBranch() {
		commonBranchToDelete = createBranchWithNameAndType(BranchType.COMNBR,"");
		
		IStudentCurricularPlan scpD = new StudentCurricularPlan();
		scpD.setBranch(commonBranchToDelete);
		studentCurricularPlansInCommonBranchToDelete.add(scpD);
		
		IStudentCurricularPlanLEIC scpDLEIC = new StudentCurricularPlanLEIC();
		scpDLEIC.setSecundaryBranch(commonBranchToDelete);
		studentCurricularPlansLEICInCommonBranchToDelete.add(scpDLEIC);
		
		IStudentCurricularPlanLEEC scpDLEEC = new StudentCurricularPlanLEEC();
		scpDLEEC.setSecundaryBranch(commonBranchToDelete);
		studentCurricularPlansLEECInCommonBranchToDelete.add(scpDLEEC);
		
		IDegreeCurricularPlan dcpD = new DegreeCurricularPlan();
		dcpD.addAreas(commonBranchToDelete);
	}

	private void setUpDeleteCaseForNonDeletableBranchWithProposals() {
		nonCommonBranchNotToDeleteWithProposals = createBranchWithNameAndType(BranchType.SECNBR,"Branch not to delete because of associated final degree work proposals");
		
		IProposal proposal1 = new Proposal();
		nonCommonBranchNotToDeleteWithProposals.addAssociatedFinalDegreeWorkProposals(proposal1);
		
		IOptionalCurricularCourseGroup opCCG = new OptionalCurricularCourseGroup();
		opCCG.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IAreaCurricularCourseGroup areaCCG = new AreaCurricularCourseGroup();
		areaCCG.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IStudentCurricularPlan scpW = new StudentCurricularPlan();
		scpW.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IStudentCurricularPlanLEIC scpWLEIC = new StudentCurricularPlanLEIC();
		scpWLEIC.setSecundaryBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IStudentCurricularPlanLEEC scpWLEEC = new StudentCurricularPlanLEEC();
		scpWLEEC.setSecundaryBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IDegreeCurricularPlan dcpW = new DegreeCurricularPlan();
		dcpW.addAreas(nonCommonBranchNotToDeleteWithProposals);
		
		ICurricularCourseScope ccsW = new CurricularCourseScope();
		ccsW.setBranch(nonCommonBranchNotToDeleteWithProposals);
	}

	private void setUpDeleteCaseForNonCommonBranch() {
		nonCommonBranchToDelete = createBranchWithNameAndType(BranchType.SECNBR,"Generic Branch To Delete");
		
		commonBranchInOtherDegreeCurricularPlan = createBranchWithNameAndType(BranchType.COMNBR,"");
		
		commonBranchInSameDegreeCurricularPlan = createBranchWithNameAndType(BranchType.COMNBR,"");
		
		IDegreeCurricularPlan dcp = new DegreeCurricularPlan();
		nonCommonBranchToDelete.setDegreeCurricularPlan(dcp);
		commonBranchInSameDegreeCurricularPlan.setDegreeCurricularPlan(dcp);
		
		IDegreeCurricularPlan otherDCP = new DegreeCurricularPlan();
		commonBranchInOtherDegreeCurricularPlan.setDegreeCurricularPlan(otherDCP);
		
		ICurricularCourse cc1 = new CurricularCourse();
		ICurricularCourse cc2 = new CurricularCourse();
		ICurricularCourse cc3 = new CurricularCourse();
		
		ICurricularCourseScope ccs1 = new CurricularCourseScope();
		ccs1.setBranch(nonCommonBranchToDelete);
		ccs1.setCurricularCourse(cc1);
		curricularCoursesWithNoneHavingCCScopeInCommonBranch.add(cc1);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc1);
		curricularCourseScopesToCommonBranch.add(ccs1);

		ICurricularCourseScope ccs2 = new CurricularCourseScope();
		ccs2.setBranch(nonCommonBranchToDelete);
		ccs2.setCurricularCourse(cc2);
		curricularCoursesWithNoneHavingCCScopeInCommonBranch.add(cc2);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc2);
		curricularCourseScopesToCommonBranch.add(ccs2);

		ICurricularCourseScope ccs3 = new CurricularCourseScope();
		ccs3.setBranch(nonCommonBranchToDelete);
		ccs3.setCurricularCourse(cc3);
		curricularCourseScopesToDelete.add(ccs3);

		ICurricularCourseScope ccs4 = new CurricularCourseScope();
		ccs4.setBranch(commonBranchInSameDegreeCurricularPlan);
		ccs4.setCurricularCourse(cc3);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc3);
		
		ICurricularCourseScope ccs5 = new CurricularCourseScope();
		ccs5.setBranch(commonBranchInOtherDegreeCurricularPlan);
		ccs5.setCurricularCourse(cc2);

		ICurricularCourseGroup ccg1 = new OptionalCurricularCourseGroup();
		nonCommonBranchToDelete.addCurricularCourseGroups(ccg1);
		curricularCourseGroupsToCommonBranch.add(ccg1);
		optionalCurricularCourseGroupsToCommonBranch.add((IOptionalCurricularCourseGroup)ccg1);
		
		ICurricularCourseGroup ccg2 = new AreaCurricularCourseGroup();
		nonCommonBranchToDelete.addCurricularCourseGroups(ccg2);
		curricularCourseGroupsToCommonBranch.add(ccg2);
		areaCurricularCourseGroupsToCommonBranch.add((IAreaCurricularCourseGroup)ccg2);
		
		
		IStudentCurricularPlan scp1 = new StudentCurricularPlan();
		scp1.setBranch(nonCommonBranchToDelete);
		studentCurricularPlans.add(scp1);
		
		IStudentCurricularPlanLEIC scp2 = new StudentCurricularPlanLEIC();
		scp2.setBranch(nonCommonBranchToDelete);
		scp2.setSecundaryBranch(nonCommonBranchToDelete);
		studentCurricularPlansLEIC.add(scp2);
		
		IStudentCurricularPlanLEEC scp3 = new StudentCurricularPlanLEEC();
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
		
		for (ICurricularCourseScope ccs : curricularCourseScopesToCommonBranch) {
			assertTrue("Failed to switch CurricularCourseScope's Branch to common branch", ccs.getBranch().representsCommonBranch());
		}

		for (ICurricularCourseScope ccs : curricularCourseScopesToDelete) {
			assertFalse("Failed to dereference CurricularCourseScope's Branch", ccs.hasBranch());
			assertFalse("Failed to dereference CurricularCourseScope's CurricularCourse", ccs.hasCurricularCourse());
			
			for (ICurricularCourse cc : curricularCoursesWithOneHavingCCScopeInCommonBranch) {
				assertFalse("CurricularCourse should not have CurricularCourseScopes that will be deleted.", cc.hasScopes(ccs));
			}
		}
		
		for (ICurricularCourseGroup ccg : curricularCourseGroupsToCommonBranch) {
			assertTrue("Failed to switch CurricularCourseGroup's Branch to common branch", ccg.getBranch().representsCommonBranch());
		}
		
		for (IStudentCurricularPlan scp : studentCurricularPlans) {
			assertFalse("Failed to dereference StudentCurricularPlan from Branch", scp.hasBranch());
		}
		
		for (IStudentCurricularPlanLEIC scp : studentCurricularPlansLEIC) {
			assertFalse("Failed to dereference StudentCurricularPlanLEIC from Branch", scp.hasBranch());
			assertFalse("Failed to dereference StudentCurricularPlanLEIC from secondary Branch", scp.hasSecundaryBranch());
		}
		
		for (IStudentCurricularPlanLEEC scp : studentCurricularPlansLEEC) {
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


