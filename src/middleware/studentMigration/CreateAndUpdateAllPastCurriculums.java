package middleware.studentMigration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWCurricularCourseScope;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;

import org.apache.commons.lang.StringUtils;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IUniversity;
import Dominio.University;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentUniversity;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class CreateAndUpdateAllPastCurriculums
{
	private static int totalDegreeCurricularPlansCreated = 0;
	private static int totalCurricularCoursesCreated = 0;
	private static int totalCurricularCourseScopesCreated = 0;
	private static int totalBranchesCreated = 0;
	private static int totalMWCurricularCourseScopesRead = 0;
	private static int totalUniversitysCreated = 0;

	public static void main(String args[])
	{
		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWCurricularCourseScope persistentMWCurricularCourseScope = mws.getIPersistentMWCurricularCourseScope();
	
			fenixPersistentSuport.iniciarTransaccao();
	
			Integer numberOfMWCurricularCourseScopes = persistentMWCurricularCourseScope.countAll();
	
			fenixPersistentSuport.confirmarTransaccao();
	
			int numberOfElementsInSpan = 100;
			int numberOfSpans = numberOfMWCurricularCourseScopes.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfMWCurricularCourseScopes.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
	
			for (int span = 0; span < numberOfSpans; span++) {
				fenixPersistentSuport.iniciarTransaccao();
				fenixPersistentSuport.clearCache();	
	
				System.out.println("[INFO] Reading MWCurricularCourseScopes...");
				List result = persistentMWCurricularCourseScope.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
				CreateAndUpdateAllPastCurriculums.totalMWCurricularCourseScopesRead = CreateAndUpdateAllPastCurriculums.totalMWCurricularCourseScopesRead + result.size();
	
				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Read [" + result.size() + "] MWCurricularCourseScopes...");
		
				Iterator iterator = result.iterator();
				while (iterator.hasNext()) {
					
					MWCurricularCourseScope mwCurricularCourseScope = (MWCurricularCourseScope) iterator.next();
	
					fenixPersistentSuport.iniciarTransaccao();
	
					CreateAndUpdateAllPastCurriculums.writeAndUpdate(mwCurricularCourseScope, fenixPersistentSuport);

					fenixPersistentSuport.confirmarTransaccao();
				}
			}
			
			CreateAndUpdateAllPastCurriculums.createAndUpdateUniversitys(mws, fenixPersistentSuport);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total DegreeCurricularPlans created: [" + CreateAndUpdateAllPastCurriculums.totalDegreeCurricularPlansCreated + "].");
		System.out.println("[INFO] Total CurricularCourses created: [" + CreateAndUpdateAllPastCurriculums.totalCurricularCoursesCreated + "].");
		System.out.println("[INFO] Total CurricularCourseScopes created: [" + CreateAndUpdateAllPastCurriculums.totalCurricularCourseScopesCreated + "].");
		System.out.println("[INFO] Total Branches created: [" + CreateAndUpdateAllPastCurriculums.totalBranchesCreated + "].");
		System.out.println("[INFO] Total Universitys created: [" + CreateAndUpdateAllPastCurriculums.totalUniversitysCreated + "].");
		System.out.println("[INFO] Total MWCurricularCourseScopes read: [" + CreateAndUpdateAllPastCurriculums.totalMWCurricularCourseScopesRead + "].");

	}

	/**
	 * @param mwCurricularCourseScope
	 * @param fenixPersistentSuport
	 * @throws Exception
	 */
	private static void writeAndUpdate(MWCurricularCourseScope mwCurricularCourseScope, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		try {
			IDegreeCurricularPlan degreeCurricularPlan = CreateAndUpdateAllPastCurriculums.getDegreeCurricularPlan(mwCurricularCourseScope.getDegreecode(), fenixPersistentSuport);
			if (degreeCurricularPlan == null) {
				// This should NEVER happen!
				System.out.println("[ERROR 01] No record of Degree with code: [" + mwCurricularCourseScope.getDegreecode() + "]! ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
				return;
			}

			IBranch branch = CreateAndUpdateAllPastCurriculums.getBranch(mwCurricularCourseScope.getDegreecode(), mwCurricularCourseScope.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
			if (branch == null) {
				branch = CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees6And1(mwCurricularCourseScope, degreeCurricularPlan, fenixPersistentSuport);
				if (branch == null) {
					// This should NEVER happen!
					System.out.println("[ERROR 02] No record of Branch with code: [" + mwCurricularCourseScope.getBranchcode() + "] for Degree with code: [" + mwCurricularCourseScope.getDegreecode() + "]! ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
					return;
				}
			}

			ICurricularCourse curricularCourse = CreateAndUpdateAllPastCurriculums.getCurricularCourse(mwCurricularCourseScope.getCoursecode(), degreeCurricularPlan, fenixPersistentSuport);
			if (curricularCourse == null) {
				// This should NEVER happen!
				System.out.println("[ERROR 03] No record of CurricularCourse with code: [" + mwCurricularCourseScope.getCoursecode() + "]! ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
				return;
			}

			ICurricularCourseScope curricularCourseScope = CreateAndUpdateAllPastCurriculums.getCurricularCourseScope(mwCurricularCourseScope, curricularCourse, branch, fenixPersistentSuport);
			if (curricularCourseScope == null) {
				// This should NEVER happen!
				System.out.print("[ERROR 04] No record of CurricularCourseScope with data: ");
				System.out.print("Course Code - [" + mwCurricularCourseScope.getCoursecode() + "], ");
				System.out.print("Degree Code - [" + mwCurricularCourseScope.getDegreecode() + "], ");
				System.out.print("Branch Code - [" + mwCurricularCourseScope.getBranchcode() + "], ");
				System.out.print("Curricular Year - [" + mwCurricularCourseScope.getCurricularyear() + "], ");
				System.out.println("Curricular Semester - [" + mwCurricularCourseScope.getCurricularsemester() + "], ");
				System.out.println("ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
				return;
			}

		} catch (Exception e) {
			System.out.println("[ERROR 09] Exception migrating MWCurricularCourseScope!");
			System.out.println("[ERROR 09] Execution Year: [" + mwCurricularCourseScope.getExecutionyear() + "]");
			System.out.println("[ERROR 09] Course Code: [" + mwCurricularCourseScope.getCoursecode() + "]");
			System.out.println("[ERROR 09] Degree Code: [" + mwCurricularCourseScope.getDegreecode() + "]");
			System.out.println("[ERROR 09] Branch Code: [" + mwCurricularCourseScope.getBranchcode() + "]");
			System.out.println("[ERROR 09] Curricular Year: [" + mwCurricularCourseScope.getCurricularyear() + "]");
			System.out.println("[ERROR 09] Curricular Semester: [" + mwCurricularCourseScope.getCurricularsemester() + "]");
			System.out.println("[ERROR 09] Course Type: [" + mwCurricularCourseScope.getCoursetype() + "]");
			throw new Exception(e);
		}
	}

	/**
	 * @param mwCurricularCourseScope
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null) {
			String degreeCurricularPlanName = "PAST-" + mwDegreeTranslation.getDegree().getSigla();
			ICurso degree = mwDegreeTranslation.getDegree();
			IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree(degreeCurricularPlanName, degree);
			if (degreeCurricularPlan == null) {
				IDegreeCurricularPlan degreeCurricularPlanToWrite = new DegreeCurricularPlan();
			
				persistentDegreeCurricularPlan.simpleLockWrite(degreeCurricularPlanToWrite);
			
				degreeCurricularPlanToWrite.setDegree(degree);
				degreeCurricularPlanToWrite.setInitialDate(new Date());
				degreeCurricularPlanToWrite.setEndDate(new Date());
				degreeCurricularPlanToWrite.setMarkType(MarkType.TYPE20_OBJ);
				degreeCurricularPlanToWrite.setName(degreeCurricularPlanName);
				degreeCurricularPlanToWrite.setState(DegreeCurricularPlanState.PAST_OBJ);
				degreeCurricularPlanToWrite.setDegreeDuration(new Integer(5));
				degreeCurricularPlanToWrite.setMinimalYearForOptionalCourses(new Integer(3));

				degreeCurricularPlanToWrite.setCurricularCourses(null);
				degreeCurricularPlanToWrite.setNeededCredits(null);
				degreeCurricularPlanToWrite.setNumerusClausus(null);

				CreateAndUpdateAllPastCurriculums.totalDegreeCurricularPlansCreated++;

				return degreeCurricularPlanToWrite;
			} else {
				return degreeCurricularPlan;
			}
		} else {
			return null;
		}
	}

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	public static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
		IPersistentBranch persistentBranch = fenixPersistentSuport.getIPersistentBranch();

		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);
		
		if (mwBranch != null) {
			branch = persistentBranch.readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwBranch.getDescription());

			if (branch == null) {
				branch = persistentBranch.readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
			}

			if (branch == null) {
				branch = new Branch();
			
				persistentBranch.simpleLockWrite(branch);

				if (mwBranch.getDescription().startsWith("CURSO DE ")) {
					branch.setName(new String(""));
					branch.setCode(new String(""));
				} else {
					branch.setName(mwBranch.getDescription());
					branch.setCode(new String("" + mwBranch.getDegreecode() + mwBranch.getBranchcode() + mwBranch.getOrientationcode()));
				}

				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setScopes(null);

				CreateAndUpdateAllPastCurriculums.totalBranchesCreated++;
			}
		}

		return branch;
	}

	/**
	 * @param courseCode
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static ICurricularCourse getCurricularCourse(String courseCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentCurricularCourse persistentCurricularCourse = fenixPersistentSuport.getIPersistentCurricularCourse();

		List curricularCourses = persistentCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(courseCode), degreeCurricularPlan);

		if (curricularCourses == null) {
			curricularCourses = new ArrayList();
		}

		if (curricularCourses.size() > 1) {
			// This should NEVER happen!
			System.out.println("[ERROR 05] Several Fenix CurricularCourses with code [" + StringUtils.trim(courseCode) + "] were found for Degree [" + degreeCurricularPlan.getDegree().getNome() + "]!");
			return null;
		} else if (curricularCourses.size() < 1) {
			// In fact this can only be curricularCourses.size() == 0 but better safe than sorry :)
			// This means no CurricularCourse was found with that code, for that DegreeCurricularPlan.
			ICurricularCourse curricularCourse = new CurricularCourse();
			
			persistentCurricularCourse.simpleLockWrite(curricularCourse);
			
			String curricularCourseName = CreateAndUpdateAllPastCurriculums.getCurricularCourseName(StringUtils.trim(courseCode));
			if (curricularCourseName == null) {
				// This should NEVER happen!
				System.out.println("[ERROR 06] Couldn't find name for CurricularCourse with code [" + StringUtils.trim(courseCode) + "]!");
				return null;
			}

			curricularCourse.setCode(StringUtils.trim(courseCode));
			curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
			curricularCourse.setName(curricularCourseName);
			// NOTE [DAVID]: Verficar se pode usar o valor do tipo de disciplina que vem no ficheiro do Almeida.
			curricularCourse.setType(CurricularCourseType.NORMAL_COURSE_OBJ);

			curricularCourse.setUniversity(null);
			curricularCourse.setCurricularCourseExecutionScope(null);
			curricularCourse.setMandatory(null);
			curricularCourse.setScopes(null);
			curricularCourse.setAssociatedExecutionCourses(null);
			curricularCourse.setBasic(null);
			curricularCourse.setCredits(null);
			curricularCourse.setDepartmentCourse(null);

			CreateAndUpdateAllPastCurriculums.totalCurricularCoursesCreated++;

			return curricularCourse;
		} else {
			// It has been found exactly ONE CurricularCourse already in the Fenix DB with that code, for that DegreeCurricularPlan.
			return (ICurricularCourse) curricularCourses.get(0);
		}
	}

	/**
	 * @param mwCurricularCourseScope
	 * @param curricularCourse
	 * @param branch
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static ICurricularCourseScope getCurricularCourseScope(MWCurricularCourseScope mwCurricularCourseScope, ICurricularCourse curricularCourse, IBranch branch, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentCurricularCourseScope persistentCurricularCourseScope = fenixPersistentSuport.getIPersistentCurricularCourseScope();
		IPersistentCurricularSemester persistentCurricularSemester = fenixPersistentSuport.getIPersistentCurricularSemester();
		IPersistentCurricularYear persistentCurricularYear = fenixPersistentSuport.getIPersistentCurricularYear();
		
		ICurricularYear curricularYear = persistentCurricularYear.readCurricularYearByYear(mwCurricularCourseScope.getCurricularyear());
		if (curricularYear == null) {
			// This should NEVER happen!
			System.out.println("[ERROR 07] Can't find in Fenix DB CurricularYear with year [" + mwCurricularCourseScope.getCurricularyear() + "]!");
			return null;
		}

		ICurricularSemester curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(mwCurricularCourseScope.getCurricularsemester(), curricularYear);
		if (curricularSemester == null) {
			// This should NEVER happen!
			System.out.println("[ERROR 08] Can't find in Fenix DB CurricularSemester with semester [" + mwCurricularCourseScope.getCurricularsemester() + "] and year [" + curricularYear.getYear() + "]!");
			return null;
		}

		ICurricularCourseScope curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);
		if (curricularCourseScope == null) {
			// This means no CurricularCourseScope was found with CurricularCourse, CurricularSemester and Branch.
			ICurricularCourseScope curricularCourseScopeToWrite = new CurricularCourseScope();
			
			persistentCurricularCourseScope.simpleLockWrite(curricularCourseScopeToWrite);
			
			curricularCourseScopeToWrite.setBranch(branch);
			curricularCourseScopeToWrite.setCredits(mwCurricularCourseScope.getCredits());
			curricularCourseScopeToWrite.setCurricularCourse(curricularCourse);
			curricularCourseScopeToWrite.setCurricularSemester(curricularSemester);
			curricularCourseScopeToWrite.setLabHours(mwCurricularCourseScope.getLabhours());
			curricularCourseScopeToWrite.setMaxIncrementNac(new Integer(2));
			curricularCourseScopeToWrite.setMinIncrementNac(new Integer(1));
			curricularCourseScopeToWrite.setTheoreticalHours(mwCurricularCourseScope.getTheoreticalhours());
			curricularCourseScopeToWrite.setPraticalHours(mwCurricularCourseScope.getPraticahours());
			curricularCourseScopeToWrite.setTheoPratHours(mwCurricularCourseScope.getTheoprathours());
			curricularCourseScopeToWrite.setBeginDate(Calendar.getInstance());
			curricularCourseScopeToWrite.setEndDate(Calendar.getInstance());
			curricularCourseScopeToWrite.setEctsCredits(null);

			curricularCourseScopeToWrite.setWeigth(null);

			CreateAndUpdateAllPastCurriculums.totalCurricularCourseScopesCreated++;

			return curricularCourseScopeToWrite;
		} else {
			// It has been in the Fenix DB the exact CurricularCourseScope we were looking for.
			return curricularCourseScope;
		}
	}

	/**
	 * @param curricularCourseCode
	 * @return
	 * @throws Exception
	 */
	private static String getCurricularCourseName(String curricularCourseCode) throws Exception
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWCurricularCourse persistentMWCurricualrCourse = mws.getIPersistentMWCurricularCourse();
		
		MWCurricularCourse mwCurricularCourse = persistentMWCurricualrCourse.readByCode(curricularCourseCode);
		
		if (mwCurricularCourse != null) {
			return mwCurricularCourse.getCoursename();
		} else {
			return null;
		}
	}

	/**
	 * @param persistentMiddlewareSupport
	 * @param fenixPersistentSuport
	 * @throws Exception
	 */
	private static void createAndUpdateUniversitys(IPersistentMiddlewareSupport persistentMiddlewareSupport, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentMWUniversity persistentMWUniversity = persistentMiddlewareSupport.getIPersistentMWUniversity();
		IPersistentUniversity persistentUniversity = fenixPersistentSuport.getIPersistentUniversity();
		
		fenixPersistentSuport.iniciarTransaccao();

		List mwUniversityList = persistentMWUniversity.readAll();
		
		if (mwUniversityList != null) {
			Iterator iterator = mwUniversityList.iterator();
			while (iterator.hasNext()) {
				MWUniversity mwUniversity = (MWUniversity) iterator.next();
				
				IUniversity university = persistentUniversity.readByNameAndCode(mwUniversity.getUniversityName(), mwUniversity.getUniversityCode());
		
				if (university == null) {
					university = new University();
					persistentUniversity.simpleLockWrite(university);
					university.setCode(mwUniversity.getUniversityCode());
					university.setName(mwUniversity.getUniversityName());
					CreateAndUpdateAllPastCurriculums.totalUniversitysCreated++;
				}
			}
		}

		fenixPersistentSuport.confirmarTransaccao();
	}

//	----------------------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------------------------
// ------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS ---------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------------------------

	private static IBranch solveBranchesProblemsForDegrees6And1(MWCurricularCourseScope mwCurricularCourseScope, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		if ((mwCurricularCourseScope.getDegreecode().intValue() == 6) ||
			(mwCurricularCourseScope.getDegreecode().intValue() == 1) ) {
			IPersistentBranch persistentBranch = fenixPersistentSuport.getIPersistentBranch();

			IBranch branch = new Branch();
			persistentBranch.simpleLockWrite(branch);
			branch.setName(new String("BRANCH THAT NO LONGER EXISTS"));
			branch.setCode(new String("" + mwCurricularCourseScope.getDegreecode() + mwCurricularCourseScope.getBranchcode() + 0));
			branch.setDegreeCurricularPlan(degreeCurricularPlan);
			branch.setScopes(null);
			CreateAndUpdateAllPastCurriculums.totalBranchesCreated++;
			return branch;
		} else {
			return null;
		}
	}
}