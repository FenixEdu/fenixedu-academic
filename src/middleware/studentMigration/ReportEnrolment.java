/*
 * Created on 14/Out/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author David Santos
 * 14/Out/2003
 */
public class ReportEnrolment
{
	private static HashMap notFoundCurricularCourses = new HashMap();
	private static HashMap notFoundCurricularCoursesCardinality = new HashMap();

	private static HashMap notFoundExecutionCourse = new HashMap();

	public static void addExecutionCourseNotFound(String courseCode, String degreeCode, String studentNumber)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);

		List studentList = (List) notFoundExecutionCourse.get(key.toString());
		if (studentList == null)
		{
			studentList = new ArrayList();
			studentList.add(studentNumber);
		} else
		{
			if (!studentList.contains(studentNumber)) {
				studentList.add(studentNumber);
			}
		}
		
		notFoundExecutionCourse.put(key.toString(), studentList);
	}
	/**
	 * 
	 * @param courseCode course enrolled by student
	 * @param degreeCode student degree
	 * @param foundHere list of @link Dominio.CurricularCourseScope
	 */
	public static void addNotFoundCurricularCourse(String courseCode, String degreeCode, List foundHere)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);
		Integer times = new Integer(1);
		if (!notFoundCurricularCourses.containsKey(key.toString()))
		{
			notFoundCurricularCourses.put(key.toString(), foundHere);
		} else
		{
			Integer timesBefore = (Integer) notFoundCurricularCoursesCardinality.get(key.toString());
			times = new Integer(timesBefore.intValue() + 1);
		}
		notFoundCurricularCoursesCardinality.put(key.toString(), times);
	}

	public static void report(PrintWriter out)
	{
		out.println("Enrolment cases --------------------------------------------------------------------------");
		out.println("\tCaso 1 - Disciplinas não encontradas no curso do aluno mas encontradas em varios outros cursos");
		Iterator iterator = notFoundCurricularCourses.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			String key = (String) mapEntry.getKey();

			out.print("\t\tCadeira - Curso = " + key);
			out.println(" Aconteceu " + notFoundCurricularCoursesCardinality.get(key) + " veze(s)...");
			List foundHere = (List) mapEntry.getValue();
			// TODO [DAVID] print list here

		}
		
		
		out.println("\tCaso 2 - Execuções não encontradas");
		Iterator iterator2 = notFoundExecutionCourse.entrySet().iterator();
		while (iterator2.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator2.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\tCadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos ="+ studentList.toString());
		}
		
		out.close();
	}
}
