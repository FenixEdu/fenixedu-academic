package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDisciplinaExecucao;

/**
 * @author David Santos
 * 14/Out/2003
 */
public class ReportEnrolment
{
	private static HashMap curricularCoursesFoundInOtherDegree = new HashMap();
	private static HashMap notFoundExecutionCourse = new HashMap();
	private static HashMap notFoundCurricularCourseScopes = new HashMap();
	private static HashMap notFoundCurricularCourses = new HashMap();
	private static HashMap notFoundAttends = new HashMap();

	private static HashMap replicatedCurricularCourses = new HashMap();
	private static HashMap createdAttends = new HashMap();

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 */
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
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 */
	public static void addFoundCurricularCourseInOtherDegrees(String courseCode, String degreeCode, String studentNumber)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);

		List studentList = (List) curricularCoursesFoundInOtherDegree.get(key.toString());
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
		
		curricularCoursesFoundInOtherDegree.put(key.toString(), studentList);
	}

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 * @param year
	 * @param semester
	 * @param branchCode
	 */
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

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 */
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

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 */
	public static void addAttendNotFound(String courseCode, String degreeCode, String studentNumber)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);

		List studentList = (List) notFoundAttends.get(key.toString());
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
		
		notFoundAttends.put(key.toString(), studentList);
	}

// ------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param courseCode
	 * @param curricularCourses
	 */
	public static void addReplicatedCurricularCourses(String courseCode, List curricularCourses)
	{
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(((ICurricularCourse) curricularCourses.get(0)).getName());

		List degreeList = (List) replicatedCurricularCourses.get(key.toString());
		if (degreeList == null)
		{
			degreeList = new ArrayList();
			Iterator iterator = curricularCourses.iterator();
			while(iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				degreeList.add(curricularCourse.getDegreeCurricularPlan().getDegree());
			}
		} else
		{
			Iterator iterator = curricularCourses.iterator();
			while(iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				if (!degreeList.contains(curricularCourse.getDegreeCurricularPlan().getDegree())) {
					degreeList.add(curricularCourse.getDegreeCurricularPlan().getDegree());
				}
			}
		}
		
		replicatedCurricularCourses.put(key.toString(), degreeList);
	}

	public static void addCreatedAttend(String courseCode, String degreeCode, String studentNumber, IDisciplinaExecucao executionCourse)
	{
		StringBuffer key = new StringBuffer(degreeCode).append(" - ").append(studentNumber);
		StringBuffer courseName = new StringBuffer(courseCode).append(" - ").append(executionCourse.getNome());

		List courseList = (List) createdAttends.get(key.toString());
		if (courseList == null)
		{
			courseList = new ArrayList();
			courseList.add(courseName);
		} else
		{
			if (!courseList.contains(courseName)) {
				courseList.add(courseName);
			}
		}
		
		createdAttends.put(key.toString(), courseList);
	}

//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param out
	 */
	public static void report(PrintWriter out)
	{
		out.println("\n------------------------------ CASOS DE ERRO ------------------------------");
		out.println("\tCASO 1 - DISCIPLINAS ENCONTRADAS NUM CURSO DIFERENTE DO CURSO DO ALUNO MAS NAS QUAIS O ALUNO NÃO TEM ATTEND NEM É POSSIVEL INFERIR PELOS ATENDS DOS COLEGAS (estes casos correspondem a alunos sem attend para estas disciplinas)");
		Iterator iterator = curricularCoursesFoundInOtherDegree.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos = "+ studentList.toString());

		}
		
		
		out.println("\n\tCASO 2 - EXECUÇÕES NÃO ENCONTRADAS");
		Iterator iterator2 = notFoundExecutionCourse.entrySet().iterator();
		while (iterator2.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator2.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos = "+ studentList.toString());
		}
		

		out.println("\n\tCASO 3 - SCOPES NÃO ENCONTRADOS");
		Iterator iterator3 = notFoundCurricularCourseScopes.entrySet().iterator();
		while (iterator3.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator3.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso - Ano - Semestre - Ramo = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos = "+ studentList.toString());
		}
		

		out.println("\n\tCASO 4 - CURRICULARES NÃO ENCONTRADAS");
		Iterator iterator4 = notFoundCurricularCourses.entrySet().iterator();
		while (iterator4.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator4.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos = "+ studentList.toString());
		}
		
		out.println("\n\tCASO 5 - ATTENDS NÃO ENCONTRADOS");
		Iterator iterator5 = notFoundAttends.entrySet().iterator();
		while (iterator5.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator5.next();
			String key = (String) mapEntry.getKey();
			List studentList = (List) mapEntry.getValue();
			out.print("\t\t Cadeira - Curso = " + key);
			out.println(" Aconteceu " + studentList.size() + " veze(s)...");
			out.println("\t\t\tAlunos = "+ studentList.toString());
		}
		
//		------------------------------------------------------------------------------------------------------------------------------

		out.println("\n\tDISCIPLINAS REPLICADAS:");
		Iterator iterator6 = replicatedCurricularCourses.entrySet().iterator();
		while (iterator6.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator6.next();
			String key = (String) mapEntry.getKey();
			out.println("\t\t Cadeira = " + key);
			out.print("\t\t\tCursos = ");
			List degreeList = (List) mapEntry.getValue();
			Iterator iterator7 = degreeList.iterator();
			while(iterator7.hasNext()) {
				ICurso degree = (ICurso) iterator7.next();
				out.print(degree.getNome() + "\n\t\t\t         ");
			}
			out.println("");
		}
		
		out.println("\n\tATTENDS CRIADOS:");
		Iterator iterator8 = createdAttends.entrySet().iterator();
		int total = 0;
		while (iterator8.hasNext())
		{
			Map.Entry mapEntry = (Map.Entry) iterator8.next();
			String key = (String) mapEntry.getKey();
			out.println("\t\t Curso - Aluno = " + key);
			List courseList = (List) mapEntry.getValue();
			out.println("\t\t\tDisciplinas = "+ courseList.toString());
			total = total + courseList.size();
		}
		out.println("\t\t\tTotal Attends criados: " + total);
		
		out.close();
	}
}