/*
 * Created on Dec 01, 2003
 *  
 */
package middleware.executionCourses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Aula;
import Dominio.DomainObject;
import Dominio.ExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.Turno;

/**
 * @author Luis Cruz
 *  
 */
public class EliminateRepeatedExecutionCourses
{

	protected static PersistenceBroker broker = null;
	protected static Map mapExecutionCourses = null;
	protected static Map mapExecutionCoursesDuplicates = null;
	protected static Map mapExecutionCoursesInconsistent = null;

	static {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		mapExecutionCourses = new HashMap(2438);
		mapExecutionCoursesDuplicates = new HashMap(628);
		mapExecutionCoursesInconsistent = new HashMap(628);
	}

	public static void main(String[] args)
	{
		Calendar startTime;
		Calendar endTime;

		Query queryAllExecutionCourses = new QueryByCriteria(ExecutionCourse.class, null);
		Query queryAllShifts = new QueryByCriteria(Turno.class, null);
		Query queryAllLessons = new QueryByCriteria(Aula.class, null);

		broker.beginTransaction();
		int numberExecutionCourses = broker.getCount(queryAllExecutionCourses);
		int numberShifts = broker.getCount(queryAllShifts);
		int numberLessons = broker.getCount(queryAllLessons);
		System.out.println("Number of execution courses = " + numberExecutionCourses);
		System.out.println("Number of shifts = " + numberShifts);
		System.out.println("Number of lessons = " + numberLessons);
		broker.commitTransaction();

		startTime = Calendar.getInstance();
		for (int i = 0; i < numberExecutionCourses; i++)
		{
			broker.beginTransaction();
			ExecutionCourse executionCourse =
				(ExecutionCourse) readSpan(queryAllExecutionCourses, new Integer(i + 1));
			String executionCourseKey =
				executionCourse.getKeyExecutionPeriod().toString() + executionCourse.getSigla();
			//System.out.println("executionCourseKey=" + executionCourseKey);
			if (mapExecutionCourses.containsKey(executionCourseKey))
			{
				ExecutionCourse conflictingExecutionCourse =
					readExecutionCourse((Integer) mapExecutionCourses.get(executionCourseKey));
				if (executionCourse.getNome().equalsIgnoreCase(conflictingExecutionCourse.getNome())
					&& executionCourse.getAssociatedCurricularCourses().containsAll(
						conflictingExecutionCourse.getAssociatedCurricularCourses())
					&& conflictingExecutionCourse.getAssociatedCurricularCourses().containsAll(
						executionCourse.getAssociatedCurricularCourses()))
				{
					if (mapExecutionCoursesDuplicates.get(executionCourseKey) != null)
					{
						((List) mapExecutionCoursesDuplicates.get(executionCourseKey)).add(
							executionCourse.getIdInternal());
					}
					else
					{
						mapExecutionCoursesDuplicates.put(
							executionCourseKey,
							makeListWihtElement(executionCourse.getIdInternal()));
					}
				}
				else
				{
					if (mapExecutionCoursesInconsistent.get(executionCourseKey) != null)
					{
						((List) mapExecutionCoursesInconsistent.get(executionCourseKey)).add(
							executionCourse.getIdInternal());
					}
					else
					{
						mapExecutionCoursesInconsistent.put(
							executionCourseKey,
							makeListWihtElement(executionCourse.getIdInternal()));
					}
				}
			}
			else
			{
				mapExecutionCourses.put(executionCourseKey, executionCourse.getIdInternal());
			}
			broker.commitTransaction();
		}
		endTime = Calendar.getInstance();
		broker.close();

		System.out.println("Number of distict execution courses = " + mapExecutionCourses.size());
		System.out.println(
			"Number of duplicate execution courses = " + mapExecutionCoursesDuplicates.size());
		System.out.println(
			"Number of inconsistent execution courses = " + mapExecutionCoursesInconsistent.size());
		long executionTime = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		System.out.println("Process took: " + executionTime + " seconds");

		System.out.println("Fixing Inconsistent Execution Courses");
		startTime = Calendar.getInstance();
		fixInconsistentExecutionCourses();
		endTime = Calendar.getInstance();
		executionTime = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		System.out.println("Process took: " + executionTime + " seconds");

		System.out.println("Fixing Duplicate Execution Courses");
		startTime = Calendar.getInstance();
		fixDuplicateExecutionCourses();
		endTime = Calendar.getInstance();
		executionTime = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		System.out.println("Process took: " + executionTime + " seconds");

		broker.beginTransaction();
		numberExecutionCourses = broker.getCount(queryAllExecutionCourses);
		numberShifts = broker.getCount(queryAllShifts);
		numberLessons = broker.getCount(queryAllLessons);
		broker.commitTransaction();
		System.out.println("Number of execution courses = " + numberExecutionCourses);
		System.out.println("Number of shifts = " + numberShifts);
		System.out.println("Number of lessons = " + numberLessons);
	}

	/**
	 *  
	 */
	private static void fixDuplicateExecutionCourses()
	{
		broker.beginTransaction();
		Iterator iterator = mapExecutionCoursesDuplicates.values().iterator();
		while (iterator.hasNext())
		{
			List executionCoursesToFix = (List) iterator.next();
			String executionCourseKey =
				readExecutionCourse((Integer) executionCoursesToFix.get(0)).getKeyExecutionPeriod().toString()
					+ readExecutionCourse((Integer) executionCoursesToFix.get(0)).getSigla();
			ExecutionCourse executionCourse =
				readExecutionCourse((Integer) mapExecutionCourses.get(executionCourseKey));
			for (int i = 0; i < executionCoursesToFix.size(); i++)
			{
				ExecutionCourse executionCourseToDelete = readExecutionCourse((Integer) executionCoursesToFix.get(i));
				List shifts = readShifts(executionCourseToDelete);
				for (int j = 0; j < shifts.size(); j++)
				{
					Turno shift = (Turno) shifts.get(j);
					shift.setDisciplinaExecucao(executionCourse);
					broker.store(shift);
					List lessons = shift.getAssociatedLessons();
					for (int k = 0; k < lessons.size(); k++)
					{
						Aula lesson = (Aula) lessons.get(k);
						lesson.setDisciplinaExecucao(executionCourse);
						broker.store(lesson);
					}
				}
				broker.delete(executionCourseToDelete);
			}
		}
		broker.commitTransaction();
	}

	/**
	 * @param executionCourseToDelete
	 * @return
	 */
	private static List readShifts(ExecutionCourse executionCourse)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("chaveDisciplinaExecucao", executionCourse.getIdInternal());
		Query query = new QueryByCriteria(Turno.class, criteria);
		return (List) broker.getCollectionByQuery(query);
	}

	/**
	 *  
	 */
	private static void fixInconsistentExecutionCourses()
	{
		broker.beginTransaction();
		Iterator iterator = mapExecutionCoursesInconsistent.values().iterator();
		while (iterator.hasNext())
		{
			List executionCoursesToFix = (List) iterator.next();
			for (int i = 0; i < executionCoursesToFix.size(); i++)
			{
				ExecutionCourse executionCourseToFix =
					readExecutionCourse((Integer) executionCoursesToFix.get(i));
				executionCourseToFix.setSigla(
					makeCode(
						executionCourseToFix.getExecutionPeriod(),
						executionCourseToFix.getNome()));
				broker.store(executionCourseToFix);
			}
		}
		broker.commitTransaction();
	}

	protected static DomainObject readSpan(Query query, Integer elementNumber)
	{
		query.setStartAtIndex(elementNumber.intValue());
		query.setEndAtIndex(elementNumber.intValue() + 1);
		return (DomainObject) ((List) broker.getCollectionByQuery(query)).get(0);
	}

	/**
	 * @param string
	 * @return
	 */
	private static String makeCode(IExecutionPeriod executionPeriod, String executionCourseName)
	{
		String executionCourseCode = generateCode(executionCourseName);
		System.out.println("Generated code = " + executionCourseCode);
		String attempt = executionCourseCode;
		int i = 0;
		while (readExecutionCourse(executionPeriod, attempt) != null)
		{
			attempt = executionCourseCode + "-" + ++i;
		}
		System.out.println("Available code = " + attempt);
		return attempt;
	}

	/**
	 * @param executionPeriod
	 * @param executionCourseCode
	 * @return
	 */
	private static ExecutionCourse readExecutionCourse(
		IExecutionPeriod executionPeriod,
		String executionCourseCode)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", executionCourseCode);
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
		Query query = new QueryByCriteria(ExecutionCourse.class, criteria);
		return (ExecutionCourse) broker.getObjectByQuery(query);
	}

	private static ExecutionCourse readExecutionCourse(Integer executionPeriodOID)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", executionPeriodOID);
		Query query = new QueryByCriteria(ExecutionCourse.class, criteria);
		return (ExecutionCourse) broker.getObjectByQuery(query);
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private static List makeListWihtElement(Object object)
	{
		List result = new ArrayList();
		result.add(object);
		return result;
	}

	private static String generateCode(String nomeDisciplina)
	{
		String caracterEspaco = " ";
		String sigla = new String();
		String token;
		nomeDisciplina = nomeDisciplina.trim();
		if (isOneWord(nomeDisciplina))
		{
			sigla = nomeDisciplina.substring(0, 3);
		}
		else
		{
			StringTokenizer stringTokenizer = new StringTokenizer(nomeDisciplina, caracterEspaco);
			while (stringTokenizer.hasMoreTokens())
			{
				token = stringTokenizer.nextToken();
				token = token.toUpperCase();
				if (isRomanNumber(token))
				{
					sigla = sigla.concat(token);
				}
				else if (isTokenValid(token))
				{
					sigla = sigla.concat(String.valueOf(token.charAt(0)));
				}
			}
		}
		return sigla;
	}

	private static boolean isTokenValid(String token)
	{
		return !token.equals("E")
			&& !token.equals("A")
			&& !token.equals("O")
			&& !token.equals("DE")
			&& !token.equals("DA")
			&& !token.equals("DO")
			&& !token.equals("EM")
			&& !token.equals("DAS")
			&& !token.equals("DOS")
			&& !token.equals("PELO")
			&& !token.equals("PELA")
			&& !token.equals("AO")
			&& !token.equals("AOS")
			&& !token.equals("-")
			&& token.charAt(0) != '(';
	}

	private static boolean isRomanNumber(String token)
	{
		return token.equals("I")
			|| token.equals("II")
			|| token.equals("III")
			|| token.equals("IV")
			|| token.equals("V");
	}

	private static boolean isOneWord(String str)
	{
		return str.indexOf(" ") < 0;
	}

}
