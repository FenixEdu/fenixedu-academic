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

public class ReportAllPastEnrollmentMigration
{
	// For errors:
	private static HashMap notFoundDegreeCurricularPlans = new HashMap();
	private static HashMap notFoundStudentCurricularPlans = new HashMap();
	private static HashMap notFoundCurricularCourseScopes = new HashMap();
	private static HashMap notFoundCurricularCourses = new HashMap();

	// For information:
	private static HashMap curricularCourses = new HashMap();
	private static HashMap degrees = new HashMap();
	private static int enrolmentsMigrated = 0;

//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param degreeCode
	 * @param studentNumber
	 */
	public static void addNotFoundStudentCurricularPlan(String degreeCode, String studentNumber)
	{
		ReportAllPastEnrollmentMigration.addDegreeName(degreeCode);

		String key = new String(degreeCode);

		List studentList = (List) ReportAllPastEnrollmentMigration.notFoundStudentCurricularPlans.get(key);
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
		
		ReportAllPastEnrollmentMigration.notFoundStudentCurricularPlans.put(key, studentList);
	}

	/**
	 * @param degreeCode
	 * @param studentNumber
	 */
	public static void addNotFoundDegreeCurricularPlan(String degreeCode, String studentNumber)
	{
		ReportAllPastEnrollmentMigration.addDegreeName(degreeCode);

		String key = new String(degreeCode);

		List studentList = (List) ReportAllPastEnrollmentMigration.notFoundDegreeCurricularPlans.get(key);
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
		
		ReportAllPastEnrollmentMigration.notFoundDegreeCurricularPlans.put(key, studentList);
	}

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 * @param year
	 * @param semester
	 * @param branchCode
	 */
	public static void addNotFoundCurricularCourseScope(String courseCode, String degreeCode, String studentNumber, String year, String semester, String branchCode)
	{
		ReportAllPastEnrollmentMigration.addCurricularCourseName(courseCode);
		ReportAllPastEnrollmentMigration.addDegreeName(degreeCode);

		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode).append(" - ").append(year).append(" - ").append(semester).append(" - ").append(branchCode);

		List studentList = (List) ReportAllPastEnrollmentMigration.notFoundCurricularCourseScopes.get(key.toString());
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
		
		ReportAllPastEnrollmentMigration.notFoundCurricularCourseScopes.put(key.toString(), studentList);
	}

	/**
	 * @param courseCode
	 * @param degreeCode
	 * @param studentNumber
	 */
	public static void addNotFoundCurricularCourse(String courseCode, String degreeCode, String studentNumber)
	{
		ReportAllPastEnrollmentMigration.addCurricularCourseName(courseCode);
		ReportAllPastEnrollmentMigration.addDegreeName(degreeCode);

		StringBuffer key = new StringBuffer(courseCode).append(" - ").append(degreeCode);

		List studentList = (List) ReportAllPastEnrollmentMigration.notFoundCurricularCourses.get(key.toString());
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
		
		ReportAllPastEnrollmentMigration.notFoundCurricularCourses.put(key.toString(), studentList);
	}

// ------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param courseCode
	 */
	private static void addCurricularCourseName(String courseCode)
	{
		String key = courseCode;
		String courseName = ReportEnrolment.findCurricularCourseName(courseCode);

		String value = (String) ReportAllPastEnrollmentMigration.curricularCourses.get(key);
		if (value == null) {
			ReportAllPastEnrollmentMigration.curricularCourses.put(key, courseName);
		}
	}

	/**
	 * @param degreeCode
	 */
	private static void addDegreeName(String degreeCode)
	{
		String key = degreeCode;
		String degreeName = ReportEnrolment.findDegreeName(degreeCode);

		String value = (String) ReportAllPastEnrollmentMigration.degrees.get(key);
		if (value == null) {
			ReportAllPastEnrollmentMigration.degrees.put(key, degreeName);
		}
	}

	/**
	 * @param value
	 */
	public static void addEnrolmentMigrated(int value)
	{
		ReportAllPastEnrollmentMigration.enrolmentsMigrated = ReportAllPastEnrollmentMigration.enrolmentsMigrated + value;
	}

	/**
	 *
	 */
	public static void addEnrolmentMigrated()
	{
		ReportAllPastEnrollmentMigration.enrolmentsMigrated++;
	}

//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param out
	 * @return
	 */
	private static int printNotFoundDegreeCurricularPlans(PrintWriter out)
	{
		int totalNotFoundDegreeCurricularPlans = 0;
		if(!ReportAllPastEnrollmentMigration.notFoundDegreeCurricularPlans.entrySet().isEmpty()) {
			out.println("\nERROR TYPE 1 - NOT FOUND DEGREE CURRICULAR PLANS");
			Iterator iterator = ReportAllPastEnrollmentMigration.notFoundDegreeCurricularPlans.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCurso: [" + key + "] ");
				out.println("Happened [" + studentList.size() + "] time(s)...");
				out.println("\t\tStudents: "+ studentList.toString());
				totalNotFoundDegreeCurricularPlans = totalNotFoundDegreeCurricularPlans + studentList.size();
			}
			out.println("TOTAL: " + totalNotFoundDegreeCurricularPlans);
		}
		return totalNotFoundDegreeCurricularPlans;
	}

	/**
	 * @param out
	 * @return
	 */
	private static int printNotFoundCurricularCourseScopes(PrintWriter out)
	{
		int totalNotFoundCurricularCourseScopes = 0;
		if(!ReportAllPastEnrollmentMigration.notFoundCurricularCourseScopes.entrySet().isEmpty()) {
			out.println("\nERROR TYPE 2 - NOT FOUND CURRICULAR COURSE SCOPES");
			Iterator iterator = ReportAllPastEnrollmentMigration.notFoundCurricularCourseScopes.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCourse - Degree - Year - Semester - Branch: [" + key + "] ");
				out.println("Happened [" + studentList.size() + "] times(s)...");
				out.println("\t\tStudents: "+ studentList.toString());
				totalNotFoundCurricularCourseScopes = totalNotFoundCurricularCourseScopes + studentList.size();
			}
			out.println("TOTAL: " + totalNotFoundCurricularCourseScopes);
		}
		return totalNotFoundCurricularCourseScopes;
	}

	/**
	 * @param out
	 * @return
	 */
	private static int printNotFoundCurricularCourses(PrintWriter out)
	{
		int totalNotFoundCurricularCourses = 0;
		if(!ReportAllPastEnrollmentMigration.notFoundCurricularCourses.entrySet().isEmpty()) {
			out.println("\nERROR TYPE 3 - NOT FOUND CURRICULAR COURSES");
			Iterator iterator = ReportAllPastEnrollmentMigration.notFoundCurricularCourses.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tCourse - Degree: [" + key + "] ");
				out.println("Happened [" + studentList.size() + "] times(s)...");
				out.println("\t\tStudents: "+ studentList.toString());
				totalNotFoundCurricularCourses = totalNotFoundCurricularCourses + studentList.size();
			}
			out.println("TOTAL: " + totalNotFoundCurricularCourses);
		}
		return totalNotFoundCurricularCourses;
	}

	/**
	 * @param out
	 * @return
	 */
	private static int printNotFoundStudentCurricularPlans(PrintWriter out)
	{
		int totalNotFoundStudentCurricularPlans = 0;
		if(!ReportAllPastEnrollmentMigration.notFoundStudentCurricularPlans.entrySet().isEmpty()) {
			out.println("\nERROR TYPE 4 - NOT FOUND STUDENT CURRICULAR PLANS");
			Iterator iterator = ReportAllPastEnrollmentMigration.notFoundStudentCurricularPlans.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List studentList = (List) mapEntry.getValue();
				out.print("\tDegree: [" + key + "] ");
				out.println("Happened [" + studentList.size() + "] times(s)...");
				out.println("\t\tStudents: "+ studentList.toString());
				totalNotFoundStudentCurricularPlans = totalNotFoundStudentCurricularPlans + studentList.size();
			}
			out.println("TOTAL: " + totalNotFoundStudentCurricularPlans);
		}
		return totalNotFoundStudentCurricularPlans;
	}

	/**
	 * @param out
	 * @param totalEnrolmentsNotMigrated
	 */
	private static void printCurricularCoursesNames(PrintWriter out, int totalEnrolmentsNotMigrated)
	{
		if((!ReportAllPastEnrollmentMigration.curricularCourses.entrySet().isEmpty()) && (totalEnrolmentsNotMigrated > 0)) {
			out.println("\nCURRICULAR COURSES");
			Iterator iterator = ReportAllPastEnrollmentMigration.curricularCourses.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				String value = (String) mapEntry.getValue();
				out.println("\t[" + key + "] - [" + value + "]");
			}
		}
	}

	/**
	 * @param out
	 * @param totalEnrolmentsNotMigrated
	 */
	private static void printDegreesNames(PrintWriter out, int totalEnrolmentsNotMigrated)
	{
		if((!ReportAllPastEnrollmentMigration.degrees.entrySet().isEmpty()) && (totalEnrolmentsNotMigrated > 0)) {
			out.println("\nCURSOS");
			Iterator iterator = ReportAllPastEnrollmentMigration.degrees.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				String value = (String) mapEntry.getValue();
				out.println("\t[" + key + "] - [" + value + "]");
			}
		}
	}

	/**
	 * @param out
	 */
	public static void report(PrintWriter out)
	{
		out.println("\n---------------------------------------------------------------------------");
		out.println("\n------------------------------ ERROR CASES --------------------------------");
		out.println("\n---------------------------------------------------------------------------");

		int totalNotFoundDegreeCurricularPlans = ReportAllPastEnrollmentMigration.printNotFoundDegreeCurricularPlans(out);
		int totalNotFoundCurricularCourseScopes = ReportAllPastEnrollmentMigration.printNotFoundCurricularCourseScopes(out);
		int totalNotFoundCurricularCourses = ReportAllPastEnrollmentMigration.printNotFoundCurricularCourses(out);
		int totalNotFoundStudentCurricularPlans = ReportAllPastEnrollmentMigration.printNotFoundStudentCurricularPlans(out);
		
		
		if(ReportAllPastEnrollmentMigration.enrolmentsMigrated > 0) {
			out.println("\nTOTAL MIGRATED ENROLMENTS: " + ReportAllPastEnrollmentMigration.enrolmentsMigrated);
		}

		int totalEnrolmentsNotMigrated = totalNotFoundDegreeCurricularPlans + totalNotFoundCurricularCourseScopes + totalNotFoundCurricularCourses + totalNotFoundStudentCurricularPlans;
		if(totalEnrolmentsNotMigrated > 0) {
			out.println("\nTOTAL ENROLMENTS NOT MIGRADTED: " + totalEnrolmentsNotMigrated);
		}

		out.println("\n---------------------------------------------------------------------------");
		out.println("\n----------------------------- INFORMATIONS --------------------------------");
		out.println("\n---------------------------------------------------------------------------");
		
		ReportAllPastEnrollmentMigration.printCurricularCoursesNames(out, totalEnrolmentsNotMigrated);
		ReportAllPastEnrollmentMigration.printDegreesNames(out, totalEnrolmentsNotMigrated);

		out.close();
	}
}