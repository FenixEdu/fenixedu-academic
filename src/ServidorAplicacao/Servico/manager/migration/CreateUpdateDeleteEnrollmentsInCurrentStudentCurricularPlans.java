package ServidorAplicacao.Servico.manager.migration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.studentMigration.enrollments.ReportEnrolment;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public abstract class CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans
	extends CreateUpdateDeleteEnrollments
	implements IService
{
	protected IExecutionPeriod executionPeriod;
	protected int numberOfElementsInSpan;
	protected int maximumNumberOfElementsToConsider;

	/**
	 * @param degreeCode
	 * @param studentCurricularPlan
	 * @throws Throwable
	 */
	protected IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, IStudentCurricularPlan studentCurricularPlan)
		throws Throwable
	{
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation =
			super.persistentMiddlewareSuport.getIPersistentMWDegreeTranslation();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		ICursoExecucao executionDegree =
			super.persistentSuport.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(
				mwDegreeTranslation.getDegree().getNome(),
				this.executionPeriod.getExecutionYear(),
				TipoCurso.LICENCIATURA_OBJ);

		if (executionDegree == null)
		{
			super.out.println(
				"[ERROR 101] the degree has no execution in [" + this.executionPeriod.getExecutionYear().getYear() + "]!");
			return null;
		} else
		{
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(executionDegree.getCurricularPlan()))
			{
				super.out.println(
					"[INFO] the student [" + studentCurricularPlan.getStudent().getNumber() + "] has changed his degree curricular plan!");
				return studentCurricularPlan.getDegreeCurricularPlan();
			} else
			{
				return executionDegree.getCurricularPlan();
			}
		}
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @param solveSomeProblems
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	protected ICurricularCourse getCurricularCourse(
		MWEnrolment mwEnrolment,
		IDegreeCurricularPlan degreeCurricularPlan,
		boolean solveSomeProblems)
		throws Throwable
	{
		String courseCode = null;
		if (solveSomeProblems)
		{
			courseCode = this.getRealCurricularCourseCodeForCodesAZx(mwEnrolment);
		} else
		{
			courseCode = mwEnrolment.getCoursecode();
		}

		List curricularCourses =
			super.persistentSuport.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(
				StringUtils.trim(courseCode),
				degreeCurricularPlan);

		ICurricularCourse curricularCourse = null;

		// Ideally we find only one CurricularCourse but we never know, we may find more or even less.
		if (curricularCourses.size() != 1)
		{
			if (curricularCourses.size() > 1)
			{
				super.out.println(
					"[ERROR 102] Several Fenix CurricularCourses with code ["
						+ courseCode
						+ "] were found for Degree ["
						+ mwEnrolment.getDegreecode()
						+ "]!");
				return null;
			} else // size == 0
				{
				// We did not find any CurricularCourse with that code in that DegreeCurricularPlan.
				// Now we try to read by the CurricularCourse code only.
				IPersistentCurricularCourse curricularCourseDAO = super.persistentSuport.getIPersistentCurricularCourse();
				curricularCourses =
					curricularCourseDAO.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
						StringUtils.trim(courseCode),
						degreeCurricularPlan.getDegree().getTipoCurso(),
						degreeCurricularPlan.getState());

				if (curricularCourses.size() == 1)
				{
					curricularCourse = (ICurricularCourse) curricularCourses.get(0);
				} else if (curricularCourses.size() > 1)
				{
					if (this.hasDiferentDegrees(curricularCourses))
					{
						curricularCourse = this.getCurricularCourseFromAnotherDegree(mwEnrolment, degreeCurricularPlan);
						if (curricularCourse == null)
						{
							// NOTE [DAVID]: This is for information only.
							ReportEnrolment.addReplicatedCurricularCourses(courseCode, curricularCourses);
							return null;
						}
					} else
					{
						curricularCourse = (ICurricularCourse) curricularCourses.get(0);
					}
				} else
				{
					ReportEnrolment.addCurricularCourseNotFound(
						courseCode,
						mwEnrolment.getDegreecode().toString(),
						mwEnrolment.getNumber().toString());
					return null;
				}
			}
		} else // curricularCourses.size() == 1
			{
			curricularCourse = (ICurricularCourse) curricularCourses.get(0);
		}
		return curricularCourse;
	}

	/**
	 * @param curricularCourses
	 * @return true/false
	 */
	private boolean hasDiferentDegrees(List curricularCourses)
	{
		int numberOfDegrees = CollectionUtils.getCardinalityMap(curricularCourses).size();
		return (numberOfDegrees > 1);
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	private ICurricularCourse getCurricularCourseFromAnotherDegree(
			final MWEnrolment mwEnrolment,
			IDegreeCurricularPlan degreeCurricularPlan)
	throws Throwable
	{
		ICurricularCourse curricularCourse = null;

		IPersistentMWCurricularCourseOutsideStudentDegree persistentMWCurricularCourseOutsideStudentDegree =
		super.persistentMiddlewareSuport.getIPersistentMWCurricularCourseOutsideStudentDegree();

		// First of all we look in the MWCurricularCourseOutsideStudentDegree table to see if there is
		// already a
		// correspondence between this CurricularCourse and this Degree.
		MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegree =
		persistentMWCurricularCourseOutsideStudentDegree.readByCourseCodeAndDegreeCode(
				mwEnrolment.getCoursecode(),
				mwEnrolment.getDegreecode());
		if (mwCurricularCourseOutsideStudentDegree != null)
		{
			curricularCourse = mwCurricularCourseOutsideStudentDegree.getCurricularCourse();

			// If there is no correspondence yet, let us look if this CurricularCourse has only one
			// ExecuitonCourse in the given period.
		} else if (this.curricularCourseHasOnlyOneExecutionInGivenPeriod(this.executionPeriod, mwEnrolment, degreeCurricularPlan))
		{

			// If there is only one ExecutionCourse for all the existing CurricularCourses then we can
			// choose
			// any CurricularCourse but this choice is registred in table
			// MWCurricularCourseOutsideStudentDegree
			// to keep coherence of choice and to make the next similar choice quicker.
			IPersistentCurricularCourse persistentCurricularCourse = super.persistentSuport.getIPersistentCurricularCourse();
			List curricularCourses =
			persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
					StringUtils.trim(mwEnrolment.getCoursecode()),
					degreeCurricularPlan.getDegree().getTipoCurso(),
					degreeCurricularPlan.getState());
			curricularCourse = (ICurricularCourse) curricularCourses.get(0);

			MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite =
			new MWCurricularCourseOutsideStudentDegree();
			persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(mwCurricularCourseOutsideStudentDegreeToWrite);
			mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
			mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
			mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

		} else
		{

			IFrequentaPersistente attendDAO = super.persistentSuport.getIFrequentaPersistente();
			List attendList = attendDAO.readByStudentNumberInCurrentExecutionPeriod(mwEnrolment.getNumber());
			List attendsWithCurricularCourseCode = (List) CollectionUtils.select(attendList, new Predicate()
					{
				public boolean evaluate(Object input)
				{
					IFrequenta attend = (IFrequenta) input;

					String courseCode = mwEnrolment.getCoursecode();

					List associatedCurricularCourses = attend.getDisciplinaExecucao().getAssociatedCurricularCourses();
					Iterator iterator = associatedCurricularCourses.iterator();
					while (iterator.hasNext())
					{
						ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
						if (curricularCourse.getCode().equals(courseCode))
						{
							return true;
						}
					}
					return false;
				}
			});

			if (attendsWithCurricularCourseCode.size() > 0)
			{
				List associatedCurricularCourses =
				((IFrequenta) attendsWithCurricularCourseCode.get(0)).getDisciplinaExecucao().getAssociatedCurricularCourses();

				curricularCourse = (ICurricularCourse) associatedCurricularCourses.get(0);

				MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite =
				new MWCurricularCourseOutsideStudentDegree();
				persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(mwCurricularCourseOutsideStudentDegreeToWrite);
				mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
				mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
				mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

			} else
			{
				ReportEnrolment.addFoundCurricularCourseInOtherDegrees(
						mwEnrolment.getCoursecode(),
						mwEnrolment.getDegreecode().toString(),
						mwEnrolment.getNumber().toString());
			}
		}

		return curricularCourse;
	}

	/**
	 * @param executionPeriod
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return true/false
	 * @throws Throwable
	 */
	private boolean curricularCourseHasOnlyOneExecutionInGivenPeriod(
			IExecutionPeriod executionPeriod,
			MWEnrolment mwEnrolment,
			IDegreeCurricularPlan degreeCurricularPlan)
	throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = super.persistentSuport.getIPersistentCurricularCourse();

		List curricularCourses =
		persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
				StringUtils.trim(mwEnrolment.getCoursecode()),
				degreeCurricularPlan.getDegree().getTipoCurso(),
				degreeCurricularPlan.getState());

		List executionCourses = new ArrayList();

		Iterator iterator1 = curricularCourses.iterator();
		while (iterator1.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
			List associatedExecutionCourses = curricularCourse.getAssociatedExecutionCourses();
			Iterator iterator2 = associatedExecutionCourses.iterator();
			while (iterator2.hasNext())
			{
				IExecutionCourse executionCourse = (IExecutionCourse) iterator2.next();
				if (executionCourse.getExecutionPeriod().equals(executionPeriod))
				{
					if (!executionCourses.contains(executionCourse))
					{
						executionCourses.add(executionCourse);
					}
				}
			}
		}

		if (executionCourses.size() == 1)
		{
			return true;
		} else
		{
			return false;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// ------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS ----------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param mwEnrolment
	 * @return String
	 */
	private String getRealCurricularCourseCodeForCodesAZx(MWEnrolment mwEnrolment)
	{
		if (mwEnrolment.getCoursecode().equals("AZ2") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "QN";
		} else if (mwEnrolment.getCoursecode().equals("AZ3") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "PY";
		} else if (mwEnrolment.getCoursecode().equals("AZ4") && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
		{
			return "P5";
		} else if (mwEnrolment.getCoursecode().equals("AZ5") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "UN";
		} else if (mwEnrolment.getCoursecode().equals("AZ6") && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
		{
			return "U8";
		} else
		{
			return mwEnrolment.getCoursecode();
		}
	}

}