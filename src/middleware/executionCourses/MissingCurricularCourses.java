/*
 * Created on Nov 29, 2003
 *  
 */
package middleware.executionCourses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.DomainObject;
import Dominio.ExecutionCourse;
import Dominio.ExecutionPeriod;
import Dominio.Site;

/**
 * @author Luis Cruz
 *  
 */
public class MissingCurricularCourses
{

	protected static PersistenceBroker broker = null;

	static {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}

	public static void main(String[] args)
	{
		Calendar startTime;
		Calendar endTime;

		Criteria criteriaCurricularCourses = new Criteria();
		criteriaCurricularCourses.addLike("degreeCurricularPlan.name", "L%");
		criteriaCurricularCourses.addGreaterThan("degreeCurricularPlanKey", new Integer(23));
		criteriaCurricularCourses.addEqualTo("scopes.curricularSemester.semester", new Integer(2));
		Query queryCurricularCourses =
			new QueryByCriteria(CurricularCourse.class, criteriaCurricularCourses);

		broker.beginTransaction();
		int numberCurricularCourses = broker.getCount(queryCurricularCourses);
		int numberCurricularCoursesAssociatedToExecutionCourses = 0;
		int numberCurricularCoursesNotAssociatedToExecutionCourses = 0;
		System.out.println("Number of curricular courses to process = " + numberCurricularCourses);

		Criteria criteriaExecutionPeriod = new Criteria();
		criteriaExecutionPeriod.addEqualTo("semester", new Integer(2));
		criteriaExecutionPeriod.addEqualTo("executionYear.year", "2003/2004");
		Query queryExecutionPeriod = new QueryByCriteria(ExecutionPeriod.class, criteriaExecutionPeriod);
		ExecutionPeriod executionPeriod =
			(ExecutionPeriod) broker.getObjectByQuery(queryExecutionPeriod);
		broker.commitTransaction();

		startTime = Calendar.getInstance();
		for (int i = 0; i < numberCurricularCourses; i++)
		{
			broker.beginTransaction();
			CurricularCourse curricularCourse =
				(CurricularCourse) readSpan(queryCurricularCourses, new Integer(i + 1));
			if (curricularCourse.getAssociatedExecutionCourses() == null
				|| curricularCourse.getAssociatedExecutionCourses().isEmpty())
			{
				numberCurricularCoursesNotAssociatedToExecutionCourses++;
				ExecutionCourse executionCourse =
					createExecutionCourse(executionPeriod, curricularCourse);
				createSite(executionCourse);
			}
			else
			{
				numberCurricularCoursesAssociatedToExecutionCourses++;
			}
			broker.commitTransaction();
		}
		endTime = Calendar.getInstance();
		broker.close();

		System.out.println(
			"Number of curricular courses associated to execution courses = "
				+ numberCurricularCoursesAssociatedToExecutionCourses);
		System.out.println(
			"Number of curricular courses not associated to execution courses = "
				+ numberCurricularCoursesNotAssociatedToExecutionCourses);
		long executionTime = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		System.out.println("Process took: " + executionTime + " seconds");
	}

	/**
	 * @param executionCourse
	 */
	private static void createSite(ExecutionCourse executionCourse)
	{
		Site site = new Site();
		site.setAlternativeSite(null);
		site.setExecutionCourse(executionCourse);
		//site.setIdInternal();
		site.setInitialStatement(null);
		site.setIntroduction(null);
		site.setKeyExecutionCourse(null);
		site.setMail(null);
		site.setStyle(null);
		broker.store(site);
	}

	protected static DomainObject readSpan(Query query, Integer elementNumber)
	{
		query.setStartAtIndex(elementNumber.intValue());
		query.setEndAtIndex(elementNumber.intValue() + 1);
		return (DomainObject) ((List) broker.getCollectionByQuery(query)).get(0);
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private static ExecutionCourse createExecutionCourse(
		ExecutionPeriod executionPeriod,
		CurricularCourse curricularCourse)
	{
		CurricularCourseScope scope = (CurricularCourseScope) curricularCourse.getScopes().get(0);
		
		ExecutionCourse executionCourse = new ExecutionCourse();
		executionCourse.setAssociatedCurricularCourses(makeListWihtElement(curricularCourse));
		executionCourse.setAssociatedEvaluations(new ArrayList());
		executionCourse.setAssociatedExams(new ArrayList());
		executionCourse.setAttendingStudents(new ArrayList());
		executionCourse.setComment("");
		executionCourse.setExecutionPeriod(executionPeriod);
		//executionCourse.setIdInternal();
		//executionCourse.setKeyExecutionPeriod();
		executionCourse.setLabHours(scope.getLabHours());
		executionCourse.setNome(curricularCourse.getName());
		executionCourse.setPraticalHours(scope.getPraticalHours());
		executionCourse.setSigla(makeCode(executionPeriod, curricularCourse.getName()));
		executionCourse.setTheoPratHours(scope.getTheoPratHours());
		executionCourse.setTheoreticalHours(scope.getTheoreticalHours());
		broker.store(executionCourse);
		return executionCourse;
	}

	/**
	 * @param string
	 * @return
	 */
	private static String makeCode(ExecutionPeriod executionPeriod, String executionCourseName)
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
	private static ExecutionCourse readExecutionCourse(ExecutionPeriod executionPeriod, String executionCourseCode)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", executionCourseCode);
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
		Query query = new QueryByCriteria(ExecutionCourse.class, criteria);
		return (ExecutionCourse) broker.getObjectByQuery(query);
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private static List makeListWihtElement(CurricularCourse curricularCourse)
	{
		List result = new ArrayList();
		result.add(curricularCourse);
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
