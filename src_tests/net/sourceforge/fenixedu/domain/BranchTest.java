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
	
	protected void setUp() throws Exception {
		super.setUp();
		nonCommonBranchToDelete = new Branch();
		nonCommonBranchToDelete.setIdInternal(1);
		nonCommonBranchToDelete.setBranchType(BranchType.SECNBR);
		nonCommonBranchToDelete.setName("Generic Branch To Delete");
		
		commonBranchInOtherDegreeCurricularPlan = new Branch();
		commonBranchInOtherDegreeCurricularPlan.setIdInternal(2);
		commonBranchInOtherDegreeCurricularPlan.setBranchType(BranchType.COMNBR);
		commonBranchInOtherDegreeCurricularPlan.setName("");
		
		commonBranchInSameDegreeCurricularPlan = new Branch();
		commonBranchInSameDegreeCurricularPlan.setIdInternal(3);
		commonBranchInSameDegreeCurricularPlan.setBranchType(BranchType.COMNBR);
		commonBranchInSameDegreeCurricularPlan.setName("");
		
		nonCommonBranchNotToDeleteWithProposals = new Branch();
		nonCommonBranchNotToDeleteWithProposals.setIdInternal(4);
		nonCommonBranchNotToDeleteWithProposals.setBranchType(BranchType.SECNBR);
		nonCommonBranchNotToDeleteWithProposals.setName("Branch not to delete because of associated final degree work proposals");
		
		IDegreeCurricularPlan dcp = new DegreeCurricularPlan();
		dcp.setIdInternal(1);
		nonCommonBranchToDelete.setDegreeCurricularPlan(dcp);
		commonBranchInSameDegreeCurricularPlan.setDegreeCurricularPlan(dcp);
		degreeCurricularPlan = dcp;
		
		IDegreeCurricularPlan otherDCP = new DegreeCurricularPlan();
		otherDCP.setIdInternal(2);
		commonBranchInOtherDegreeCurricularPlan.setDegreeCurricularPlan(otherDCP);
		
		ICurricularCourse cc1 = new CurricularCourse();
		cc1.setIdInternal(1);
		ICurricularCourse cc2 = new CurricularCourse();
		cc2.setIdInternal(2);
		ICurricularCourse cc3 = new CurricularCourse();
		cc3.setIdInternal(3);
		
		ICurricularCourseScope ccs1 = new CurricularCourseScope();
		ccs1.setIdInternal(1);
		ccs1.setBranch(nonCommonBranchToDelete);
		ccs1.setCurricularCourse(cc1);
		curricularCoursesWithNoneHavingCCScopeInCommonBranch.add(cc1);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc1);
		curricularCourseScopesToCommonBranch.add(ccs1);

		ICurricularCourseScope ccs2 = new CurricularCourseScope();
		ccs2.setIdInternal(2);
		ccs2.setBranch(nonCommonBranchToDelete);
		ccs2.setCurricularCourse(cc2);
		curricularCoursesWithNoneHavingCCScopeInCommonBranch.add(cc2);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc2);
		curricularCourseScopesToCommonBranch.add(ccs2);

		ICurricularCourseScope ccs3 = new CurricularCourseScope();
		ccs3.setIdInternal(3);
		ccs3.setBranch(nonCommonBranchToDelete);
		ccs3.setCurricularCourse(cc3);
		curricularCourseScopesToDelete.add(ccs3);

		ICurricularCourseScope ccs4 = new CurricularCourseScope();
		ccs4.setIdInternal(4);
		ccs4.setBranch(commonBranchInSameDegreeCurricularPlan);
		ccs4.setCurricularCourse(cc3);
		curricularCoursesWithOneHavingCCScopeInCommonBranch.add(cc3);
		
		ICurricularCourseScope ccs5 = new CurricularCourseScope();
		ccs5.setIdInternal(5);
		ccs5.setBranch(commonBranchInOtherDegreeCurricularPlan);
		ccs5.setCurricularCourse(cc2);

		ICurricularCourseGroup ccg1 = new OptionalCurricularCourseGroup();
		ccg1.setIdInternal(1);
		nonCommonBranchToDelete.addOptionalCurricularCourseGroups((IOptionalCurricularCourseGroup)ccg1);
		nonCommonBranchToDelete.addCurricularCourseGroups(ccg1);
		curricularCourseGroupsToCommonBranch.add(ccg1);
		optionalCurricularCourseGroupsToCommonBranch.add((IOptionalCurricularCourseGroup)ccg1);
		
		ICurricularCourseGroup ccg2 = new AreaCurricularCourseGroup();
		ccg2.setIdInternal(2);
		nonCommonBranchToDelete.addAreaCurricularCourseGroups((IAreaCurricularCourseGroup) ccg2);
		nonCommonBranchToDelete.addCurricularCourseGroups(ccg2);
		curricularCourseGroupsToCommonBranch.add(ccg2);
		areaCurricularCourseGroupsToCommonBranch.add((IAreaCurricularCourseGroup)ccg2);
		
		
		IStudentCurricularPlan scp1 = new StudentCurricularPlan();
		scp1.setIdInternal(1);
		scp1.setBranch(nonCommonBranchToDelete);
		studentCurricularPlans.add(scp1);
		
		IStudentCurricularPlanLEIC scp2 = new StudentCurricularPlanLEIC();
		scp2.setIdInternal(2);
		scp2.setBranch(nonCommonBranchToDelete);
		scp2.setSecundaryBranch(nonCommonBranchToDelete);
		studentCurricularPlansLEIC.add(scp2);
		
		IStudentCurricularPlanLEEC scp3 = new StudentCurricularPlanLEEC();
		scp3.setIdInternal(3);
		scp3.setBranch(nonCommonBranchToDelete);
		scp3.setSecundaryBranch(nonCommonBranchToDelete);
		studentCurricularPlansLEEC.add(scp3);
		
		
		/* initialization of nonCommonBranchNotToDeleteWithProposals */
		{
		IProposal proposal1 = new Proposal();
		proposal1.setIdInternal(1);
		nonCommonBranchNotToDeleteWithProposals.addAssociatedFinalDegreeWorkProposals(proposal1);
		
		IOptionalCurricularCourseGroup opCCG = new OptionalCurricularCourseGroup();
		opCCG.setIdInternal(20);
		opCCG.setBranchForWhichThisIsOptionalCurricularCourseGroup(nonCommonBranchNotToDeleteWithProposals);
		opCCG.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IAreaCurricularCourseGroup areaCCG = new AreaCurricularCourseGroup();
		areaCCG.setIdInternal(20);
		areaCCG.setBranchForWhichThisIsAreaCurricularCourseGroup(nonCommonBranchNotToDeleteWithProposals);
		areaCCG.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IStudentCurricularPlan scpW = new StudentCurricularPlan();
		scpW.setIdInternal(20);
		scpW.setBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IStudentCurricularPlanLEIC scpWLEIC = new StudentCurricularPlanLEIC();
		scpWLEIC.setIdInternal(21);
		scpWLEIC.setSecundaryBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IStudentCurricularPlanLEEC scpWLEEC = new StudentCurricularPlanLEEC();
		scpWLEEC.setIdInternal(22);
		scpWLEEC.setSecundaryBranch(nonCommonBranchNotToDeleteWithProposals);
		
		IDegreeCurricularPlan dcpW = new DegreeCurricularPlan();
		dcpW.setIdInternal(20);
		dcpW.addAreas(nonCommonBranchNotToDeleteWithProposals);
		
		ICurricularCourseScope ccsW = new CurricularCourseScope();
		ccsW.setIdInternal(20);
		ccsW.setBranch(nonCommonBranchNotToDeleteWithProposals);
		}
		
		/* initialization of commonBranchToDelete */
		{
		commonBranchToDelete = new Branch();
		commonBranchToDelete.setIdInternal(30);
		commonBranchToDelete.setBranchType(BranchType.COMNBR);
		commonBranchToDelete.setName("");
		
		IStudentCurricularPlan scpD = new StudentCurricularPlan();
		scpD.setIdInternal(30);
		scpD.setBranch(commonBranchToDelete);
		studentCurricularPlansInCommonBranchToDelete.add(scpD);
		
		IStudentCurricularPlanLEIC scpDLEIC = new StudentCurricularPlanLEIC();
		scpDLEIC.setIdInternal(31);
		scpDLEIC.setSecundaryBranch(commonBranchToDelete);
		studentCurricularPlansLEICInCommonBranchToDelete.add(scpDLEIC);
		
		IStudentCurricularPlanLEEC scpDLEEC = new StudentCurricularPlanLEEC();
		scpDLEEC.setIdInternal(32);
		scpDLEEC.setSecundaryBranch(commonBranchToDelete);
		studentCurricularPlansLEECInCommonBranchToDelete.add(scpDLEEC);
		
		IDegreeCurricularPlan dcpD = new DegreeCurricularPlan();
		dcpD.setIdInternal(30);
		dcpD.addAreas(commonBranchToDelete);
		degreeCurricularPlanInCommonBranchToDelete = dcpD;
		}
		
		/* initialization of commonBranchNotToDelete */
		{
			commonBranchNotToDelete = new Branch();
			commonBranchNotToDelete.setIdInternal(40);
			commonBranchNotToDelete.setBranchType(BranchType.COMNBR);
			commonBranchNotToDelete.setName("");
			
			IOptionalCurricularCourseGroup opCCG = new OptionalCurricularCourseGroup();
			opCCG.setIdInternal(40);
			opCCG.setBranchForWhichThisIsOptionalCurricularCourseGroup(commonBranchNotToDelete);
			opCCG.setBranch(commonBranchNotToDelete);
			
			IAreaCurricularCourseGroup areaCCG = new AreaCurricularCourseGroup();
			areaCCG.setIdInternal(40);
			areaCCG.setBranchForWhichThisIsAreaCurricularCourseGroup(commonBranchNotToDelete);
			areaCCG.setBranch(commonBranchNotToDelete);
			
			IStudentCurricularPlan scpW = new StudentCurricularPlan();
			scpW.setIdInternal(40);
			scpW.setBranch(commonBranchNotToDelete);
			
			IStudentCurricularPlanLEIC scpWLEIC = new StudentCurricularPlanLEIC();
			scpWLEIC.setIdInternal(41);
			scpWLEIC.setSecundaryBranch(commonBranchNotToDelete);
			
			IStudentCurricularPlanLEEC scpWLEEC = new StudentCurricularPlanLEEC();
			scpWLEEC.setIdInternal(42);
			scpWLEEC.setSecundaryBranch(commonBranchNotToDelete);
			
			IDegreeCurricularPlan dcpW = new DegreeCurricularPlan();
			dcpW.setIdInternal(40);
			dcpW.addAreas(commonBranchNotToDelete);
			
			ICurricularCourseScope ccsW = new CurricularCourseScope();
			ccsW.setIdInternal(40);
			ccsW.setBranch(commonBranchNotToDelete);
		}
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
		assertFalse(nonCommonBranchToDelete.hasAnyOptionalCurricularCourseGroups());
		assertFalse(nonCommonBranchToDelete.hasAnyAreaCurricularCourseGroups());
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
		
		for (IOptionalCurricularCourseGroup ccg : optionalCurricularCourseGroupsToCommonBranch) {
			assertTrue(ccg.getBranchForWhichThisIsOptionalCurricularCourseGroup().representsCommonBranch());
		}
		
		for (IAreaCurricularCourseGroup ccg : areaCurricularCourseGroupsToCommonBranch) {
			assertTrue(ccg.getBranchForWhichThisIsAreaCurricularCourseGroup().representsCommonBranch());
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
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnyOptionalCurricularCourseGroups());
		assertTrue(nonCommonBranchNotToDeleteWithProposals.hasAnyAreaCurricularCourseGroups());
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
		assertTrue(commonBranchNotToDelete.hasAnyOptionalCurricularCourseGroups());
		assertTrue(commonBranchNotToDelete.hasAnyAreaCurricularCourseGroups());
		assertTrue(commonBranchNotToDelete.hasAnyStudentCurricularPlans());
		assertTrue(commonBranchNotToDelete.hasAnySecundaryStudentCurricularPlansLEEC());
		assertTrue(commonBranchNotToDelete.hasAnySecundaryStudentCurricularPlansLEIC());
	}
}

