package ServidorAplicacao.Servico.manager.migration.withBroker;

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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IUniversity;
import Dominio.StudentCurricularPlan;
import Util.BranchType;
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
	protected HashMap branchesCreated;
	protected int numberOfElementsInSpan;
	protected int maximumNumberOfElementsToConsider;

	/**
	 * @param degreeCode
	 * @return IDegreeCurricularPlan
	 */
	protected IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode)
	{
		MWDegreeTranslation mwDegreeTranslation = super.persistentSuport.readMWDegreeTranslationByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null)
		{
			String degreeCurricularPlanName = "PAST-" + mwDegreeTranslation.getDegree().getSigla();
			ICurso degree = mwDegreeTranslation.getDegree();
			IDegreeCurricularPlan degreeCurricularPlan =
				super.persistentSuport.readDegreeCurricularPlanByNameAndDegree(degreeCurricularPlanName, degree);
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
	 */
	protected IStudentCurricularPlan getStudentCurricularPlan(
		IDegreeCurricularPlan degreeCurricularPlan,
		IStudent student,
		MWStudent mwStudent)
	{
		List result =
			super.persistentSuport.readStudentCurricularPlansByStudentAntState(student, StudentCurricularPlanState.PAST_OBJ);
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
						"[ERROR 007] No record of Branch with code: ["
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
				studentCurricularPlan.setAckOptLock(new Integer(1));

				super.persistentSuport.store(studentCurricularPlan);

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
	 */
	private IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan)
	{
		IBranch branch = null;
		MWBranch mwBranch = super.persistentSuport.readMWBranchByDegreeCodeAndBranchCode(degreeCode, branchCode);

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

			branch = super.persistentSuport.readBranchByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

		} else
		{
			branch = this.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(degreeCode, branchCode, degreeCurricularPlan);
		}

		return branch;
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return ICurricularCourse
	 */
	protected ICurricularCourse getCurricularCourse(MWEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan)
	{
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
				super.out.println("[WARNING 003] No record of University with code: [" + mwEnrolment.getUniversitycode() + "]!");
			} else
			{
				curricularCourse.setUniversity(university);
				super.persistentSuport.store(curricularCourse);
			}
		}
		return curricularCourse;
	}

	/**
	 * @param curricularCourseCode
	 * @return String
	 */
	private String getCurricularCourseName(String curricularCourseCode)
	{
		MWCurricularCourse mwCurricularCourse = super.persistentSuport.readMWCurricularCourseByCode(curricularCourseCode);

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
	 */
	private ICurricularCourse getCurricularCourse(String courseCode, IDegreeCurricularPlan degreeCurricularPlan, String key)
	{
		List curricularCourses =
			super.persistentSuport.readCurricularCoursesByCourseCodeAndDegreeCurricularPlan(
				StringUtils.trim(courseCode),
				degreeCurricularPlan);

		if (curricularCourses == null)
		{
			curricularCourses = new ArrayList();
		}

		if (curricularCourses.size() > 1)
		{
			System.out.println(
				"[ERROR 008] Several Fenix CurricularCourses with code ["
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
					"[ERROR 009] Couldn't find name for CurricularCourse with code [" + StringUtils.trim(courseCode) + "]!");
				return null;
			}

			ICurricularCourse curricularCourse = new CurricularCourse();

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
			curricularCourse.setAckOptLock(new Integer(1));

			super.persistentSuport.store(curricularCourse);

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
	 */
	private IUniversity getUniversity(String universityCode)
	{
		MWUniversity mwUniversity = super.persistentSuport.readMWUniversityByCode(universityCode);

		if (mwUniversity == null)
		{
			return null;
		}

		IUniversity university =
			super.persistentSuport.readUniversityByNameAndCode(mwUniversity.getUniversityName(), mwUniversity.getUniversityCode());

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
				super.out.println("[ERROR 010] No record of EnrolmentEvaluationType with code: [" + mwEnrolment.getSeason() + "]!");
				break;
		}

		return enrolmentEvaluationType;
	}

	//	----------------------------------------------------------------------------------------------------------------------
	//	----------------------------------------------------------------------------------------------------------------------
	//	------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS ---------------------------------------------------
	//	----------------------------------------------------------------------------------------------------------------------
	//	----------------------------------------------------------------------------------------------------------------------

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @return IBranch
	 */
	public IBranch solveBranchesProblemsForDegrees1And4And6And51And53And54And64(
		Integer degreeCode,
		Integer branchCode,
		IDegreeCurricularPlan degreeCurricularPlan)
	{
		if ((degreeCode.intValue() == 6)
			|| (degreeCode.intValue() == 1)
			|| (degreeCode.intValue() == 4)
			|| (degreeCode.intValue() == 51)
			|| (degreeCode.intValue() == 53)
			|| (degreeCode.intValue() == 54)
			|| (degreeCode.intValue() == 64))
		{

			String realBranchCode = new String(degreeCode.toString() + branchCode.toString() + 0);
			IBranch branch = super.persistentSuport.readBranchByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

			if (branch == null)
			{
				branch = (IBranch) this.branchesCreated.get(realBranchCode);
			}

			if (branch == null)
			{
				branch = new Branch();
				branch.setName(new String("RAMO QUE JÁ NÃO EXISTE"));
				branch.setCode(realBranchCode);
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setScopes(null);
				branch.setBranchType(BranchType.COMMON_BRANCH);
				branch.setAckOptLock(new Integer(1));
				super.persistentSuport.store(branch);
				this.branchesCreated.put(realBranchCode, branch);
			}

			return branch;
		} else
		{
			return null;
		}
	}

}