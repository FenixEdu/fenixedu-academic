package ServidorAplicacao.Servico.manager.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;
import middleware.studentMigration.enrollments.CreateAndUpdateAllPastCurriculums;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IUniversity;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentUniversity;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import Util.CurricularCourseType;
import Util.EnrolmentEvaluationType;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos in Feb 7, 2004
 */

public abstract class CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlans
	extends CreateUpdateDeleteEnrollments
	implements IService
{
	protected HashMap studentCurricularPlansCreated;
	protected HashMap curricularCoursesCreated;
	protected int numberOfElementsInSpan;
	protected int maximumNumberOfElementsToConsider;

	/**
	 * @param degreeCode
	 * @return IDegreeCurricularPlan
	 * @throws Throwable
	 */
	protected IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode) throws Throwable
	{
		IPersistentMWDegreeTranslation mwDegreeTranslationDAO = super.persistentMiddlewareSuport.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = super.persistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = mwDegreeTranslationDAO.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null)
		{
			String degreeCurricularPlanName = "PAST-" + mwDegreeTranslation.getDegree().getSigla();
			ICurso degree = mwDegreeTranslation.getDegree();
			IDegreeCurricularPlan degreeCurricularPlan =
				degreeCurricularPlanDAO.readByNameAndDegree(degreeCurricularPlanName, degree);
			return degreeCurricularPlan;
		} else
		{
			return null;
		}
	}

	/**
	 * @param degreeCurricularPlan
	 * @param student
	 * @param mwStudent
	 * @return IStudentCurricularPlan
	 * @throws Throwable
	 */
	protected IStudentCurricularPlan getStudentCurricularPlan(
		IDegreeCurricularPlan degreeCurricularPlan,
		IStudent student,
		MWStudent mwStudent)
		throws Throwable
	{
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = super.persistentSuport.getIStudentCurricularPlanPersistente();

		List result = studentCurricularPlanDAO.readAllByStudentAntState(student, StudentCurricularPlanState.PAST_OBJ);
		if ((result == null) || (result.isEmpty()))
		{
			IStudentCurricularPlan studentCurricularPlanToObtainKey = new StudentCurricularPlan();
			studentCurricularPlanToObtainKey.setStudent(student);
			studentCurricularPlanToObtainKey.setDegreeCurricularPlan(degreeCurricularPlan);
			studentCurricularPlanToObtainKey.setCurrentState(StudentCurricularPlanState.PAST_OBJ);
			String key = super.getStudentCurricularPlanKey(studentCurricularPlanToObtainKey);

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) this.studentCurricularPlansCreated.get(key);

			if (studentCurricularPlan == null)
			{
				IBranch branch = this.getBranch(mwStudent.getDegreecode(), mwStudent.getBranchcode(), degreeCurricularPlan);
				if (branch == null)
				{
					super.out.println(
						"[ERROR 201] No record of Branch with code: ["
							+ mwStudent.getBranchcode()
							+ "] for Degree with code: ["
							+ mwStudent.getDegreecode()
							+ "]!");
					return null;
				}

				Date startDate = null;
				if ((mwStudent.getEnrolments() == null) || (mwStudent.getEnrolments().isEmpty()))
				{
					startDate = new Date();
				} else
				{
					ComparatorChain comparatorChain = new ComparatorChain();
					comparatorChain.addComparator(new BeanComparator("enrolmentyear"));
					Collections.sort(mwStudent.getEnrolments(), comparatorChain);
					MWEnrolment mwEnrolment = (MWEnrolment) mwStudent.getEnrolments().get(0);
					startDate = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment).getBeginDate();
				}

				studentCurricularPlan = new StudentCurricularPlan();

				studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);

				studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
				studentCurricularPlan.setCurrentState(StudentCurricularPlanState.PAST_OBJ);
				studentCurricularPlan.setStartDate(startDate);
				studentCurricularPlan.setStudent(student);
				studentCurricularPlan.setBranch(branch);

				studentCurricularPlan.setClassification(null);
				studentCurricularPlan.setCompletedCourses(null);
				studentCurricularPlan.setEmployee(null);
				studentCurricularPlan.setEnrolledCourses(null);
				studentCurricularPlan.setEnrolments(null);
				studentCurricularPlan.setGivenCredits(null);
				studentCurricularPlan.setObservations(null);
				studentCurricularPlan.setSpecialization(null);
				studentCurricularPlan.setWhen(null);

				this.studentCurricularPlansCreated.put(key, studentCurricularPlan);
			}

			return studentCurricularPlan;
		} else if (!result.isEmpty())
		{
			return (IStudentCurricularPlan) result.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @return IBranch
	 * @throws Throwable
	 */
	private IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan) throws Throwable
	{
		IBranch branch = null;

		IPersistentMWBranch mwBranchDAO = super.persistentMiddlewareSuport.getIPersistentMWBranch();
		IPersistentBranch branchDAO = super.persistentSuport.getIPersistentBranch();

		MWBranch mwBranch = mwBranchDAO.readByDegreeCodeAndBranchCode(degreeCode, branchCode);

		if (mwBranch != null)
		{
			String realBranchCode = null;

			if (mwBranch.getDescription().startsWith("CURSO DE "))
			{
				realBranchCode = new String("");
			} else
			{
				realBranchCode =
					new String(
						mwBranch.getDegreecode().toString()
							+ mwBranch.getBranchcode().toString()
							+ mwBranch.getOrientationcode().toString());
			}

			branch = branchDAO.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

		} else
		{
			branch =
				CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(
					degreeCode,
					branchCode,
					degreeCurricularPlan,
					branchDAO);
		}

		return branch;
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	protected ICurricularCourse getCurricularCourse(MWEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan)
		throws Throwable
	{
		IPersistentCurricularCourse curricularCourseDAO = super.persistentSuport.getIPersistentCurricularCourse();

		ICurricularCourse curricularCourseToObtainKey = new CurricularCourse();
		curricularCourseToObtainKey.setDegreeCurricularPlan(degreeCurricularPlan);
		curricularCourseToObtainKey.setCode(mwEnrolment.getCoursecode());
		curricularCourseToObtainKey.setName(this.getCurricularCourseName(mwEnrolment.getCoursecode()));
		String key = super.getCurricularCourseKey(curricularCourseToObtainKey);

		ICurricularCourse curricularCourse = (ICurricularCourse) this.curricularCoursesCreated.get(key);
		if (curricularCourse == null)
		{
			curricularCourse = this.getCurricularCourse(mwEnrolment.getCoursecode(), degreeCurricularPlan, key);
		}

		if ((curricularCourse != null) && (curricularCourse.getUniversity() == null))
		{
			IUniversity university = this.getUniversity(mwEnrolment.getUniversitycode());
			if (university == null)
			{
				super.out.println("[WARNING 201] No record of University with code: [" + mwEnrolment.getUniversitycode() + "]!");
			} else
			{
				curricularCourseDAO.simpleLockWrite(curricularCourse);
				curricularCourse.setUniversity(university);
			}
		}
		return curricularCourse;
	}

	/**
	 * @param curricularCourseCode
	 * @return String
	 * @throws Throwable
	 */
	private String getCurricularCourseName(String curricularCourseCode) throws Throwable
	{
		IPersistentMWCurricularCourse persistentMWCurricualrCourse =
			super.persistentMiddlewareSuport.getIPersistentMWCurricularCourse();

		MWCurricularCourse mwCurricularCourse = persistentMWCurricualrCourse.readByCode(curricularCourseCode);

		if (mwCurricularCourse != null)
		{
			return mwCurricularCourse.getCoursename();
		} else
		{
			return null;
		}
	}

	/**
	 * @param courseCode
	 * @param degreeCurricularPlan
	 * @param key
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	private ICurricularCourse getCurricularCourse(String courseCode, IDegreeCurricularPlan degreeCurricularPlan, String key)
		throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = super.persistentSuport.getIPersistentCurricularCourse();

		List curricularCourses =
			persistentCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(courseCode), degreeCurricularPlan);

		if (curricularCourses == null)
		{
			curricularCourses = new ArrayList();
		}

		if (curricularCourses.size() > 1)
		{
			System.out.println(
				"[ERROR 203] Several Fenix CurricularCourses with code ["
					+ StringUtils.trim(courseCode)
					+ "] were found for Degree ["
					+ degreeCurricularPlan.getDegree().getNome()
					+ "]!");
			return null;
		} else if (curricularCourses.size() < 1)
		{
			// In fact this can only be curricularCourses.size() == 0 but better safe than sorry :)
			// This means no CurricularCourse was found with that code, for that DegreeCurricularPlan.

			String curricularCourseName = this.getCurricularCourseName(StringUtils.trim(courseCode));
			if (curricularCourseName == null)
			{
				System.out.println(
					"[ERROR 204] Couldn't find name for CurricularCourse with code [" + StringUtils.trim(courseCode) + "]!");
				return null;
			}

			ICurricularCourse curricularCourse = new CurricularCourse();

			persistentCurricularCourse.simpleLockWrite(curricularCourse);

			curricularCourse.setCode(StringUtils.trim(courseCode));
			curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
			curricularCourse.setName(curricularCourseName);
			curricularCourse.setType(CurricularCourseType.NORMAL_COURSE_OBJ);

			curricularCourse.setUniversity(null);
			curricularCourse.setCurricularCourseExecutionScope(null);
			curricularCourse.setMandatory(null);
			curricularCourse.setScopes(null);
			curricularCourse.setAssociatedExecutionCourses(null);
			curricularCourse.setBasic(null);
			curricularCourse.setCredits(null);
			curricularCourse.setDepartmentCourse(null);

			this.curricularCoursesCreated.put(key, curricularCourse);

			return curricularCourse;
		} else
		{
			// It has been found exactly ONE CurricularCourse already in the Fenix DB with that code, for that DegreeCurricularPlan.
			return (ICurricularCourse) curricularCourses.get(0);
		}
	}

	/**
	 * @param universityCode
	 * @return IUniversity
	 * @throws Throwable
	 */
	private IUniversity getUniversity(String universityCode) throws Throwable
	{
		IPersistentMWUniversity mwUniversityDAO = super.persistentMiddlewareSuport.getIPersistentMWUniversity();
		IPersistentUniversity universityDAO = super.persistentSuport.getIPersistentUniversity();

		MWUniversity mwUniversity = mwUniversityDAO.readByCode(universityCode);

		if (mwUniversity == null)
		{
			return null;
		}

		IUniversity university = universityDAO.readByNameAndCode(mwUniversity.getUniversityName(), mwUniversity.getUniversityCode());

		return university;
	}

	/**
	 * @param mwEnrolment
	 * @return EnrolmentEvaluationType
	 */
	protected EnrolmentEvaluationType getEvaluationType(MWEnrolment mwEnrolment)
	{
		EnrolmentEvaluationType enrolmentEvaluationType = null;

		int season = mwEnrolment.getSeason().intValue();

		switch (season)
		{
			case 0 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NO_SEASON_OBJ;
				break;
			case 1 :
				enrolmentEvaluationType = EnrolmentEvaluationType.FIRST_SEASON_OBJ;
				break;
			case 2 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SECOND_SEASON_OBJ;
				break;
			case 3 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON_OBJ;
				break;
			case 4 :
				enrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
				break;
			case 5 :
				enrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
				break;
			default :
				super.out.println("[ERROR 205] No record of EnrolmentEvaluationType with code: [" + mwEnrolment.getSeason() + "]!");
				break;
		}

		return enrolmentEvaluationType;
	}

}