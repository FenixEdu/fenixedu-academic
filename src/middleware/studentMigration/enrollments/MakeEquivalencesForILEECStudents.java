package middleware.studentMigration.enrollments;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.middlewareDomain.MWEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWEquivalenciasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.EnrolmentEquivalence;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class MakeEquivalencesForILEECStudents
{
	private static int totalEnrolmentEquivalencesCreated = 0;
	private static int totalEquivalentEnrolmentForEnrolmentEquivalencesCreated = 0;

	private static HashMap enrolmentEquivalencesCreated = new HashMap();
	private static HashMap equivalentEnrolmentForEnrolmentEquivalencesCreated = new HashMap();
	
	private static HashMap creditEquivalencesType2 = new HashMap();
	private static HashMap creditEquivalencesType5 = new HashMap();
	private static HashMap noEquivalences = new HashMap();
	
	public static void main(String args[])
	{
		IStudent student = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();

			System.out.println("[INFO] Reading Students...");

			fenixPersistentSuport.iniciarTransaccao();
			
			List result = MakeEquivalencesForILEECStudents.getListOfStudents(fenixPersistentSuport);
	
			fenixPersistentSuport.confirmarTransaccao();
	
			System.out.println("[INFO] Total number of student curriculums to update [" + result.size() + "].");

			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				student = (IStudent) iterator.next();
//				System.out.println("[INFO] Updating student with number [" + student.getNumber() + "]...");
				fenixPersistentSuport.iniciarTransaccao();
				MakeEquivalencesForILEECStudents.makeEquivalences(student, fenixPersistentSuport);
				fenixPersistentSuport.confirmarTransaccao();
				MakeEquivalencesForAllStudentsPastEnrolments.enrollmentsCreated.clear();
				MakeEquivalencesForAllStudentsPastEnrolments.enrollmentEvaluationsCreated.clear();
				MakeEquivalencesForILEECStudents.enrolmentEquivalencesCreated.clear();
				MakeEquivalencesForILEECStudents.equivalentEnrolmentForEnrolmentEquivalencesCreated.clear();
			}
		} catch (Throwable e) {
			System.out.println("[ERROR 401] Exception giving equivalences for student [" + student.getNumber() + "] enrolments!");
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total Enrolments created: [" + MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentsCreated + "].");
		System.out.println("[INFO] Total EnrolmentEvaluations created: [" + MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentEvaluationsCreated + "].");
		System.out.println("[INFO] Total Equivalences created: [" + MakeEquivalencesForILEECStudents.totalEnrolmentEquivalencesCreated + "].");

		MakeEquivalencesForILEECStudents.printReport(new PrintWriter(System.out, true));

	}

	/**
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static List getListOfStudents(ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();
		List studentsList = new ArrayList();
		
		IDegreeCurricularPlan degreeCurricularPlan = MakeEquivalencesForILEECStudents.getDegreeCurricularPlan(new Integer(14), fenixPersistentSuport);
		List studentCurricularPlansList = persistentStudentCurricularPlan.readAllByDegreeCurricularPlanAndState(degreeCurricularPlan, StudentCurricularPlanState.ACTIVE_OBJ);
		
		Iterator iterator = studentCurricularPlansList.iterator();
		while(iterator.hasNext())
		{
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
			if(!studentsList.contains(studentCurricularPlan.getStudent()))
			{
				studentsList.add(studentCurricularPlan.getStudent());
			}
		}

		if(!studentsList.isEmpty())
		{
			return studentsList;
		} else
		{
			return null;
		}
	}

	/**
	 * @param degreeCode
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null) {
			ICurso degree = mwDegreeTranslation.getDegree();
			List result = persistentDegreeCurricularPlan.readByDegreeAndState(degree, DegreeCurricularPlanState.ACTIVE_OBJ);
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) result.get(0);
			return degreeCurricularPlan;
		} else {
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
		IStudentCurricularPlan pastStudentCurricularPlan = MakeEquivalencesForILEECStudents.getStudentCurricularPlan(student, StudentCurricularPlanState.PAST_OBJ, fenixPersistentSuport);
		if (pastStudentCurricularPlan == null) {
			System.out.println("[ERROR 402] Could not obtain past StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}
		
		IStudentCurricularPlan currentStudentCurricularPlan = MakeEquivalencesForILEECStudents.getStudentCurricularPlan(student, StudentCurricularPlanState.ACTIVE_OBJ, fenixPersistentSuport);
		if (pastStudentCurricularPlan == null) {
			System.out.println("[ERROR 403] Could not obtain current StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}
		
		MakeEquivalencesForILEECStudents.writeAndUpdateEquivalences(student, pastStudentCurricularPlan, currentStudentCurricularPlan, fenixPersistentSuport);
	}

	/**
	 * @param student
	 * @param studentCurricularPlanState
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(IStudent student, StudentCurricularPlanState studentCurricularPlanState,ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, studentCurricularPlanState);
		if ((result != null) && (!result.isEmpty())) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
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
	private static void writeAndUpdateEquivalences(IStudent student, IStudentCurricularPlan pastStudentCurricularPlan, IStudentCurricularPlan currentStudentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		List pastEnrolments = pastStudentCurricularPlan.getEnrolments();
		Iterator iterator = pastEnrolments.iterator();
		while (iterator.hasNext()) {

			IEnrolment enrolment = (IEnrolment) iterator.next();

			if(!MakeEquivalencesForILEECStudents.alreadyHasEquivalence(enrolment, currentStudentCurricularPlan, fenixPersistentSuport))
			{
				ICurricularCourse newCurricularCourse = MakeEquivalencesForILEECStudents.getCurricularCourse(enrolment, fenixPersistentSuport);
				if(newCurricularCourse == null)
				{
					continue;
				}
	
				ICurricularCourseScope newCurricularCourseScope = MakeEquivalencesForILEECStudents.getCurricularCourseScope(enrolment, newCurricularCourse, fenixPersistentSuport);
				if(newCurricularCourseScope == null)
				{
					continue;
				}
	
				IEnrolment enrolmentWriten = MakeEquivalencesForAllStudentsPastEnrolments.writeEnrollment(enrolment, newCurricularCourseScope, currentStudentCurricularPlan, fenixPersistentSuport);
				if(enrolmentWriten == null)
				{
					continue;
				}
				MakeEquivalencesForILEECStudents.writeEquivalences(enrolment, enrolmentWriten, fenixPersistentSuport);
			}
		}
	}

	/**
	 * @param oldEnrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourse getCurricularCourse(IEnrolment oldEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();

		IPersistentMWEquivalenciasIleec mwEquivalenciasIleecDAO = mws.getIPersistentMWEquivalenciasIleec();
		IPersistentMWDisciplinasIleec mwDisciplinasIleecDAO = mws.getIPersistentMWDisciplinasIleec();
		IPersistentCurricularCourse curricularCourseDAO = fenixPersistentSuport.getIPersistentCurricularCourse();

		IDegreeCurricularPlan currentDegreeCurricularPlan = MakeEquivalencesForILEECStudents.getDegreeCurricularPlan(new Integer(14), fenixPersistentSuport);
		ICurricularCourse oldCurricularCourse = oldEnrolment.getCurricularCourseScope().getCurricularCourse();
		ICurricularCourse newCurricularCourse = null;

		MWEquivalenciaIleec mwEquivalenciaIleec = mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(new Integer(4), oldCurricularCourse.getCode());
		if(mwEquivalenciaIleec == null)
		{
			MakeEquivalencesForILEECStudents.reportIt(oldEnrolment, fenixPersistentSuport);
			return null;
		}

		List result = curricularCourseDAO.readbyCourseCodeAndDegreeCurricularPlan(mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual(), currentDegreeCurricularPlan);

		if ((result != null) && (!result.isEmpty()))
		{
			if (result.size() == 1)
			{
				newCurricularCourse = (ICurricularCourse) result.get(0);
			} else
			{
				System.out.println("[ERROR 404] Several Fenix CurricularCourses with code [" + mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual() + "] were found for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
			}
		} else
		{
			MWDisciplinaIleec mwDisciplinaIleec = mwDisciplinasIleecDAO.readByCodigoDisciplina(mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual());
			System.out.println("[ERROR 405] Cannot find Fenix CurricularCourse with code [" + mwEquivalenciaIleec.getCodigoDisciplinaCurriculoActual() + "] and name [" + mwDisciplinaIleec.getNome() + "] for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
			return null;
		}
		
		return newCurricularCourse;
	}

	/**
	 * @param newCurricularCourse
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourseScope getCurricularCourseScope(IEnrolment oldEnrolment, ICurricularCourse newCurricularCourse, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();

		IPersistentMWDisciplinasIleec mwDisciplinasIleecDAO = mws.getIPersistentMWDisciplinasIleec();
		IPersistentCurricularCourseScope persistentCurricularCourseScope = fenixPersistentSuport.getIPersistentCurricularCourseScope();
		
		Integer year = null;
		Integer semester = null;
		List result = null;

		MWDisciplinaIleec mwDisciplinaIleec = mwDisciplinasIleecDAO.readByCodigoDisciplina(newCurricularCourse.getCode());
		if(mwDisciplinaIleec == null)
		{
			return null;
		}

		if(mwDisciplinaIleec.getAnoCurricular().intValue() == 0)
		{
			year = new Integer(4);
			result = persistentCurricularCourseScope.readByCurricularCourseAndYear(newCurricularCourse, year);
			if ((result == null) || (result.isEmpty()))
			{
				year = new Integer(5);
				result = persistentCurricularCourseScope.readByCurricularCourseAndYear(newCurricularCourse, year);
				if ((result == null) || (result.isEmpty()))
				{
					year = oldEnrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear();
					result = persistentCurricularCourseScope.readByCurricularCourseAndYear(newCurricularCourse, year);
					if ((result == null) || (result.isEmpty()))
					{
						System.out.println("[ERROR 406] Cannot find Fenix CurricularCourseScope for CurricularCourse with code [" + newCurricularCourse.getCode() + "] and name [" + newCurricularCourse.getName() + "] in period [year: " + mwDisciplinaIleec.getAnoCurricular().toString() + " semester: " + mwDisciplinaIleec.getSemestre().toString() + "]!");
						return null;
					}
				}
			}
		} else
		{
			year = mwDisciplinaIleec.getAnoCurricular();
		}

		if(mwDisciplinaIleec.getSemestre().intValue() == 3)
		{
			semester = new Integer(1);
			result = persistentCurricularCourseScope.readByCurricularCourseAndYearAndSemester(newCurricularCourse, year, semester);
			if ((result == null) || (result.isEmpty()))
			{
				semester = new Integer(2);
				result = persistentCurricularCourseScope.readByCurricularCourseAndYearAndSemester(newCurricularCourse, year, semester);
				if ((result == null) || (result.isEmpty()))
				{
					semester = oldEnrolment.getCurricularCourseScope().getCurricularSemester().getSemester();
					result = persistentCurricularCourseScope.readByCurricularCourseAndYearAndSemester(newCurricularCourse, year, semester);
					if ((result == null) || (result.isEmpty()))
					{
						System.out.println("[ERROR 407] Cannot find Fenix CurricularCourseScope for CurricularCourse with code [" + newCurricularCourse.getCode() + "] and name [" + newCurricularCourse.getName() + "] in period [year: " + mwDisciplinaIleec.getAnoCurricular().toString() + " semester: " + mwDisciplinaIleec.getSemestre().toString() + "]!");
						return null;
					}
				}
			}
		} else
		{
			semester = mwDisciplinaIleec.getSemestre();
		}

		result = persistentCurricularCourseScope.readByCurricularCourseAndYearAndSemester(newCurricularCourse, year, semester);
		if ((result == null) || (result.isEmpty()))
		{
			System.out.println("[ERROR 408] Cannot find Fenix CurricularCourseScope for CurricularCourse with code [" + newCurricularCourse.getCode() + "] and name [" + newCurricularCourse.getName() + "] in period [year: " + mwDisciplinaIleec.getAnoCurricular().toString() + " semester: " + mwDisciplinaIleec.getSemestre().toString() + "]!");
			return null;
		} else
		{
			return (ICurricularCourseScope) result.get(0);
		}
	}

	/**
	 * @param enrolmentToGiveEquivalence
	 * @param equivalentEnrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeEquivalences(IEnrolment oldEnrolment, IEnrolment newEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolmentEquivalence enrolmentEquivalenceDAO = fenixPersistentSuport.getIPersistentEnrolmentEquivalence();
		IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalenceDAO = fenixPersistentSuport.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

		IEnrolmentEquivalence enrolmentEquivalence = enrolmentEquivalenceDAO.readByEnrolment(newEnrolment);
		if(enrolmentEquivalence == null)
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

		IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalence = equivalentEnrolmentForEnrolmentEquivalenceDAO.readByEnrolmentEquivalenceAndEquivalentEnrolment(enrolmentEquivalence, oldEnrolment);
		if(equivalentEnrolmentForEnrolmentEquivalence == null)
		{
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(oldEnrolment);
			equivalentEnrolmentForEnrolmentEquivalence = (IEquivalentEnrolmentForEnrolmentEquivalence) MakeEquivalencesForILEECStudents.equivalentEnrolmentForEnrolmentEquivalencesCreated.get(key);
			if(equivalentEnrolmentForEnrolmentEquivalence == null)
			{
				equivalentEnrolmentForEnrolmentEquivalence = new EquivalentEnrolmentForEnrolmentEquivalence();
				equivalentEnrolmentForEnrolmentEquivalenceDAO.simpleLockWrite(equivalentEnrolmentForEnrolmentEquivalence);
				equivalentEnrolmentForEnrolmentEquivalence.setEnrolmentEquivalence(enrolmentEquivalence);
				equivalentEnrolmentForEnrolmentEquivalence.setEquivalentEnrolment(oldEnrolment);
				MakeEquivalencesForILEECStudents.equivalentEnrolmentForEnrolmentEquivalencesCreated.put(key, equivalentEnrolmentForEnrolmentEquivalence);
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

		IPersistentMWEquivalenciasIleec mwEquivalenciasIleecDAO = mws.getIPersistentMWEquivalenciasIleec();

		ICurricularCourse oldCurricularCourse = oldEnrolment.getCurricularCourseScope().getCurricularCourse();
		IStudent student = oldEnrolment.getStudentCurricularPlan().getStudent();
		Integer equivalenceType = new Integer(2);

		MWEquivalenciaIleec mwEquivalenciaIleec = mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(equivalenceType, oldCurricularCourse.getCode());
		if(mwEquivalenciaIleec == null)
		{
			equivalenceType = new Integer(5);
			mwEquivalenciaIleec = mwEquivalenciasIleecDAO.readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(equivalenceType, oldCurricularCourse.getCode());
		}

		if(mwEquivalenciaIleec == null)
		{
			String key = "Código: [" + oldCurricularCourse.getCode() + "] e Nome: [" + oldCurricularCourse.getName() + "]";

			List cases = (List) MakeEquivalencesForILEECStudents.noEquivalences.get(key);
			if (cases == null)
			{
				cases = new ArrayList();
				cases.add(student.getNumber().toString());
			} else
			{
				if (!cases.contains(student.getNumber().toString())) {
					cases.add(student.getNumber().toString());
				}
			}
			MakeEquivalencesForILEECStudents.noEquivalences.put(key, cases);
		} else
		{
			String key = "Código: [" + oldCurricularCourse.getCode() + "] e Nome: [" + oldCurricularCourse.getName() + "]";
			
			List cases = null;
			
			if(equivalenceType.intValue() == 2)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType2.get(key);
			} else if(equivalenceType.intValue() == 5)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType5.get(key);
			}

			if (cases == null)
			{
				cases = new ArrayList();
				cases.add(student.getNumber().toString());
			} else
			{
				if (!cases.contains(student.getNumber().toString())) {
					cases.add(student.getNumber().toString());
				}
			}
			
			if(equivalenceType.intValue() == 2)
			{
				cases = (List) MakeEquivalencesForILEECStudents.creditEquivalencesType2.put(key, cases);
			} else if(equivalenceType.intValue() == 5)
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
		if(!MakeEquivalencesForILEECStudents.noEquivalences.entrySet().isEmpty()) {
			int total = 0;
			out.println("\nDISCIPLINAS SEM EQUIVALÊNCIA");
			out.println("\nOS CASOS SÃO:");
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
			out.println("TOTAL DE CASOS: " + total);
			out.println("\nAS DISCIPLINAS SÃO:");
			iterator = MakeEquivalencesForILEECStudents.noEquivalences.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				out.println("\t" + key);
			}
			out.println("TOTAL DE DISCIPLINAS: " + MakeEquivalencesForILEECStudents.noEquivalences.size());
		}

		if(!MakeEquivalencesForILEECStudents.creditEquivalencesType2.entrySet().isEmpty()) {
			int total = 0;
			out.println("\nDISCIPLINAS COM EQUIVALÊNCIAS DO TIPO \"CRÉDITOS EM QUALQUER ÁREA SECUNDÁRIA\"");
			out.println("\nOS CASOS SÃO:");
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
			out.println("TOTAL DE CASOS: " + total);
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

		if(!MakeEquivalencesForILEECStudents.creditEquivalencesType5.entrySet().isEmpty()) {
			int total = 0;
			out.println("\nDISCIPLINAS COM EQUIVALÊNCIAS DO TIPO \"CRÉDITOS EM ÁREA CIENTÍFICA ESPECIFICA\"");
			out.println("\nOS CASOS SÃO:");
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
			out.println("TOTAL DE CASOS: " + total);
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
	 * @return
	 * @throws Throwable
	 */
	private static boolean alreadyHasEquivalence(IEnrolment enrolment, IStudentCurricularPlan currentStudentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		boolean result = false;

		ICurricularCourse curricularCourse = MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseFromCurrentDegreeCurricularPlan(enrolment.getCurricularCourseScope().getCurricularCourse(), currentStudentCurricularPlan.getDegreeCurricularPlan(), fenixPersistentSuport);
		if(curricularCourse == null)
		{
			result = false;
		} else
		{
			result = true;
		}
		return result;
	}
}