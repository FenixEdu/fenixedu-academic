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
	private static HashMap curricularCoursesFoundInOtherDegree = new HashMap();
	private static HashMap curricularCoursesFoundInOtherDegreeCardinality = new HashMap();

	private static HashMap notFoundExecutionCourse = new HashMap();

	private static HashMap notFoundCurricularCourseScopes = new HashMap();
	
	private static HashMap notFoundCurricularCourses = new HashMap();











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
	public static void addFoundCurricularCourseInOtherDegrees(String courseCode, String degreeCode, List foundHere)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);
		Integer times = new Integer(1);
		if (!curricularCoursesFoundInOtherDegree.containsKey(key.toString()))
		{
			curricularCoursesFoundInOtherDegree.put(key.toString(), foundHere);
		} else
		{
			Integer timesBefore = (Integer) curricularCoursesFoundInOtherDegreeCardinality.get(key.toString());
			times = new Integer(timesBefore.intValue() + 1);
		}
		curricularCoursesFoundInOtherDegreeCardinality.put(key.toString(), times);
	}

//	public static void addFoundCurricularCourseInOtherDegrees(String courseCode, String degreeCode, String studentNumber)
//	{
//		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);
//		Integer times = new Integer(1);
//
//		if (!curricularCoursesFoundInOtherDegree.containsKey(key.toString()))
//		{
//			List studentList = (List) curricularCoursesFoundInOtherDegree.get(key.toString());
//			if (studentList == null)
//			{
//				studentList = new ArrayList();
//				studentList.add(studentNumber);
//			} else
//			{
//				if (!studentList.contains(studentNumber)) {
//					studentList.add(studentNumber);
//				}
//			}
//			curricularCoursesFoundInOtherDegree.put(key.toString(), studentList);
//		} else
//		{
//			Integer timesBefore = (Integer) curricularCoursesFoundInOtherDegreeCardinality.get(key.toString());
//			times = new Integer(timesBefore.intValue() + 1);
//		}
//		curricularCoursesFoundInOtherDegreeCardinality.put(key.toString(), times);
//	}

	public static void addCurricularCourseScopeNotFound(String courseCode, String degreeCode, String studentNumber, String year, String semester, String branchCode)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode).append(" - ").append(year).append(" - ").append(semester).append(" - ").append(branchCode);

		List studentList = (List) notFoundCurricularCourseScopes.get(key.toString());
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
		
		notFoundCurricularCourseScopes.put(key.toString(), studentList);
	}


	public static void addCurricularCourseNotFound(String courseCode, String degreeCode, String studentNumber)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);

		List studentList = (List) notFoundCurricularCourses.get(key.toString());
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
		
		notFoundCurricularCourses.put(key.toString(), studentList);
	}


	public static void report(PrintWriter out)
	{
		out.println("\nCASOS DE ERRO --------------------------------------------------------------------------");
//		out.println("\tCaso 1 - Disciplinas não encontradas no curso do aluno mas encontradas em varios outros cursos");
		out.println("\tCASO 1 - Disciplinas encontradas num curso diferente do curso do aluno mas nas quais o aluno não tem Attend");
		Iterator iterator = curricularCoursesFoundInOtherDegree.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			String key = (String) mapEntry.getKey();

			out.print("\t\tCadeira - Curso = " + key);
			out.println(" Aconteceu " + curricularCoursesFoundInOtherDegreeCardinality.get(key) + " veze(s)...");
			List foundHere = (List) mapEntry.getValue();
			// TODO [DAVID] print list here

		}
		
		
		out.println("\n\tCASO 2 - Execuções não encontradas");
		Iterator iterator2 = notFoundExecutionCourse.entrySet().iterator();
		while (iterator2.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator2.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos ="+ studentList.toString());
		}
		

		out.println("\n\tCASO 3 - Scopes não encontrados");
		Iterator iterator3 = notFoundCurricularCourseScopes.entrySet().iterator();
		while (iterator3.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator3.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso - Ano - Semestre - Ramo = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos ="+ studentList.toString());
		}
		

		out.println("\n\tCASO 4 - Curriculares não encontradas");
		Iterator iterator4 = notFoundCurricularCourses.entrySet().iterator();
		while (iterator4.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator4.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos ="+ studentList.toString());
		}
		
		out.close();
	}
}