package middleware.studentMigration.enrollments;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 14/Out/2003
 */
public class ReportEnrolment
{
	// For errors:
	private static HashMap curricularCoursesFoundInOtherDegree = new HashMap();
	private static HashMap notFoundExecutionCourse = new HashMap();
	private static HashMap notFoundCurricularCourseScopes = new HashMap();
	private static HashMap notFoundCurricularCourses = new HashMap();
	private static HashMap notFoundAttends = new HashMap();

	// For information:
	private static HashMap replicatedCurricularCourses = new HashMap();
	private static HashMap createdAttends = new HashMap();
	private static HashMap curricularCourses = new HashMap();
	private static HashMap degrees = new HashMap();
	private static int enrolmentsMigrated = 0;
	private static int enrolmentEvaluationsMigrated = 0;
	private static int enrolmentsDeleted = 0;
	private static int enrolmentEvaluationsDeleted = 0;
	
	// For control:
	private static boolean smallReport = true;

//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 */
	public static void addExecutionCourseNotFound(String courseCode, String degreeCode, String studentNumber)
	{
		addCurricularCourseName(courseCode);
		addDegreeName(degreeCode);

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
		addCurricularCourseName(courseCode);
		addDegreeName(degreeCode);

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
		addCurricularCourseName(courseCode);
		addDegreeName(degreeCode);

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
		addCurricularCourseName(courseCode);
		addDegreeName(degreeCode);

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
		if(!smallReport) {
			addCurricularCourseName(courseCode);
			addDegreeName(degreeCode);
		}

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

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 * @param executionCourse
	 */
	public static void addCreatedAttend(String courseCode, String degreeCode, String studentNumber)
	{
		StringBuffer key = new StringBuffer(degreeCode).append(" - ").append(studentNumber);

		List courseList = (List) createdAttends.get(key.toString());
		if (courseList == null)
		{
			courseList = new ArrayList();
			courseList.add(courseCode);
		} else
		{
			if (!courseList.contains(courseCode)) {
				courseList.add(courseCode);
			}
		}
		
		createdAttends.put(key.toString(), courseList);
	}

	/**
	 * @param courseCode
	 */
	private static void addCurricularCourseName(String courseCode)
	{
		String key = courseCode;
		String courseName = findCurricularCourseName(courseCode);

		String value = (String) curricularCourses.get(key);
		if (value == null) {
			curricularCourses.put(key, courseName);
		}
	}

	/**
	 * @param degreeCode
	 */
	private static void addDegreeName(String degreeCode)
	{
		String key = degreeCode;
		String degreeName = findDegreeName(degreeCode);

		String value = (String) degrees.get(key);
		if (value == null) {
			degrees.put(key, degreeName);
		}
	}

	/**
	 * @param value
	 */
	public static void addEnrolmentMigrated(int value)
	{
		enrolmentsMigrated = enrolmentsMigrated + value;
	}

	/**
	 *
	 */
	public static void addEnrolmentDeleted()
	{
		enrolmentsDeleted++;
	}

	public static void addEnrolmentEvaluationMigrated()
	{
		enrolmentEvaluationsMigrated++;
	}

	public static void addEnrolmentMigrated()
	{
		enrolmentsMigrated++;
	}

	public static void addEnrolmentEvaluationDeleted()
	{
		enrolmentEvaluationsDeleted++;
	}

//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param out
	 */
	public static void report(PrintWriter out)
	{
		out.println("[INFO] DONE!");
		out.println("[INFO] Total Enrolments created: [" + ReportEnrolment.enrolmentsMigrated + "].");
		out.println("[INFO] Total EnrolmentEvaluations created: [" + ReportEnrolment.enrolmentEvaluationsMigrated + "].");
		out.println("[INFO] Total Enrolments deleted: [" + ReportEnrolment.enrolmentsDeleted + "]");
		out.println("[INFO] Total EnrolmentEvaluations deleted: [" + ReportEnrolment.enrolmentEvaluationsDeleted + "]");
		
		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("------------------------------ CASOS DE ERRO ------------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");

		int totalCurricularCoursesFoundInOtherDegree = 0;
		if(!curricularCoursesFoundInOtherDegree.entrySet().isEmpty()) {
			out.println("");
			out.println("CASO 1 - DISCIPLINAS ENCONTRADAS NUM CURSO DIFERENTE DO CURSO DO ALUNO SOBRE AS QUAIS NÃO É POSSIVEL INFERIR QUAL A DISCIPLINA A ESCOLHER");
			Iterator iterator1 = curricularCoursesFoundInOtherDegree.entrySet().iterator();
			while (iterator1.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator1.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCadeira - Curso = " + key);
				out.println(" Aconteceu " + studentList.size() + " veze(s)...");
				out.println("\t\tAlunos = "+ studentList.toString());
				totalCurricularCoursesFoundInOtherDegree = totalCurricularCoursesFoundInOtherDegree + studentList.size();
			}
			out.println("TOTAL: " + totalCurricularCoursesFoundInOtherDegree);
		}

		int totalExecutionCoursesNotFound = 0;
		if(!notFoundExecutionCourse.entrySet().isEmpty()) {
			out.println("");
			out.println("CASO 2 - EXECUÇÕES NÃO ENCONTRADAS");
			Iterator iterator2 = notFoundExecutionCourse.entrySet().iterator();
			while (iterator2.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator2.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCadeira - Curso = " + key);
				out.println(" Aconteceu " + studentList.size() + " veze(s)...");
				out.println("\t\tAlunos = "+ studentList.toString());
				totalExecutionCoursesNotFound = totalExecutionCoursesNotFound + studentList.size();
			}
			out.println("TOTAL: " + totalExecutionCoursesNotFound);
		}
		
		int totalCurricularCourseScopesNotFound = 0;
		if(!notFoundCurricularCourseScopes.entrySet().isEmpty()) {
			out.println("");
			out.println("CASO 3 - SCOPES NÃO ENCONTRADOS");
			Iterator iterator3 = notFoundCurricularCourseScopes.entrySet().iterator();
			while (iterator3.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator3.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCadeira - Curso - Ano - Semestre - Ramo = " + key);
				out.println(" Aconteceu " + studentList.size() + " veze(s)...");
				out.println("\t\tAlunos = "+ studentList.toString());
				totalCurricularCourseScopesNotFound = totalCurricularCourseScopesNotFound + studentList.size();
			}
			out.println("TOTAL: " + totalCurricularCourseScopesNotFound);
		}
		
		int totalCurricularCoursesNotFound = 0;
		if(!notFoundCurricularCourses.entrySet().isEmpty()) {
			out.println("");
			out.println("CASO 4 - CURRICULARES NÃO ENCONTRADAS");
			Iterator iterator4 = notFoundCurricularCourses.entrySet().iterator();
			while (iterator4.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator4.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCadeira - Curso = " + key);
				out.println(" Aconteceu " + studentList.size() + " veze(s)...");
				out.println("\t\tAlunos = "+ studentList.toString());
				totalCurricularCoursesNotFound = totalCurricularCoursesNotFound + studentList.size();
			}
			out.println("TOTAL: " + totalCurricularCoursesNotFound);
		}
		
		int totalAttendsNotFound = 0;
		if(!notFoundAttends.entrySet().isEmpty()) {
			out.println("");
			out.println("CASO 5 - ATTENDS NÃO ENCONTRADOS");
			Iterator iterator5 = notFoundAttends.entrySet().iterator();
			while (iterator5.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator5.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				if(!smallReport) {
					out.print("\tCadeira - Curso = " + key);
					out.println(" Aconteceu " + studentList.size() + " veze(s)...");
					out.println("\t\tAlunos = "+ studentList.toString());
				}
				totalAttendsNotFound = totalAttendsNotFound + studentList.size();
			}
			out.println("TOTAL: " + totalAttendsNotFound);
		}

		if(enrolmentsMigrated > 0) {
			out.println("");
			out.println("TOTAL ENROLMENTS MIGRADOS: " + enrolmentsMigrated);
		}

		int totalEnrolmentsNotMigrated = totalCurricularCoursesFoundInOtherDegree + totalExecutionCoursesNotFound + totalCurricularCourseScopesNotFound + totalCurricularCoursesNotFound;
		if(totalEnrolmentsNotMigrated > 0) {
			out.println("");
			out.println("TOTAL ENROLMENTS NÃO MIGRADOS: " + totalEnrolmentsNotMigrated);
		}

		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("-------------------------- LEGENDAS E INFORMAÇÃO --------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");
		
		if(!replicatedCurricularCourses.entrySet().isEmpty()) {
			out.println("");
			out.println("DISCIPLINAS REPLICADAS");
			Iterator iterator6 = replicatedCurricularCourses.entrySet().iterator();
			while (iterator6.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator6.next();
				String key = (String) mapEntry.getKey();
				out.println("\tCadeira = " + key);
				out.print("\t\tCursos = ");
				List degreeList = (List) mapEntry.getValue();
				Iterator iterator7 = degreeList.iterator();
				while(iterator7.hasNext()) {
					ICurso degree = (ICurso) iterator7.next();
					out.println("");
					out.print(degree.getNome() + "\t\t         ");
				}
				out.println("");
			}
		}

		if(!createdAttends.entrySet().isEmpty()) {
			out.println("");
			out.println("ATTENDS CRIADOS");
			int totalCreatedAttends = 0;
			Iterator iterator8 = createdAttends.entrySet().iterator();
			while (iterator8.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator8.next();
				String key = (String) mapEntry.getKey();
				List courseList = (List) mapEntry.getValue();
				if(!smallReport) {
					out.println("\tCurso - Aluno = " + key);
					out.println("\t\tDisciplinas = "+ courseList.toString());
				}
				totalCreatedAttends = totalCreatedAttends + courseList.size();
			}
			out.println("TOTAL: " + totalCreatedAttends);
		}
		
		if((!curricularCourses.entrySet().isEmpty()) && (totalEnrolmentsNotMigrated > 0)) {
			out.println("");
			out.println("DISCIPLINAS");
			Iterator iterator9 = curricularCourses.entrySet().iterator();
			while (iterator9.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator9.next();
				String key = (String) mapEntry.getKey();
				String value = (String) mapEntry.getValue();
				out.println("\t" + key + " - " + value);
			}
		}
		
		if((!degrees.entrySet().isEmpty()) && (totalEnrolmentsNotMigrated > 0)) {
			out.println("");
			out.println("CURSOS");
			Iterator iterator10 = degrees.entrySet().iterator();
			while (iterator10.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator10.next();
				String key = (String) mapEntry.getKey();
				String value = (String) mapEntry.getValue();
				out.println("\t" + key + " - " + value);
			}
		}
		
		ReportEnrolment.reset();
	}

//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param courseCode
	 * @return
	 */
	public static String findCurricularCourseName(String courseCode) {

		try {
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWCurricularCourse persistentMWCurricularCourse = mws.getIPersistentMWCurricularCourse();

			MWCurricularCourse mwCurricularCourse = persistentMWCurricularCourse.readByCode(courseCode);
			
			if(mwCurricularCourse != null) {
				return mwCurricularCourse.getCoursename();
			}

		} catch (PersistentMiddlewareSupportException e) {
			e.printStackTrace();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param degreeCode
	 * @return
	 */
	public static String findDegreeName(String degreeCode) {

		try {
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();

			MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(Integer.valueOf(degreeCode));

			if(mwDegreeTranslation != null) {
				return mwDegreeTranslation.getDegree().getNome();
			}

		} catch (PersistentMiddlewareSupportException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void reset()
	{
		curricularCoursesFoundInOtherDegree.clear();
		notFoundExecutionCourse.clear();
		notFoundCurricularCourseScopes.clear();
		notFoundCurricularCourses.clear();
		notFoundAttends.clear();
		replicatedCurricularCourses.clear();
		createdAttends.clear();
		curricularCourses.clear();
		degrees.clear();
		enrolmentsMigrated = 0;
		enrolmentEvaluationsMigrated = 0;
		enrolmentsDeleted = 0;
		enrolmentEvaluationsDeleted = 0;
		smallReport = true;
	}
}