/*
 * Created on 14/Out/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author David Santos
 * 14/Out/2003
 */
public class ReportEnrolment {
	private static HashMap notFoundCurricularCourses = new HashMap();
	private static HashMap notFoundCurricularCoursesCardinality = new HashMap();
	
	/**
	 * 
	 * @param courseCode course enrolled by student
	 * @param degreeCode student degree
	 * @param foundHere list of @link Dominio.CurricularCourseScope
	 */
	public static void addNotFoundCurricularCourse (String courseCode, String degreeCode, List foundHere){
		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);
		Integer times = new Integer(1);
		if (!notFoundCurricularCourses.containsKey(key.toString())){
			notFoundCurricularCourses.put(key.toString(), foundHere);
		} else {
			Integer timesBefore = (Integer) notFoundCurricularCoursesCardinality.get(key.toString());
			times = new Integer(timesBefore.intValue() + 1);
		}
		notFoundCurricularCoursesCardinality.put(key.toString(), times);
	}
	
	public static void report(PrintWriter out){
		out.println("Enrolment cases --------------------------------------------------------------------------");
		out.println("\tCaso 1 - Disciplinas não encontradas no curso do aluno mas encontradas em varios outros cursos");
		Iterator iterator = notFoundCurricularCourses.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			String key = (String) mapEntry.getKey();
			
			out.println("\t\tCadeira - Curso = " + key);
			out.print("\tAconteceu " + notFoundCurricularCoursesCardinality.get(key) + " veze(s)...");
			List foundHere = (List) mapEntry.getValue();
			// TODO [DAVID] print list here
			
		}
		out.close();
	}
}
