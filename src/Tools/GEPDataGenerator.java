package Tools;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.ICostCenter;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEmployee;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author David Santos in Mar 26, 2004
 */

public class GEPDataGenerator
{
	private static PrintWriter out = null;
	private static PrintWriter error = null;
	private static ISuportePersistente persistentSuport = null;

	public static void main(String args[])
	{
		try
		{
			error = new PrintWriter(System.out, true);
			out = new PrintWriter(System.out, true);
			if (args[0] != null)
			{
				FileOutputStream file = new FileOutputStream(args[0]);
				out = new PrintWriter(file);
			}

			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();

			IPersistentExecutionYear executionYearDAO = persistentSuport.getIPersistentExecutionYear();
			IExecutionYear currentExecutionYear = executionYearDAO.readCurrentExecutionYear();

			ICursoExecucaoPersistente executionDegreeDAO = persistentSuport.getICursoExecucaoPersistente();
			List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(currentExecutionYear,
				TipoCurso.LICENCIATURA_OBJ);
			
			Iterator executionDegreesIterator = executionDegrees.iterator();
			while (executionDegreesIterator.hasNext())
			{
				ICursoExecucao executionDegree = (ICursoExecucao) executionDegreesIterator.next();
				IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();
				List curricularCourses = degreeCurricularPlan.getCurricularCourses();
				Iterator curricularCoursesIterator = curricularCourses.iterator();
				while (curricularCoursesIterator.hasNext())
				{
					ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesIterator.next();
					IPersistentExecutionPeriod executionPeriodDAO = persistentSuport.getIPersistentExecutionPeriod();
					List executionPeriods = executionPeriodDAO.readByExecutionYear(currentExecutionYear);
					
					Iterator executionPeriodsIterator = executionPeriods.iterator();
					while (executionPeriodsIterator.hasNext())
					{
						IExecutionPeriod executionPeriod = (IExecutionPeriod) executionPeriodsIterator.next();
						IPersistentEnrolment enrollmentDAO = persistentSuport.getIPersistentEnrolment();

						List enrollmentsInCurricularCourse = enrollmentDAO.readByCurricularCourseAndExecutionPeriod(
							curricularCourse, executionPeriod);

						List curricularCourseStatistics = getCurricularCourseStatistics(enrollmentsInCurricularCourse);

						List teachersResponsibleForCurricularCourse = getTeachersResponsibleForCurricularCourse(curricularCourse);

						String degreeName = getDegreeName(degreeCurricularPlan);
						String degreeCode = getDegreeCode(degreeCurricularPlan.getDegree());
						String curricularCourseName = getCurricularCourseName(curricularCourse);
						String curricularCourseCode = getCurricularCourseCode(curricularCourse);
						String semester = getSemester(executionPeriod);
						String numberOfEnrolledStudents = getNumberOfEnrolledStudents(curricularCourseStatistics);
						String numberOfEnrolledStudentsForFirstTime = getNumberOfEnrolledStudentsForFirstTime(
							curricularCourseStatistics);
						String numberOfEnrolledStudentsForSecondTime = getNumberOfEnrolledStudentsForSecondTime(
							curricularCourseStatistics);
						String numberOfEvaluatedStudents = getNumberOfEvaluatedStudents(curricularCourseStatistics);
						String numberOfApprovedStudents = getNumberOfApprovedStudents(curricularCourseStatistics);

						writeToFile(degreeName, degreeCode, curricularCourseName, curricularCourseCode, semester,
							numberOfEnrolledStudents, numberOfEnrolledStudentsForFirstTime, numberOfEnrolledStudentsForSecondTime,
							numberOfEvaluatedStudents, numberOfApprovedStudents, teachersResponsibleForCurricularCourse);
					}
				}
			}
			
			persistentSuport.confirmarTransaccao();
		} catch (Throwable e1)
		{
			try
			{
				persistentSuport.cancelarTransaccao();
			} catch (Throwable e2)
			{
				e2.printStackTrace(error);
			}
			e1.printStackTrace(error);
		}

		out.close();
		error.close();
	}

	/**
	 * @param curricularCourse
	 * @return List
	 * @throws Throwable
	 */
	private static List getTeachersResponsibleForCurricularCourse(ICurricularCourse curricularCourse) throws Throwable
	{
		List executionCourses = curricularCourse.getAssociatedExecutionCourses();
		List teachers = new ArrayList();

		Iterator executionCoursesIterator = executionCourses.iterator();
		while (executionCoursesIterator.hasNext())
		{
			IExecutionCourse executionCourse = (IExecutionCourse) executionCoursesIterator.next();

			IPersistentResponsibleFor responsibleForDAO = persistentSuport.getIPersistentResponsibleFor();
			
			List responsibles = responsibleForDAO.readByExecutionCourse(executionCourse);
			
			Iterator responsiblesIterator = responsibles.iterator();
			while (responsiblesIterator.hasNext())
			{
				IResponsibleFor responsibleFor = (IResponsibleFor) responsiblesIterator.next();
				teachers.add(responsibleFor.getTeacher());
			}
		}
		
		return teachers;
	}

	/**
	 * @param degreeName
	 * @param degreeCode
	 * @param curricularCourseName
	 * @param curricularCourseCode
	 * @param semester
	 * @param numberOfEnrolledStudents
	 * @param numberOfEnrolledStudentsForFirstTime
	 * @param numberOfEnrolledStudentsForSecondTime
	 * @param numberOfEvaluatedStudents
	 * @param numberOfApprovedStudents
	 * @param teachersResponsibleForCurricularCourse
	 * @throws Throwable
	 */
	private static void writeToFile(String degreeName, String degreeCode, String curricularCourseName, String curricularCourseCode,
		String semester, String numberOfEnrolledStudents, String numberOfEnrolledStudentsForFirstTime,
		String numberOfEnrolledStudentsForSecondTime, String numberOfEvaluatedStudents, String numberOfApprovedStudents,
		List teachersResponsibleForCurricularCourse) throws Throwable
	{
		StringBuffer fileLine = new StringBuffer();

		fileLine.append(degreeCode).append("\t").append(degreeName).append("\t").append(curricularCourseCode).append("\t").append(
			curricularCourseName).append("\t").append(semester).append("\t").append(numberOfEnrolledStudents).append("\t").append(
			numberOfEnrolledStudentsForFirstTime).append("\t").append(numberOfEnrolledStudentsForSecondTime).append("\t").append(
			numberOfEvaluatedStudents).append("\t").append(numberOfApprovedStudents);

		if (teachersResponsibleForCurricularCourse != null && !teachersResponsibleForCurricularCourse.isEmpty())
		{
			Iterator teachersIterator = teachersResponsibleForCurricularCourse.iterator();
			while (teachersIterator.hasNext())
			{
				ITeacher teacher = (ITeacher) teachersIterator.next();
				String number = teacher.getTeacherNumber().toString();
				String departmentName = getTeacherDepartmentName(teacher);
				fileLine.append("\t").append(number).append("\t").append(departmentName);
			}
		} else
		{
			fileLine.append("\t").append("null").append("\t").append("null");
		}

		out.println(fileLine);
	}

	/**
	 * @param teacher
	 * @return String
	 * @throws Throwable
	 */
	private static String getTeacherDepartmentName(ITeacher teacher) throws Throwable
	{
		String departamentName = null;

		if (teacher != null)
		{
			IPersistentEmployee employeeDAO = persistentSuport.getIPersistentEmployee();		
			IEmployee employee = employeeDAO.readByNumber(teacher.getTeacherNumber());
			employee.fillEmployeeHistoric();
			ICostCenter costCenter = employee.getEmployeeHistoric().getWorkingPlaceCostCenter();
			departamentName = costCenter.getDepartament();
		}
		
		return departamentName;
	}

	/**
	 * @param curricularCourseStatistics
	 * @return String
	 */
	private static String getNumberOfApprovedStudents(List curricularCourseStatistics)
	{
		return ((Integer) curricularCourseStatistics.get(4)).toString();
	}

	/**
	 * @param curricularCourseStatistics
	 * @return String
	 */
	private static String getNumberOfEvaluatedStudents(List curricularCourseStatistics)
	{
		return ((Integer) curricularCourseStatistics.get(3)).toString();
	}

	/**
	 * @param curricularCourseStatistics
	 * @return String
	 */
	private static String getNumberOfEnrolledStudentsForSecondTime(List curricularCourseStatistics)
	{
		return ((Integer) curricularCourseStatistics.get(2)).toString();
	}

	/**
	 * @param curricularCourseStatistics
	 * @return String
	 */
	private static String getNumberOfEnrolledStudentsForFirstTime(List curricularCourseStatistics)
	{
		return ((Integer) curricularCourseStatistics.get(1)).toString();
	}

	/**
	 * @param curricularCourseStatistics
	 * @return String
	 */
	private static String getNumberOfEnrolledStudents(List curricularCourseStatistics)
	{
		return ((Integer) curricularCourseStatistics.get(0)).toString();
	}

	/**
	 * @param enrollmentsInCurricularCourse
	 * @return List
	 * @throws Throwable
	 */
	private static List getCurricularCourseStatistics(List enrollmentsInCurricularCourse) throws Throwable
	{
		Map stats = new HashMap();
		int numberOfEnrolledStudents = 0;
		int numberOfApprovedStudents = 0;
		int numberOfEvaluatedStudents = 0;
		int numberOfEnrolledStudentsForFirstTime = 0;
		int numberOfEnrolledStudentsForSecondTime = 0;
		
		Iterator enrollmentsIterator = enrollmentsInCurricularCourse.iterator();
		while (enrollmentsIterator.hasNext())
		{
			IEnrolment enrollment = (IEnrolment) enrollmentsIterator.next();
			
			addToHashMap(stats, "numberOfEnrolledStudents", enrollment.getStudentCurricularPlan().getStudent().getNumber(), false);

			if (enrollment.getEnrolmentState().equals(EnrolmentState.APROVED))
			{
				addToHashMap(stats, "numberOfApprovedStudents", enrollment.getStudentCurricularPlan().getStudent().getNumber(),
					false);
				addToHashMap(stats, "numberOfEvaluatedStudents", enrollment.getStudentCurricularPlan().getStudent().getNumber(),
					false);
			}

			if (enrollment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED))
			{
				addToHashMap(stats, "numberOfEvaluatedStudents", enrollment.getStudentCurricularPlan().getStudent().getNumber(),
					false);
			}

			addToHashMap(stats, enrollment.getStudentCurricularPlan().getStudent().getNumber().toString(), enrollment
				.getCurricularCourse().getCode(), true);
		}

		if (stats.get("numberOfEnrolledStudents") != null)
		{
			numberOfEnrolledStudents = ((List) stats.get("numberOfEnrolledStudents")).size();
		}
		if (stats.get("numberOfApprovedStudents") != null)
		{
			numberOfApprovedStudents = ((List) stats.get("numberOfApprovedStudents")).size();
		}
		if (stats.get("numberOfEvaluatedStudents") != null)
		{
			numberOfEvaluatedStudents = ((List) stats.get("numberOfEvaluatedStudents")).size();
		}

		if (stats.entrySet() != null)
		{
			Iterator statsIterator = stats.entrySet().iterator();
			while (statsIterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) statsIterator.next();
				String key = (String) mapEntry.getKey();
	
				if (!key.equals("numberOfEnrolledStudents") && !key.equals("numberOfApprovedStudents")
					&& !key.equals("numberOfEvaluatedStudents"))
				{
					List values = (List) mapEntry.getValue();
					if (values.size() == 1)
					{
						numberOfEnrolledStudentsForFirstTime++;
					} else if (values.size() == 2)
					{
						numberOfEnrolledStudentsForSecondTime++;
					}
				}
			}
		}

		List result = new ArrayList();
		result.add(0, new Integer(numberOfEnrolledStudents));
		result.add(1, new Integer(numberOfEnrolledStudentsForFirstTime));
		result.add(2, new Integer(numberOfEnrolledStudentsForSecondTime));
		result.add(3, new Integer(numberOfEvaluatedStudents));
		result.add(4, new Integer(numberOfApprovedStudents));

		return result;
	}

	/**
	 * @param curricularCourse
	 * @return String
	 */
	private static String getCurricularCourseCode(ICurricularCourse curricularCourse)
	{
		return curricularCourse.getCode();
	}

	/**
	 * @param curricularCourse
	 * @return String
	 */
	private static String getCurricularCourseName(ICurricularCourse curricularCourse)
	{
		return curricularCourse.getName();
	}

	/**
	 * @param degreeCurricularPlan
	 * @return String
	 */
	private static String getDegreeName(IDegreeCurricularPlan degreeCurricularPlan)
	{
		return degreeCurricularPlan.getDegree().getNome();
	}

	/**
	 * @param executionPeriod
	 * @return String
	 */
	private static String getSemester(IExecutionPeriod executionPeriod)
	{
		return executionPeriod.getSemester().toString();
	}

	/**
	 * @param degree
	 * @return String
	 * @throws Throwable
	 */
	private static String getDegreeCode(ICurso degree) throws Throwable
	{
		IPersistentMiddlewareSupport persistentMiddlewareSupport = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation mwDegreeTranslationDAO = persistentMiddlewareSupport.getIPersistentMWDegreeTranslation();
		MWDegreeTranslation mwDegreeTranslation = mwDegreeTranslationDAO.readByDegree(degree);

		if (mwDegreeTranslation != null)
		{
			return mwDegreeTranslation.getDegreeCode().toString();
		} else
		{
			return null;
		}
	}

	/**
	 * @param map
	 * @param key
	 * @param value
	 * @param withDuplicates
	 */
	private static void addToHashMap(Map map, Object key, Object value, boolean withDuplicates)
	{
		if (!map.containsKey(key))
		{
			List values = new ArrayList();
			values.add(value);
			map.put(key, values);
		} else
		{
			List values = (List) map.get(key);
			if (!withDuplicates && !values.contains(value))
			{
				values.add(value);
				map.put(key, values);
			} else if (withDuplicates)
			{
				values.add(value);
				map.put(key, values);
			}
		}
	}

}