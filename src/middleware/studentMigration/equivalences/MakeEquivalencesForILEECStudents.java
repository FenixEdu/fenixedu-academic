package middleware.studentMigration.equivalences;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWAreaCientificaIleec;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaCientificaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.studentMigration.enrollments.CreateAndUpdateAllStudentsPastEnrolments;
import Dominio.CreditsInAnySecundaryArea;
import Dominio.CreditsInScientificArea;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEvaluation;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.ICreditsInAnySecundaryArea;
import Dominio.ICreditsInScientificArea;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IExecutionPeriod;
import Dominio.IScientificArea;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentCreditsInAnySecundaryArea;
import ServidorPersistente.IPersistentCreditsInSpecificScientificArea;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos 28/Out/2003
 */

public class MakeEquivalencesForILEECStudents
{
	private static HashMap enrollmentsCreated = new HashMap();
	private static HashMap enrollmentEvaluationsCreated = new HashMap();
	private static HashMap enrolmentEquivalencesCreated = new HashMap();
	private static HashMap equivalentEnrolmentForEnrolmentEquivalencesCreated = new HashMap();

	protected static int totalEnrollmentsCreated = 0;
	protected static int totalEnrollmentEvaluationsCreated = 0;
	private static int totalEnrolmentEquivalencesCreated = 0;
	private static int totalEquivalentEnrolmentForEnrolmentEquivalencesCreated = 0;
	private static int totalCreditsInSpecificScientificArea = 0;
	private static int totalCreditsInAnySecundaryArea = 0;
	private static int deletedEnrollmentEvaluations = 0;
	private static int deletedEnrollments = 0;

	private static HashMap creditEquivalencesType2 = new HashMap();
	private static HashMap creditEquivalencesType5 = new HashMap();
	private static HashMap noEquivalences = new HashMap();
	private static HashMap schools = new HashMap();
	private static HashMap years = new HashMap();

	private static int CREDITS_IN_SPECIFIC_SCIENTIFIC_AREA = 5;
	private static int CREDITS_IN_ANY_SECUNDARY_AREA = 2;
	private static int CREDITS_COURSE_TO_COURSE = 4;
	private static int GIVEN_CREDITS = 4;
	private static int DEGREE_CODE = 14;

	public static void main(String args[])
	{
		IStudent student = null;

		try
		{
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();

			System.out.println("[INFO] Reading Students...");

			fenixPersistentSuport.iniciarTransaccao();

			List result = MakeEquivalencesForILEECStudents.getListOfStudents(fenixPersistentSuport);

			fenixPersistentSuport.confirmarTransaccao();

			System.out.println("[INFO] Total number of student curriculums to update [" + result.size() + "].");

			Iterator iterator = result.iterator();
			while (iterator.hasNext())
			{
				student = (IStudent) iterator.next();
				fenixPersistentSuport.iniciarTransaccao();
				MakeEquivalencesForILEECStudents.makeEquivalences(student, fenixPersistentSuport);
				fenixPersistentSuport.confirmarTransaccao();
				MakeEquivalencesForILEECStudents.enrollmentsCreated.clear();
				MakeEquivalencesForILEECStudents.enrollmentEvaluationsCreated.clear();
				MakeEquivalencesForILEECStudents.enrolmentEquivalencesCreated.clear();
				MakeEquivalencesForILEECStudents.equivalentEnrolmentForEnrolmentEquivalencesCreated.clear();
			}
		} catch (Throwable e)
		{
			System.out.println("[ERROR 401] Exception giving equivalences for student [" + student.getNumber() + "] enrolments!");
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total Enrolments created: [" + MakeEquivalencesForILEECStudents.totalEnrollmentsCreated + "].");
		System.out.println(
			"[INFO] Total EnrolmentEvaluations created: ["
				+ MakeEquivalencesForILEECStudents.totalEnrollmentEvaluationsCreated
				+ "].");
		System.out.println(
			"[INFO] Total Equivalences created: [" + MakeEquivalencesForILEECStudents.totalEnrolmentEquivalencesCreated + "].");
		System.out.println(
			"[INFO] Total CreditsInAnySecundaryArea created: ["
				+ MakeEquivalencesForILEECStudents.totalCreditsInAnySecundaryArea
				+ "].");
		System.out.println(
			"[INFO] Total CreditsInScientificArea created: ["
				+ MakeEquivalencesForILEECStudents.totalCreditsInSpecificScientificArea
				+ "].");
		System.out.println("[INFO] Total deleted Enrollments: [" + MakeEquivalencesForILEECStudents.deletedEnrollments + "].");
		System.out.println(
			"[INFO] Total deleted EnrollmentEvaluations: [" + MakeEquivalencesForILEECStudents.deletedEnrollmentEvaluations + "].");

		MakeEquivalencesForILEECStudents.printReport(new PrintWriter(System.out, true));

	}

	/**
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static List getListOfStudents(ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			fenixPersistentSuport.getIStudentCurricularPlanPersistente();
		List studentsList = new ArrayList();

		IDegreeCurricularPlan degreeCurricularPlan =
			MakeEquivalencesForILEECStudents.getDegreeCurricularPlan(
				new Integer(MakeEquivalencesForILEECStudents.DEGREE_CODE),
				fenixPersistentSuport);
		List studentCurricularPlansList =
			persistentStudentCurricularPlan.readAllByDegreeCurricularPlanAndState(
				degreeCurricularPlan,
				StudentCurricularPlanState.ACTIVE_OBJ);

		Iterator iterator = studentCurricularPlansList.iterator();
		while (iterator.hasNext())
		{
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
			if (!studentsList.contains(studentCurricularPlan.getStudent()))
			{
				studentsList.add(studentCurricularPlan.getStudent());
			}
		}

		if (!studentsList.isEmpty())
		{
			return studentsList;
		} else
		{
			return null;
		}

//		List studentsList = new ArrayList();
//		IStudent student =
//			fenixPersistentSuport.getIPersistentStudent().readStudentByNumberAndDegreeType(
//				new Integer(41124),
//				TipoCurso.LICENCIATURA_OBJ);
//		studentsList.add(student);
//		return studentsList;
	}

	/**
	 * @param degreeCode
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null)
		{
			ICurso degree = mwDegreeTranslation.getDegree();
			List result = persistentDegreeCurricularPlan.readByDegreeAndState(degree, DegreeCurricularPlanState.ACTIVE_OBJ);
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) result.get(0);
			return degreeCurricularPlan;
		} else
		{
			return null;
		}
	}

	/**
	 * @param student
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void makeEquivalences(IStudent student, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlan pastStudentCurricularPlan =
			MakeEquivalencesForILEECStudents.getStudentCurricularPlan(
				student,
				StudentCurricularPlanState.PAST_OBJ,
				fenixPersistentSuport);
		if (pastStudentCurricularPlan == null)
		{
			System.out.println(
				"[ERROR 402] Could not obtain past StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan currentStudentCurricularPlan =
			MakeEquivalencesForILEECStudents.getStudentCurricularPlan(
				student,
				StudentCurricularPlanState.ACTIVE_OBJ,
				fenixPersistentSuport);
		if (pastStudentCurricularPlan == null)
		{
			System.out.println(
				"[ERROR 403] Could not obtain current StudentCurricularPlan for Student with number: ["
					+ student.getNumber()
					+ "]!");
			return;
		}

		MakeEquivalencesForILEECStudents.writeAndUpdateEquivalences(
			student,
			pastStudentCurricularPlan,
			currentStudentCurricularPlan,
			fenixPersistentSuport);
	}

	/**
	 * @param student
	 * @param studentCurricularPlanState
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(
		IStudent student,
		StudentCurricularPlanState studentCurricularPlanState,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, studentCurricularPlanState);
		if ((result != null) && (!result.isEmpty()))
		{
			return (IStudentCurricularPlan) result.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * @param student
	 * @param pastStudentCurricularPlan
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeAndUpdateEquivalences(
		IStudent student,
		IStudentCurricularPlan pastStudentCurricularPlan,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		List realPastEnrolments = pastStudentCurricularPlan.getEnrolments();
		List pastEnrolmentsTemp = MakeEquivalencesForILEECStudents.keepOnlyImprovments(realPastEnrolments, fenixPersistentSuport);
		List pastEnrolments =
			MakeEquivalencesForILEECStudents.solveSpecificCase(
				pastEnrolmentsTemp,
				currentStudentCurricularPlan,
				fenixPersistentSuport);
		Iterator iterator = pastEnrolments.iterator();
		while (iterator.hasNext())
		{

			IEnrolment enrolment = (IEnrolment) iterator.next();

if(enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
{
			if (!MakeEquivalencesForILEECStudents
				.alreadyHasEquivalence(enrolment, currentStudentCurricularPlan, fenixPersistentSuport))
			{
				ICurricularCourse newCurricularCourse =
					MakeEquivalencesForILEECStudents.getCurricularCourse(
						enrolment,
						currentStudentCurricularPlan,
						fenixPersistentSuport);
				if (newCurricularCourse == null)
				{
					continue;
				}

				IEnrolment enrolmentWriten =
					MakeEquivalencesForILEECStudents.writeEnrollment(
						enrolment,
						newCurricularCourse,
						currentStudentCurricularPlan,
						fenixPersistentSuport);
				if (enrolmentWriten == null)
				{
					continue;
				}

				MakeEquivalencesForILEECStudents.writeEquivalences(enrolment, enrolmentWriten, fenixPersistentSuport);

				MakeEquivalencesForILEECStudents.deleteAllEnrollmentsInGivenCurricularCourse(
					enrolment.getCurricularCourse(),
					currentStudentCurricularPlan,
					fenixPersistentSuport);
			}
}
		}
	}

	/**
	 * @param oldEnrolment
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static ICurricularCourse getCurricularCourse(
		IEnrolment oldEnrolment,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();

		IPersistentMWEquivalenciaIleec mwEquivalenciasIleecDAO = mws.getIPersistentMWEquivalenciasIleec();
		IPersistentCurricularCourse curricularCourseDAO = fenixPersistentSuport.getIPersistentCurricularCourse();

		IDegreeCurricularPlan currentDegreeCurricularPlan =
			MakeEquivalencesForILEECStudents.getDegreeCurricularPlan(
				new Integer(MakeEquivalencesForILEECStudents.DEGREE_CODE),
				fenixPersistentSuport);
		ICurricularCourse oldCurricularCourse = oldEnrolment.getCurricularCourse();
		ICurricularCourse newCurricularCourse = null;

		MWEquivalenciaIleec mwEquivalenciaIleec =
			mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(
				new Integer(MakeEquivalencesForILEECStudents.CREDITS_COURSE_TO_COURSE),
				oldCurricularCourse.getCode());
		if (mwEquivalenciaIleec == null)
		{
			Boolean result =
				MakeEquivalencesForILEECStudents.checkForOtherEquivalences(
					oldEnrolment,
					currentStudentCurricularPlan,
					fenixPersistentSuport);
			if (result.equals(Boolean.FALSE))
			{
				MakeEquivalencesForILEECStudents.reportIt(oldEnrolment, fenixPersistentSuport);
			}
			return null;
		}

		List result =
			curricularCourseDAO.readbyCourseCodeAndDegreeCurricularPlan(
				mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual(),
				currentDegreeCurricularPlan);

		if ((result != null) && (!result.isEmpty()))
		{
			if (result.size() == 1)
			{
				newCurricularCourse = (ICurricularCourse) result.get(0);
			} else
			{
				System.out.println(
					"[ERROR 404] Several Fenix CurricularCourses with code ["
						+ mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual()
						+ "] were found for Degree ["
						+ currentDegreeCurricularPlan.getDegree().getNome()
						+ "]!");
			}
		} else
		{
			System.out.println(
				"[ERROR 405] Cannot find Fenix CurricularCourse with code ["
					+ mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual()
					+ "] for Degree ["
					+ currentDegreeCurricularPlan.getDegree().getNome()
					+ "]!");
			return null;
		}

		return newCurricularCourse;
	}

	/**
	 * @param enrolmentToGiveEquivalence
	 * @param equivalentEnrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeEquivalences(
		IEnrolment oldEnrolment,
		IEnrolment newEnrolment,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentEnrolmentEquivalence enrolmentEquivalenceDAO = fenixPersistentSuport.getIPersistentEnrolmentEquivalence();
		IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalenceDAO =
			fenixPersistentSuport.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

		IEnrolmentEquivalence enrolmentEquivalence = enrolmentEquivalenceDAO.readByEnrolment(newEnrolment);
		if (enrolmentEquivalence == null)
		{
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(newEnrolment);
			enrolmentEquivalence = (IEnrolmentEquivalence) MakeEquivalencesForILEECStudents.enrolmentEquivalencesCreated.get(key);
			if (enrolmentEquivalence == null)
			{
				enrolmentEquivalence = new EnrolmentEquivalence();
				enrolmentEquivalenceDAO.simpleLockWrite(enrolmentEquivalence);
				enrolmentEquivalence.setEnrolment(newEnrolment);
				MakeEquivalencesForILEECStudents.enrolmentEquivalencesCreated.put(key, enrolmentEquivalence);
				MakeEquivalencesForILEECStudents.totalEnrolmentEquivalencesCreated++;
			}
		}

		IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalence =
			equivalentEnrolmentForEnrolmentEquivalenceDAO.readByEnrolmentEquivalenceAndEquivalentEnrolment(
				enrolmentEquivalence,
				oldEnrolment);
		if (equivalentEnrolmentForEnrolmentEquivalence == null)
		{
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(oldEnrolment);
			equivalentEnrolmentForEnrolmentEquivalence =
				(IEquivalentEnrolmentForEnrolmentEquivalence) MakeEquivalencesForILEECStudents
					.equivalentEnrolmentForEnrolmentEquivalencesCreated
					.get(key);
			if (equivalentEnrolmentForEnrolmentEquivalence == null)
			{
				equivalentEnrolmentForEnrolmentEquivalence = new EquivalentEnrolmentForEnrolmentEquivalence();
				equivalentEnrolmentForEnrolmentEquivalenceDAO.simpleLockWrite(equivalentEnrolmentForEnrolmentEquivalence);
				equivalentEnrolmentForEnrolmentEquivalence.setEnrolmentEquivalence(enrolmentEquivalence);
				equivalentEnrolmentForEnrolmentEquivalence.setEquivalentEnrolment(oldEnrolment);
				MakeEquivalencesForILEECStudents.equivalentEnrolmentForEnrolmentEquivalencesCreated.put(
					key,
					equivalentEnrolmentForEnrolmentEquivalence);
				MakeEquivalencesForILEECStudents.totalEquivalentEnrolmentForEnrolmentEquivalencesCreated++;
			}
		}
	}

	/**
	 * @param oldEnrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void reportIt(IEnrolment oldEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();

		IPersistentMWEquivalenciaIleec mwEquivalenciasIleecDAO = mws.getIPersistentMWEquivalenciasIleec();

		ICurricularCourse oldCurricularCourse = oldEnrolment.getCurricularCourse();
		IStudent student = oldEnrolment.getStudentCurricularPlan().getStudent();
		Integer equivalenceType = new Integer(MakeEquivalencesForILEECStudents.CREDITS_IN_ANY_SECUNDARY_AREA);

		MWEquivalenciaIleec mwEquivalenciaIleec =
			mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(
				equivalenceType,
				oldCurricularCourse.getCode());
		if (mwEquivalenciaIleec == null)
		{
			equivalenceType = new Integer(MakeEquivalencesForILEECStudents.CREDITS_IN_SPECIFIC_SCIENTIFIC_AREA);
			mwEquivalenciaIleec =
				mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(
					equivalenceType,
					oldCurricularCourse.getCode());
		}

		if (mwEquivalenciaIleec == null)
		{
			String key = "Código: [" + oldCurricularCourse.getCode() + "] e Nome: [" + oldCurricularCourse.getName() + "]";

			List cases = (List) MakeEquivalencesForILEECStudents.noEquivalences.get(key);
			if (cases == null)
			{
				cases = new ArrayList();
				cases.add(student.getNumber().toString());
			} else
			{
				if (!cases.contains(student.getNumber().toString()))
				{
					cases.add(student.getNumber().toString());
				}
			}
			MakeEquivalencesForILEECStudents.noEquivalences.put(key, cases);

			cases = (List) MakeEquivalencesForILEECStudents.years.get(key);
			if (cases == null)
			{
				cases = new ArrayList();
				cases.add(oldEnrolment.getExecutionPeriod().getExecutionYear().getYear());
			} else
			{
				if (!cases.contains(oldEnrolment.getExecutionPeriod().getExecutionYear().getYear()))
				{
					cases.add(oldEnrolment.getExecutionPeriod().getExecutionYear().getYear());
				}
			}
			MakeEquivalencesForILEECStudents.years.put(key, cases);

			String rest = "Escola: [" + oldCurricularCourse.getUniversity().getName() + "]";
			String aux = (String) MakeEquivalencesForILEECStudents.schools.get(key);
			if (aux == null)
			{
				MakeEquivalencesForILEECStudents.schools.put(key, rest);
			}
		} else
		{
			String key = "Código: [" + oldCurricularCourse.getCode() + "] e Nome: [" + oldCurricularCourse.getName() + "]";

			List cases = null;

			if (equivalenceType.intValue() == MakeEquivalencesForILEECStudents.CREDITS_IN_ANY_SECUNDARY_AREA)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType2.get(key);
			} else if (equivalenceType.intValue() == MakeEquivalencesForILEECStudents.CREDITS_IN_SPECIFIC_SCIENTIFIC_AREA)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType5.get(key);
			}

			if (cases == null)
			{
				cases = new ArrayList();
				cases.add(student.getNumber().toString());
			} else
			{
				if (!cases.contains(student.getNumber().toString()))
				{
					cases.add(student.getNumber().toString());
				}
			}

			if (equivalenceType.intValue() == MakeEquivalencesForILEECStudents.CREDITS_IN_ANY_SECUNDARY_AREA)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType2.put(key, cases);
			} else if (equivalenceType.intValue() == MakeEquivalencesForILEECStudents.CREDITS_IN_SPECIFIC_SCIENTIFIC_AREA)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType5.put(key, cases);
			}
		}
	}

	/**
	 * @param out
	 * @throws Throwable
	 */
	private static void printReport(PrintWriter out)
	{
		if (!MakeEquivalencesForILEECStudents.noEquivalences.entrySet().isEmpty())
		{
			int total = 0;
			out.println("\nDISCIPLINAS SEM EQUIVALÊNCIA");
			out.println("\nAS INSCRIÇÕES SÃO:");
			Iterator iterator = MakeEquivalencesForILEECStudents.noEquivalences.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List cases = (List) mapEntry.getValue();
				out.println("\t" + key + ". Aconteceu [" + cases.size() + "] vezes!");
				out.println("\tAlunos: " + cases);
				total = total + cases.size();
			}
			out.println("TOTAL DE INSCRIÇÕES: " + total);
			out.println("\nAS DISCIPLINAS SÃO:");
			iterator = MakeEquivalencesForILEECStudents.noEquivalences.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List cases = (List) MakeEquivalencesForILEECStudents.years.get(key);
				String rest = (String) MakeEquivalencesForILEECStudents.schools.get(key) + ", Anos: " + cases;
				out.println("\t" + key + " - " + rest);
			}
			out.println("TOTAL DE DISCIPLINAS: " + MakeEquivalencesForILEECStudents.noEquivalences.size());
		}

		if (!MakeEquivalencesForILEECStudents.creditEquivalencesType2.entrySet().isEmpty())
		{
			int total = 0;
			out.println("\nDISCIPLINAS COM EQUIVALÊNCIAS DO TIPO \"CRÉDITOS EM QUALQUER ÁREA SECUNDÁRIA\"");
			out.println("\nAS INSCRIÇÕES SÃO:");
			Iterator iterator = MakeEquivalencesForILEECStudents.creditEquivalencesType2.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List cases = (List) mapEntry.getValue();
				out.println("\t" + key + ". Aconteceu [" + cases.size() + "] vezes!");
				out.println("\tAlunos: " + cases);
				total = total + cases.size();
			}
			out.println("TOTAL DE INSCRIÇÕES: " + total);
			out.println("\nAS DISCIPLINAS SÃO:");
			iterator = MakeEquivalencesForILEECStudents.creditEquivalencesType2.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				out.println("\t" + key);
			}
			out.println("TOTAL DE DISCIPLINAS: " + MakeEquivalencesForILEECStudents.creditEquivalencesType2.size());
		}

		if (!MakeEquivalencesForILEECStudents.creditEquivalencesType5.entrySet().isEmpty())
		{
			int total = 0;
			out.println("\nDISCIPLINAS COM EQUIVALÊNCIAS DO TIPO \"CRÉDITOS EM ÁREA CIENTÍFICA ESPECIFICA\"");
			out.println("\nAS INSCRIÇÕES SÃO:");
			Iterator iterator = MakeEquivalencesForILEECStudents.creditEquivalencesType5.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List cases = (List) mapEntry.getValue();
				out.println("\t" + key + ". Aconteceu [" + cases.size() + "] vezes!");
				out.println("\tAlunos: " + cases);
				total = total + cases.size();
			}
			out.println("TOTAL DE INSCRIÇÕES: " + total);
			out.println("\nAS DISCIPLINAS SÃO:");
			iterator = MakeEquivalencesForILEECStudents.creditEquivalencesType5.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				out.println("\t" + key);
			}
			out.println("TOTAL DE DISCIPLINAS: " + MakeEquivalencesForILEECStudents.creditEquivalencesType5.size());
		}
	}

	/**
	 * @param enrolment
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static boolean alreadyHasEquivalence(
		IEnrolment enrolment,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		boolean result = false;

		ICurricularCourse curricularCourse =
			MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseFromCurrentDegreeCurricularPlan(
				enrolment.getCurricularCourse(),
				currentStudentCurricularPlan.getDegreeCurricularPlan(),
				fenixPersistentSuport);
		if (curricularCourse == null)
		{
			result = false;
		} else
		{
			result = true;
		}
		return result;
	}

	/**
	 * @param pastStudentCurricularPlan
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static List solveSpecificCase(
		List pastEnrolments,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IEnrolment enrolmentInSE = null;
		IEnrolment enrolmentInSH = null;
		List newListOfEnrollments = null;

		Iterator iterator = pastEnrolments.iterator();
		while (iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (enrolment.getCurricularCourse().getCode().equals("SE")
				&& enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
			{
				enrolmentInSE = enrolment;
			} else if (
				enrolment.getCurricularCourse().getCode().equals("SH")
					&& enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
			{
				enrolmentInSH = enrolment;
			}
		}

		if (enrolmentInSE != null && enrolmentInSH != null)
		{
			ICurricularCourse curricularCourseCriteria = new CurricularCourse();
			curricularCourseCriteria.setCode("W9");
			curricularCourseCriteria.setName("ELECTRÓNICA II");

			ICurricularCourse newCurricularCourse =
				MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseFromCurrentDegreeCurricularPlan(
					curricularCourseCriteria,
					currentStudentCurricularPlan.getDegreeCurricularPlan(),
					fenixPersistentSuport);

			IEnrolment enrolmentWriten =
				MakeEquivalencesForILEECStudents.writeEnrollment(
					enrolmentInSE,
					newCurricularCourse,
					currentStudentCurricularPlan,
					fenixPersistentSuport);

			MakeEquivalencesForILEECStudents.writeEquivalences(enrolmentInSE, enrolmentWriten, fenixPersistentSuport);
			MakeEquivalencesForILEECStudents.writeEquivalences(enrolmentInSH, enrolmentWriten, fenixPersistentSuport);

			newListOfEnrollments = new ArrayList();
			newListOfEnrollments.addAll(pastEnrolments);
			newListOfEnrollments.remove(enrolmentInSE);
			newListOfEnrollments.remove(enrolmentInSH);

			MakeEquivalencesForILEECStudents.deleteAllEnrollmentsInGivenCurricularCourse(
				enrolmentInSE.getCurricularCourse(),
				currentStudentCurricularPlan,
				fenixPersistentSuport);
		} else if (enrolmentInSE != null && enrolmentInSH == null)
		{
			ICurricularCourse curricularCourseCriteria = new CurricularCourse();
			curricularCourseCriteria.setCode("ASM");
			curricularCourseCriteria.setName("PROPRIEDADES ELECTROMAGNÉTICAS DOS MATERIAIS");

			ICurricularCourse newCurricularCourse =
				MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseFromCurrentDegreeCurricularPlan(
					curricularCourseCriteria,
					currentStudentCurricularPlan.getDegreeCurricularPlan(),
					fenixPersistentSuport);

			IEnrolment enrolmentWriten =
				MakeEquivalencesForILEECStudents.writeEnrollment(
					enrolmentInSE,
					newCurricularCourse,
					currentStudentCurricularPlan,
					fenixPersistentSuport);

			MakeEquivalencesForILEECStudents.writeEquivalences(enrolmentInSE, enrolmentWriten, fenixPersistentSuport);

			newListOfEnrollments = new ArrayList();
			newListOfEnrollments.addAll(pastEnrolments);
			newListOfEnrollments.remove(enrolmentInSE);

			MakeEquivalencesForILEECStudents.deleteAllEnrollmentsInGivenCurricularCourse(
				enrolmentInSE.getCurricularCourse(),
				currentStudentCurricularPlan,
				fenixPersistentSuport);
		} else
		{
			newListOfEnrollments = pastEnrolments;
		}

		MakeEquivalencesForILEECStudents.enrollmentsCreated.clear();
		MakeEquivalencesForILEECStudents.enrollmentEvaluationsCreated.clear();
		MakeEquivalencesForILEECStudents.enrolmentEquivalencesCreated.clear();
		MakeEquivalencesForILEECStudents.equivalentEnrolmentForEnrolmentEquivalencesCreated.clear();

		return newListOfEnrollments;
	}

	/**
	 * @param enrolment
	 * @param curricularCourse
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	protected static IEnrolment writeEnrollment(
		IEnrolment enrolment,
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentEnrolment persistentEnrolment = fenixPersistentSuport.getIPersistentEnrolment();

		IExecutionPeriod executionPeriod = enrolment.getExecutionPeriod();

		IEnrolment enrolmentToWrite =
			persistentEnrolment.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				currentStudentCurricularPlan,
				curricularCourse,
				executionPeriod);

		if (enrolmentToWrite == null)
		{
			IEnrolment enrolmentToObtainKey = new Enrolment();
			enrolmentToObtainKey.setStudentCurricularPlan(currentStudentCurricularPlan);
			enrolmentToObtainKey.setCurricularCourse(curricularCourse);
			enrolmentToObtainKey.setExecutionPeriod(executionPeriod);
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(enrolmentToObtainKey);

			enrolmentToWrite = (IEnrolment) MakeEquivalencesForILEECStudents.enrollmentsCreated.get(key);

			if (enrolmentToWrite == null)
			{
				enrolmentToWrite = new Enrolment();

				persistentEnrolment.simpleLockWrite(enrolmentToWrite);

				enrolmentToWrite.setCurricularCourse(curricularCourse);
				enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
				enrolmentToWrite.setEnrolmentState(enrolment.getEnrolmentState());
				enrolmentToWrite.setExecutionPeriod(executionPeriod);
				enrolmentToWrite.setStudentCurricularPlan(currentStudentCurricularPlan);

				MakeEquivalencesForILEECStudents.enrollmentsCreated.put(key, enrolment);
				MakeEquivalencesForILEECStudents.totalEnrollmentsCreated++;
			}
		}

		if (enrolmentToWrite != null)
		{
			MakeEquivalencesForILEECStudents.writeEnrollmentEvaluation(enrolment, enrolmentToWrite, fenixPersistentSuport);
		}

		return enrolmentToWrite;
	}

	/**
	 * @param oldEnrolment
	 * @param newEnrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeEnrollmentEvaluation(
		IEnrolment oldEnrolment,
		IEnrolment newEnrolment,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();
		IEnrolmentEvaluation enrolmentEvaluation = MakeEquivalencesForILEECStudents.getLatestEvaluation(oldEnrolment);

		IEnrolmentEvaluation enrolmentEvaluationToWrite =
			persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
				newEnrolment,
				EnrolmentEvaluationType.EQUIVALENCE_OBJ,
				enrolmentEvaluation.getGrade(),
				enrolmentEvaluation.getWhen());

		if (enrolmentEvaluationToWrite == null)
		{
			IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
			enrolmentEvaluationToObtainKey.setEnrolment(newEnrolment);
			enrolmentEvaluationToObtainKey.setGrade(enrolmentEvaluation.getGrade());
			enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
			enrolmentEvaluationToObtainKey.setWhen(enrolmentEvaluation.getWhen());
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

			enrolmentEvaluationToWrite =
				(IEnrolmentEvaluation) MakeEquivalencesForILEECStudents.enrollmentEvaluationsCreated.get(key);

			if (enrolmentEvaluationToWrite == null)
			{
				enrolmentEvaluationToWrite = new EnrolmentEvaluation();

				fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluationToWrite);

				enrolmentEvaluationToWrite.setEnrolment(newEnrolment);
				enrolmentEvaluationToWrite.setEnrolmentEvaluationState(enrolmentEvaluation.getEnrolmentEvaluationState());
				enrolmentEvaluationToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
				enrolmentEvaluationToWrite.setExamDate(enrolmentEvaluation.getExamDate());
				enrolmentEvaluationToWrite.setGrade(enrolmentEvaluation.getGrade());
				enrolmentEvaluationToWrite.setObservation(enrolmentEvaluation.getObservation());
				enrolmentEvaluationToWrite.setPersonResponsibleForGrade(enrolmentEvaluation.getPersonResponsibleForGrade());
				enrolmentEvaluationToWrite.setGradeAvailableDate(enrolmentEvaluation.getGradeAvailableDate());
				enrolmentEvaluationToWrite.setWhen(enrolmentEvaluation.getWhen());
				enrolmentEvaluationToWrite.setEmployee(enrolmentEvaluation.getEmployee());
				enrolmentEvaluationToWrite.setAckOptLock(new Integer(1));
				
				enrolmentEvaluationToWrite.setCheckSum(null);

				MakeEquivalencesForILEECStudents.enrollmentEvaluationsCreated.put(key, enrolmentEvaluationToWrite);
				MakeEquivalencesForILEECStudents.totalEnrollmentEvaluationsCreated++;
			}
		}
	}

	/**
	 * @param enrolment
	 * @return @throws
	 *         Throwable
	 */
	private static IEnrolmentEvaluation getLatestEvaluation(IEnrolment enrolment) throws Throwable
	{
		List enrolmentEvaluations = enrolment.getEvaluations();

		if ((enrolment == null) || (enrolment.getEvaluations() == null) || (enrolment.getEvaluations().size() == 0))
		{
			System.out.print("[WARNING 401] ENROLLMENT IS NULL: ");
			System.out.println(enrolment == null);
			System.out.println("[WARNING 401] EVALUATIONS SIZE: " + enrolment.getEvaluations().size());
			return null;
		} else
		{
			// This sorts the list ascendingly so we need to reverse it to get the first object.
			Collections.sort(enrolmentEvaluations);
			Collections.reverse(enrolmentEvaluations);
			return (IEnrolmentEvaluation) enrolmentEvaluations.get(0);
		}
	}

	/**
	 * @param enrollmentsList
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	protected static List keepOnlyImprovments(List enrollmentsList, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolment enrolmentDAO = fenixPersistentSuport.getIPersistentEnrolment();

		List enrollmentsToRemove = new ArrayList();
		List enrollmentsToReturn = new ArrayList();

		Iterator enrollmentIterator = enrollmentsList.iterator();
		while (enrollmentIterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) enrollmentIterator.next();

			IEnrolmentEvaluation enrolmentEvaluation = MakeEquivalencesForILEECStudents.getLatestEvaluation(enrolment);

			if (enrolmentEvaluation != null
				&& enrolmentEvaluation.getEnrolmentEvaluationType() != null
				&& enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ))
			{
				List enrolmentsInSameCourse =
					enrolmentDAO.readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
						enrolment.getStudentCurricularPlan(),
						enrolment.getCurricularCourse(),
						enrolment.getExecutionPeriod());

				if (enrolmentsInSameCourse != null || !enrolmentsInSameCourse.isEmpty())
				{
					Iterator iterator = enrolmentsInSameCourse.iterator();
					while (iterator.hasNext())
					{
						IEnrolment enrolmentInSameCourse = (IEnrolment) iterator.next();

						IEnrolmentEvaluation evaluationInSameCourse =
							MakeEquivalencesForILEECStudents.getLatestEvaluation(enrolmentInSameCourse);

						try
						{
							Integer improvmentGrade = new Integer(enrolmentEvaluation.getGrade());
							Integer currentGrade = new Integer(evaluationInSameCourse.getGrade());
							if (currentGrade.intValue() <= improvmentGrade.intValue())
							{
								if (!enrollmentsToRemove.contains(enrolmentInSameCourse))
								{
									enrollmentsToRemove.add(enrolmentInSameCourse);
								}
							} else
							{
								if (!enrollmentsToRemove.contains(enrolment))
								{
									enrollmentsToRemove.add(enrolment);
								}
							}
						} catch (NumberFormatException e)
						{
						}
					}
				}
			}

		}

		enrollmentsToReturn.addAll(enrollmentsList);
		enrollmentsToReturn.removeAll(enrollmentsToRemove);
		return enrollmentsToReturn;
	}

	/**
	 * @param oldEnrolment
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @return @throws
	 *         Throwable
	 */
	private static Boolean checkForOtherEquivalences(
		IEnrolment oldEnrolment,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		if (oldEnrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
		{
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWEquivalenciaIleec mwEquivalenciasIleecDAO = mws.getIPersistentMWEquivalenciasIleec();

			IPersistentCreditsInAnySecundaryArea creditsInAnySecundaryAreaDAO =
				fenixPersistentSuport.getIPersistentCreditsInAnySecundaryArea();
			IPersistentCreditsInSpecificScientificArea creditsInSpecificScientificAreaDAO =
				fenixPersistentSuport.getIPersistentCreditsInSpecificScientificArea();

			ICurricularCourse oldCurricularCourse = oldEnrolment.getCurricularCourse();

			MWEquivalenciaIleec mwEquivalenciaIleec =
				mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(
					new Integer(MakeEquivalencesForILEECStudents.CREDITS_IN_ANY_SECUNDARY_AREA),
					oldCurricularCourse.getCode());
			if (mwEquivalenciaIleec == null)
			{
				mwEquivalenciaIleec =
					mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(
						new Integer(MakeEquivalencesForILEECStudents.CREDITS_IN_SPECIFIC_SCIENTIFIC_AREA),
						oldCurricularCourse.getCode());
				if (mwEquivalenciaIleec == null)
				{
					return Boolean.FALSE;
				} else
				{
					IScientificArea scientificArea =
						MakeEquivalencesForILEECStudents.getScientificArea(mwEquivalenciaIleec, fenixPersistentSuport);
					if (scientificArea == null)
					{
						return Boolean.FALSE;
					}

					ICreditsInScientificArea creditsInSpecificScientificArea =
						creditsInSpecificScientificAreaDAO.readByStudentCurricularPlanAndEnrollmentAndScientificArea(
							currentStudentCurricularPlan,
							oldEnrolment,
							scientificArea);
					if (creditsInSpecificScientificArea == null)
					{
						creditsInSpecificScientificArea = new CreditsInScientificArea();
						creditsInSpecificScientificAreaDAO.simpleLockWrite(creditsInSpecificScientificArea);
						creditsInSpecificScientificArea.setEnrolment(oldEnrolment);
						creditsInSpecificScientificArea.setGivenCredits(new Integer(MakeEquivalencesForILEECStudents.GIVEN_CREDITS));
						creditsInSpecificScientificArea.setScientificArea(scientificArea);
						creditsInSpecificScientificArea.setStudentCurricularPlan(currentStudentCurricularPlan);
						MakeEquivalencesForILEECStudents.totalCreditsInSpecificScientificArea++;
					}
				}
			} else
			{
				ICreditsInAnySecundaryArea creditsInAnySecundaryArea =
					creditsInAnySecundaryAreaDAO.readByStudentCurricularPlanAndEnrollment(
						currentStudentCurricularPlan,
						oldEnrolment);
				if (creditsInAnySecundaryArea == null)
				{
					creditsInAnySecundaryArea = new CreditsInAnySecundaryArea();
					creditsInAnySecundaryAreaDAO.simpleLockWrite(creditsInAnySecundaryArea);
					creditsInAnySecundaryArea.setEnrolment(oldEnrolment);
					creditsInAnySecundaryArea.setGivenCredits(new Integer(MakeEquivalencesForILEECStudents.GIVEN_CREDITS));
					creditsInAnySecundaryArea.setStudentCurricularPlan(currentStudentCurricularPlan);
					MakeEquivalencesForILEECStudents.totalCreditsInAnySecundaryArea++;
				}
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	protected static void deleteAllEnrollmentsInGivenCurricularCourse(
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan studentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentEnrolment enrolmentDAO = fenixPersistentSuport.getIPersistentEnrolment();

		List enrollments = enrolmentDAO.readByStudentCurricularPlanAndCurricularCourse(studentCurricularPlan, curricularCourse);

		if (enrollments != null)
		{
			Iterator iterator = enrollments.iterator();
			while (iterator.hasNext())
			{
				IEnrolment enrollment = (IEnrolment) iterator.next();
				MakeEquivalencesForILEECStudents.deleteEnrollment(enrollment, fenixPersistentSuport);
			}
		}
	}

	/**
	 * @param enrollment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	protected static void deleteEnrollment(IEnrolment enrollment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolment enrolmentDAO = fenixPersistentSuport.getIPersistentEnrolment();
		IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();

		if (enrollment != null)
		{
			Iterator iterator = enrollment.getEvaluations().iterator();
			while (iterator.hasNext())
			{
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
				enrolmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
				System.out.println("APAGUEI ENROLLMENT_EVALUATION\t" + enrolmentEvaluation.getEnrolment().getCurricularCourse().getCode() + "\t" + enrolmentEvaluation.getEnrolment().getCurricularCourse().getName() + "\t" + enrolmentEvaluation.getEnrolment().getExecutionPeriod().getIdInternal().toString() + "\t" + enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getStudent().getNumber().toString() + "\t" + enrolmentEvaluation.getEnrolment().getCurricularCourse().getDegreeCurricularPlan().getName() + "\t" + enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan().getName() + "\t" + enrolmentEvaluation.getGrade());
				MakeEquivalencesForILEECStudents.deletedEnrollmentEvaluations++;
			}
			enrolmentDAO.deleteByOID(Enrolment.class, enrollment.getIdInternal());
			System.out.println("APAGUEI ENROLLMENT\t" + enrollment.getCurricularCourse().getCode() + "\t" + enrollment.getCurricularCourse().getName() + "\t" + enrollment.getExecutionPeriod().getIdInternal().toString() + "\t" + enrollment.getStudentCurricularPlan().getStudent().getNumber().toString() + "\t" + enrollment.getCurricularCourse().getDegreeCurricularPlan().getName() + "\t" + enrollment.getStudentCurricularPlan().getDegreeCurricularPlan().getName());
			MakeEquivalencesForILEECStudents.deletedEnrollments++;
		}
	}

	/**
	 * @param mwEquivalenciaIleec
	 * @param fenixPersistentSuport
	 * @return IScientificArea
	 * @throws Throwable
	 */
	private static IScientificArea getScientificArea(
		MWEquivalenciaIleec mwEquivalenciaIleec,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAreaCientificaIleec mwAreaCientificaIleecDAO = mws.getIPersistentMWAreaCientificaIleec();
		IPersistentScientificArea scientificAreaDAO = fenixPersistentSuport.getIPersistentScientificArea();

		MWAreaCientificaIleec mwAreaCientificaIleec = mwAreaCientificaIleecDAO.readById(mwEquivalenciaIleec.getIdAreaCientifica());
		if (mwAreaCientificaIleec == null)
		{
			System.out.println(
				"[ERROR 409] No record of ScientificArea with ID [" + mwEquivalenciaIleec.getIdAreaCientifica().toString() + "]!");
			return null;
		}
		IScientificArea scientificArea = scientificAreaDAO.readByName(mwAreaCientificaIleec.getNome());
		if (scientificArea == null)
		{
			System.out.println("[ERROR 410] Cannot find Fenix ScientificArea with name [" + mwAreaCientificaIleec.getNome() + "]!");
			return null;
		}

		return scientificArea;
	}

}