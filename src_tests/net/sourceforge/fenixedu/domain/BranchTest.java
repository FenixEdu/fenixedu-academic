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
	private IDegreeCurricularPlan degreeCurricularPlan = null;
	private List<IProposal> proposals = new ArrayList();
	private List<ICurricularCourseScope> curricularCourseScopesToDelete = new ArrayList();
	private List<ICurricularCourseScope> curricularCourseScopesToCommonBranch = new ArrayList();

	private IBranch nonCommonBranchNotToDeleteWithProposals = null;
	
	private IBranch commonBranchToDelete = null;
	private List<IStudentCurricularPlan> studentCurricularPlansInCommonBranchToDelete = new ArrayList();
	private List<IStudentCurricularPlanLEEC> studentCurricularPlansLEECInCommonBranchToDelete = new ArrayList();
	private List<IStudentCurricularPlanLEIC> studentCurricularPlansLEICInCommonBranchToDelete = new ArrayList();
	private IDegreeCurricularPlan degreeCurricularPlanInCommonBranchToDelete = null;
	
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
	
	protected void setUp() throws Exception {
		super.setUp();
		setUpDeleteCaseForNonCommonBranch();
	
		setUpDeleteCaseForNonDeletableBranchWithProposals();
		
		setUpDeleteCaseForCommonBranch();
		
		setUpDeleteCaseForNonDeletableCommonBranch();
		
		setUpEditCase();
		
		setUpCreateCase();
	}

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
		degreeCurricularPlanInCommonBranchToDelete = dcpD;
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
		degreeCurricularPlan = dcp;
		
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
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete() {
		try {
			nonCommonBranchToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(degreeCurricularPlan.hasAreas(nonCommonBranchToDelete));
		
		assertFalse(nonCommonBranchToDelete.hasDegreeCurricularPlan());
		assertFalse(nonCommonBranchToDelete.hasAnyScopes());
		assertFalse(nonCommonBranchToDelete.hasAnyCurricularCourseGroups());
		assertFalse(nonCommonBranchToDelete.hasAnyStudentCurricularPlans());
		assertFalse(nonCommonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertFalse(nonCommonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
		
		for (ICurricularCourseScope ccs : curricularCourseScopesToCommonBranch) {
			assertTrue(ccs.getBranch().representsCommonBranch());
		}

		for (ICurricularCourseScope ccs : curricularCourseScopesToDelete) {
			assertFalse(ccs.hasBranch());
			assertFalse(ccs.hasCurricularCourse());
			
			for (ICurricularCourse cc : curricularCoursesWithOneHavingCCScopeInCommonBranch) {
				assertFalse(cc.hasScopes(ccs));
			}
		}
		
		for (ICurricularCourseGroup ccg : curricularCourseGroupsToCommonBranch) {
			assertTrue(ccg.getBranch().representsCommonBranch());
		}
		
		for (IStudentCurricularPlan scp : studentCurricularPlans) {
			assertFalse(scp.hasBranch());
		}
		
		for (IStudentCurricularPlanLEIC scp : studentCurricularPlansLEIC) {
			assertFalse(scp.hasBranch());
			assertFalse(scp.hasSecundaryBranch());
		}
		
		for (IStudentCurricularPlanLEEC scp : studentCurricularPlansLEEC) {
			assertFalse(scp.hasBranch());
			assertFalse(scp.hasSecundaryBranch());
		}
		
		
		
		
		try {
			nonCommonBranchNotToDeleteWithProposals.delete();
			fail("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {
		}
		
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasDegreeCurricularPlan());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnyScopes());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnyCurricularCourseGroups());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnyStudentCurricularPlans());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnySecundaryStudentCurricularPlansLEEC());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnySecundaryStudentCurricularPlansLEIC());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnyAssociatedFinalDegreeWorkProposals());
		
		
		
		try {
			commonBranchToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(degreeCurricularPlanInCommonBranchToDelete.hasAreas(commonBranchToDelete));
		
		assertFalse(commonBranchToDelete.hasDegreeCurricularPlan());
		assertFalse(commonBranchToDelete.hasAnyStudentCurricularPlans());
		assertFalse(commonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertFalse(commonBranchToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
		
		
		
		try {
			commonBranchNotToDelete.delete();
			fail("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {

		}
		
		assertTrue(commonBranchNotToDelete.hasDegreeCurricularPlan());
		assertTrue(commonBranchNotToDelete.hasAnyScopes());
		assertTrue(commonBranchNotToDelete.hasAnyCurricularCourseGroups());
		assertTrue(commonBranchNotToDelete.hasAnyStudentCurricularPlans());
		assertTrue(commonBranchNotToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertTrue(commonBranchNotToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
	}
	
	public void testEdit() {
		branchToEdit.edit(editedName,editedNameEn,editedCode);
		
		assertEquals(branchToEdit.getName(),editedName);
		assertEquals(branchToEdit.getNameEn(),editedNameEn);
		assertEquals(branchToEdit.getCode(),editedCode);
	}
	
	public void testCreate() {
		branchToCreate = new Branch(createdBranchName,createdBranchNameEn,createdBranchCode,createdBranchDCP);
		
		assertEquals (branchToCreate.getName(),createdBranchName);
		assertEquals (branchToCreate.getNameEn(),createdBranchNameEn);
		assertEquals (branchToCreate.getCode(),createdBranchCode);
		assertEquals (branchToCreate.getDegreeCurricularPlan(),createdBranchDCP);
	}
}


