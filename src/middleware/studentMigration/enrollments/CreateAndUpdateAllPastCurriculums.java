package middleware.studentMigration.enrollments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWCurricularCourseScope;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
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
import Dominio.IExecutionPeriod;
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
	private static int mwCurricularCourseScopesRead = 0;
	private static int universitysCreated = 0;
	private static int degreeCurricularPlansCreated = 0;
	protected static int curricularCoursesCreated = 0;
	protected static int curricularCourseScopesCreated = 0;

	private static HashMap branchesCreated = new HashMap();

	public static void main(String args[])
	{
		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWCurricularCourseScope persistentMWCurricularCourseScope = mws.getIPersistentMWCurricularCourseScope();
	
			fenixPersistentSuport.iniciarTransaccao();
	
			Integer numberOfMWCurricularCourseScopes = persistentMWCurricularCourseScope.countAll();
	
			fenixPersistentSuport.confirmarTransaccao();
	
			int numberOfElementsInSpan = 2000;
			int numberOfSpans = numberOfMWCurricularCourseScopes.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfMWCurricularCourseScopes.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
	
			for (int span = 0; span < numberOfSpans; span++) {
				fenixPersistentSuport.iniciarTransaccao();
				fenixPersistentSuport.clearCache();	
	
				System.out.println("[INFO] Reading MWCurricularCourseScopes...");
				List result = persistentMWCurricularCourseScope.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
				CreateAndUpdateAllPastCurriculums.mwCurricularCourseScopesRead = CreateAndUpdateAllPastCurriculums.mwCurricularCourseScopesRead + result.size();
	
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
		System.out.println("[INFO] Total DegreeCurricularPlans created: [" + CreateAndUpdateAllPastCurriculums.degreeCurricularPlansCreated + "].");
		System.out.println("[INFO] Total CurricularCourses created: [" + CreateAndUpdateAllPastCurriculums.curricularCoursesCreated + "].");
		System.out.println("[INFO] Total CurricularCourseScopes created: [" + CreateAndUpdateAllPastCurriculums.curricularCourseScopesCreated + "].");
		System.out.println("[INFO] Total Branches created: [" + CreateAndUpdateAllPastCurriculums.branchesCreated.size() + "].");
		System.out.println("[INFO] Total Universitys created: [" + CreateAndUpdateAllPastCurriculums.universitysCreated + "].");
		System.out.println("[INFO] Total MWCurricularCourseScopes read: [" + CreateAndUpdateAllPastCurriculums.mwCurricularCourseScopesRead + "].");

	}

	/**
	 * @param mwCurricularCourseScope
	 * @param fenixPersistentSuport
	 * @throws Exception
	 */
	private static void writeAndUpdate(MWCurricularCourseScope mwCurricularCourseScope, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		try {
			IDegreeCurricularPlan degreeCurricularPlan = CreateAndUpdateAllPastCurriculums.getDegreeCurricularPlan(mwCurricularCourseScope, fenixPersistentSuport);
			if (degreeCurricularPlan == null) {
				System.out.println("[ERROR 101] No record of Degree with code: [" + mwCurricularCourseScope.getDegreecode() + "]! ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
				return;
			}

			IBranch branch = CreateAndUpdateAllPastCurriculums.getBranch(mwCurricularCourseScope.getDegreecode(), mwCurricularCourseScope.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
			if (branch == null) {
				System.out.println("[ERROR 102] No record of Branch with code: [" + mwCurricularCourseScope.getBranchcode() + "] for Degree with code: [" + mwCurricularCourseScope.getDegreecode() + "]! ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
				return;
			}

			ICurricularCourse curricularCourse = CreateAndUpdateAllPastCurriculums.getCurricularCourse(mwCurricularCourseScope.getCoursecode(), degreeCurricularPlan, fenixPersistentSuport);
			if (curricularCourse == null) {
				System.out.println("[ERROR 103] No record of CurricularCourse with code: [" + mwCurricularCourseScope.getCoursecode() + "]! ExecutionYear: [" + mwCurricularCourseScope.getExecutionyear() + "]");
				return;
			}

			ICurricularCourseScope curricularCourseScope = CreateAndUpdateAllPastCurriculums.getCurricularCourseScope(mwCurricularCourseScope, curricularCourse, branch, fenixPersistentSuport);
			if (curricularCourseScope == null) {
				System.out.print("[ERROR 104] No record of CurricularCourseScope with data: ");
				System.out.print("Course Code - [" + mwCurricularCourseScope.getCoursecode() + "], ");
				System.out.print("Degree Code - [" + mwCurricularCourseScope.getDegreecode() + "], ");
				System.out.print("Branch Code - [" + mwCurricularCourseScope.getBranchcode() + "], ");
				System.out.print("Curricular Year - [" + mwCurricularCourseScope.getCurricularyear() + "], ");
				System.out.print("Curricular Semester - [" + mwCurricularCourseScope.getCurricularsemester() + "], ");
				System.out.println("Execution Year: [" + mwCurricularCourseScope.getExecutionyear() + "]!");
				return;
			}

		} catch (Exception e) {
			System.out.println("[ERROR 109] Exception migrating MWCurricularCourseScope!");
			System.out.println("[ERROR 109] Execution Year: [" + mwCurricularCourseScope.getExecutionyear() + "]");
			System.out.println("[ERROR 109] Course Code: [" + mwCurricularCourseScope.getCoursecode() + "]");
			System.out.println("[ERROR 109] Degree Code: [" + mwCurricularCourseScope.getDegreecode() + "]");
			System.out.println("[ERROR 109] Branch Code: [" + mwCurricularCourseScope.getBranchcode() + "]");
			System.out.println("[ERROR 109] Curricular Year: [" + mwCurricularCourseScope.getCurricularyear() + "]");
			System.out.println("[ERROR 109] Curricular Semester: [" + mwCurricularCourseScope.getCurricularsemester() + "]");
			System.out.println("[ERROR 109] Course Type: [" + mwCurricularCourseScope.getCoursetype() + "]");
			throw new Exception(e);
		}
	}

	/**
	 * @param mwCurricularCourseScope
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWCurricularCourseScope mwCurricularCourseScope, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();
		
		IDegreeCurricularPlan degreeCurricularPlan = null;

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(mwCurricularCourseScope.getDegreecode());

		if (mwDegreeTranslation != null) {
			String degreeCurricularPlanName = "PAST-" + mwDegreeTranslation.getDegree().getSigla();
			ICurso degree = mwDegreeTranslation.getDegree();

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree(degreeCurricularPlanName, degree);

			if (degreeCurricularPlan == null) {
				
				degreeCurricularPlan = new DegreeCurricularPlan();
		
				persistentDegreeCurricularPlan.simpleLockWrite(degreeCurricularPlan);
		
				degreeCurricularPlan.setDegree(degree);
				degreeCurricularPlan.setInitialDate(new Date());
				degreeCurricularPlan.setEndDate(new Date());
				degreeCurricularPlan.setMarkType(MarkType.TYPE20_OBJ);
				degreeCurricularPlan.setName(degreeCurricularPlanName);
				degreeCurricularPlan.setState(DegreeCurricularPlanState.PAST_OBJ);
				degreeCurricularPlan.setDegreeDuration(new Integer(5));
				degreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));

				degreeCurricularPlan.setCurricularCourses(null);
				degreeCurricularPlan.setNeededCredits(null);
				degreeCurricularPlan.setNumerusClausus(null);

				CreateAndUpdateAllPastCurriculums.degreeCurricularPlansCreated++;
			}
		}
		
		return degreeCurricularPlan;
	}

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
		IPersistentBranch persistentBranch = fenixPersistentSuport.getIPersistentBranch();

		IBranch branch = null;
		
		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);
		
		if (mwBranch != null) {

			String realBranchCode = null;
			String realBranchName = null;

			if (mwBranch.getDescription().startsWith("CURSO DE ")) {
				realBranchName = new String("");
				realBranchCode = new String("");
			} else {
				realBranchName = mwBranch.getDescription();
				realBranchCode = new String(mwBranch.getDegreecode().toString() + mwBranch.getBranchcode().toString() + mwBranch.getOrientationcode().toString());
			}

			branch = persistentBranch.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

			String key = new String(mwBranch.getDegreecode().toString() + mwBranch.getBranchcode().toString() + mwBranch.getOrientationcode().toString());
			if (branch == null) {
				branch = (IBranch) CreateAndUpdateAllPastCurriculums.branchesCreated.get(key);
			}
				
			if (branch == null) {
				branch = new Branch();
		
				persistentBranch.simpleLockWrite(branch);

				branch.setName(realBranchName);
				branch.setCode(realBranchCode);
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setScopes(null);

				CreateAndUpdateAllPastCurriculums.branchesCreated.put(key, branch);
			}
		} else {
			branch = CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(degreeCode, branchCode, degreeCurricularPlan, persistentBranch);
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
	protected static ICurricularCourse getCurricularCourse(String courseCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentCurricularCourse persistentCurricularCourse = fenixPersistentSuport.getIPersistentCurricularCourse();

		List curricularCourses = persistentCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(courseCode), degreeCurricularPlan);

		if (curricularCourses == null) {
			curricularCourses = new ArrayList();
		}

		if (curricularCourses.size() > 1) {
			System.out.println("[ERROR 105] Several Fenix CurricularCourses with code [" + StringUtils.trim(courseCode) + "] were found for Degree [" + degreeCurricularPlan.getDegree().getNome() + "]!");
			return null;
		} else if (curricularCourses.size() < 1) {
			// In fact this can only be curricularCourses.size() == 0 but better safe than sorry :)
			// This means no CurricularCourse was found with that code, for that DegreeCurricularPlan.

			String curricularCourseName = CreateAndUpdateAllPastCurriculums.getCurricularCourseName(StringUtils.trim(courseCode));
			if (curricularCourseName == null) {
				System.out.println("[ERROR 106] Couldn't find name for CurricularCourse with code [" + StringUtils.trim(courseCode) + "]!");
				return null;
			}

			ICurricularCourse curricularCourse = new CurricularCourse();
		
			persistentCurricularCourse.simpleLockWrite(curricularCourse);
		
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

			CreateAndUpdateAllPastCurriculums.curricularCoursesCreated++;

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
	protected static ICurricularCourseScope getCurricularCourseScope(MWCurricularCourseScope mwCurricularCourseScope, ICurricularCourse curricularCourse, IBranch branch, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		IPersistentCurricularCourseScope persistentCurricularCourseScope = fenixPersistentSuport.getIPersistentCurricularCourseScope();
		IPersistentCurricularSemester persistentCurricularSemester = fenixPersistentSuport.getIPersistentCurricularSemester();
		IPersistentCurricularYear persistentCurricularYear = fenixPersistentSuport.getIPersistentCurricularYear();
		
		ICurricularYear curricularYear = persistentCurricularYear.readCurricularYearByYear(mwCurricularCourseScope.getCurricularyear());
		if (curricularYear == null) {
			System.out.println("[ERROR 107] Can't find in Fenix DB CurricularYear with year [" + mwCurricularCourseScope.getCurricularyear() + "]!");
			return null;
		}

		ICurricularSemester curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(mwCurricularCourseScope.getCurricularsemester(), curricularYear);
		if (curricularSemester == null) {
			System.out.println("[ERROR 108] Can't find in Fenix DB CurricularSemester with semester [" + mwCurricularCourseScope.getCurricularsemester() + "] and year [" + curricularYear.getYear() + "]!");
			return null;
		}

		ICurricularCourseScope curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);
		if (curricularCourseScope == null) {

			// This means no CurricularCourseScope was found with CurricularCourse, CurricularSemester and Branch.
			curricularCourseScope = new CurricularCourseScope();
		
			persistentCurricularCourseScope.simpleLockWrite(curricularCourseScope);
		
			curricularCourseScope.setBranch(branch);
			curricularCourseScope.setCredits(mwCurricularCourseScope.getCredits());
			curricularCourseScope.setCurricularCourse(curricularCourse);
			curricularCourseScope.setCurricularSemester(curricularSemester);
			curricularCourseScope.setLabHours(mwCurricularCourseScope.getLabhours());
			curricularCourseScope.setMaxIncrementNac(new Integer(2));
			curricularCourseScope.setMinIncrementNac(new Integer(1));
			curricularCourseScope.setTheoreticalHours(mwCurricularCourseScope.getTheoreticalhours());
			curricularCourseScope.setPraticalHours(mwCurricularCourseScope.getPraticahours());
			curricularCourseScope.setTheoPratHours(mwCurricularCourseScope.getTheoprathours());

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(CreateAndUpdateAllPastCurriculums.getExecutionPeriod(mwCurricularCourseScope, fenixPersistentSuport).getBeginDate().getTime());
			curricularCourseScope.setBeginDate(calendar);
			calendar.setTimeInMillis(CreateAndUpdateAllPastCurriculums.getExecutionPeriod(mwCurricularCourseScope, fenixPersistentSuport).getEndDate().getTime());
			curricularCourseScope.setEndDate(calendar);

			curricularCourseScope.setEctsCredits(null);
			curricularCourseScope.setWeigth(null);

			CreateAndUpdateAllPastCurriculums.curricularCourseScopesCreated++;
		}
		return curricularCourseScope;
	}

	/**
	 * @param curricularCourseCode
	 * @return
	 * @throws Exception
	 */
	protected static String getCurricularCourseName(String curricularCourseCode) throws Exception
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
					CreateAndUpdateAllPastCurriculums.universitysCreated++;
				}
			}
		}

		fenixPersistentSuport.confirmarTransaccao();
	}

//	----------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------
//	------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS ---------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @param persistentBranch
	 * @return
	 * @throws Exception
	 */
	protected static IBranch solveBranchesProblemsForDegrees1And4And6And51And53And54And64(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, IPersistentBranch persistentBranch) throws Exception
	{
		if ((degreeCode.intValue() == 6) ||
			(degreeCode.intValue() == 1) ||
			(degreeCode.intValue() == 4) ||
			(degreeCode.intValue() == 51) ||
			(degreeCode.intValue() == 53) ||
			(degreeCode.intValue() == 54) ||
			(degreeCode.intValue() == 64)) {

			String realBranchCode = new String(degreeCode.toString() + branchCode.toString() + 0);
			IBranch branch = persistentBranch.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

			if (branch == null) {
				branch = (IBranch) CreateAndUpdateAllPastCurriculums.branchesCreated.get(realBranchCode);
			}
				
			if (branch == null) {
				branch = new Branch();
				persistentBranch.simpleLockWrite(branch);
				branch.setName(new String("BRANCH THAT NO LONGER EXISTS"));
				branch.setCode(realBranchCode);
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setScopes(null);
				CreateAndUpdateAllPastCurriculums.branchesCreated.put(realBranchCode, branch);
			}

			return branch;
		} else {
			return null;
		}
	}

	/**
	 * @param mwCurricularCourseScope
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IExecutionPeriod getExecutionPeriod(MWCurricularCourseScope mwCurricularCourseScope, ISuportePersistente fenixPersistentSuport) throws Exception
	{
		MWEnrolment mwEnrolment = new MWEnrolment();
		mwEnrolment.setEnrolmentyear(mwCurricularCourseScope.getExecutionyear());
		mwEnrolment.setCurricularcoursesemester(mwCurricularCourseScope.getCurricularsemester());
		IExecutionPeriod executionPeriod = CreateAndUpdateAllStudentsPastEnrolments.getExecutionPeriodForThisMWEnrolment(mwEnrolment, fenixPersistentSuport);
		return executionPeriod;
	}
}