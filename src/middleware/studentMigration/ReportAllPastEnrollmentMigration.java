package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWEnrolment;


/**
 * @author David Santos
 * 14/Out/2003
 */

public class ReportAllPastEnrollmentMigration
{
	// For errors:
	private static HashMap unknownCurricularCourses = new HashMap();
	private static HashMap unknownTeachersAndEmployees = new HashMap();

	// For information:
	private static int totalStudentCurricularPlansCreated = 0;
	private static int totalEnrollmentsCreated = 0;
	private static int totalEnrollmentEvaluationsCreated = 0;
	private static int totalCurricularCoursesCreated = 0;
	private static int totalCurricularCourseScopesCreated = 0;

	private static int unknownCurricularCoursesTimes = 0;
	private static int unknownTeachersAndEmployeesTimes = 0;

	/**
	 * @param mwEnrolment
	 */
	public static void addUnknownCurricularCourse(MWEnrolment mwEnrolment)
	{
		String key = mwEnrolment.getCoursecode();

		List mwEnrolmentCasesList = (List) ReportAllPastEnrollmentMigration.unknownCurricularCourses.get(key);
		if (mwEnrolmentCasesList == null)
		{
			mwEnrolmentCasesList = new ArrayList();
			mwEnrolmentCasesList.add(mwEnrolment);
		} else
		{
			if (!mwEnrolmentCasesList.contains(mwEnrolment)) {
				mwEnrolmentCasesList.add(mwEnrolment);
			}
		}
		
		ReportAllPastEnrollmentMigration.unknownCurricularCourses.put(key, mwEnrolmentCasesList);
		ReportAllPastEnrollmentMigration.unknownCurricularCoursesTimes++;
	}

	/**
	 * @param mwEnrolment
	 */
	public static void addUnknownTeachersAndEmployees(MWEnrolment mwEnrolment)
	{
		String key = mwEnrolment.getTeachernumber().toString();

		List mwEnrolmentCasesList = (List) ReportAllPastEnrollmentMigration.unknownTeachersAndEmployees.get(key);
		if (mwEnrolmentCasesList == null)
		{
			mwEnrolmentCasesList = new ArrayList();
			mwEnrolmentCasesList.add(mwEnrolment);
		} else
		{
			if (!mwEnrolmentCasesList.contains(mwEnrolment)) {
				mwEnrolmentCasesList.add(mwEnrolment);
			}
		}
		
		ReportAllPastEnrollmentMigration.unknownTeachersAndEmployees.put(key, mwEnrolmentCasesList);
		ReportAllPastEnrollmentMigration.unknownTeachersAndEmployeesTimes++;
	}

	/**
	 * @param value
	 */
	public static void addEnrolmentsMigrated(int value)
	{
		ReportAllPastEnrollmentMigration.totalEnrollmentsCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addStudentCurricularPlansMigrated(int value)
	{
		ReportAllPastEnrollmentMigration.totalStudentCurricularPlansCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addEnrollmentEvaluationsMigrated(int value)
	{
		ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addCurricularCoursesMigrated(int value)
	{
		ReportAllPastEnrollmentMigration.totalCurricularCoursesCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addCurricularCourseScopesMigrated(int value)
	{
		ReportAllPastEnrollmentMigration.totalCurricularCourseScopesCreated += value;
	}


	/**
	 * @param out
	 * @return
	 */
	private static void printUnknownCurricularCourses(PrintWriter out)
	{
		if(!ReportAllPastEnrollmentMigration.unknownCurricularCourses.entrySet().isEmpty()) {
			out.println("\nERROR TYPE 1 - UNKNOWN CURRICULAR COURSES");
			Iterator iterator = ReportAllPastEnrollmentMigration.unknownCurricularCourses.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List mwEnrolmentCasesList = (List) mapEntry.getValue();
				out.println("\tCourse Code: [" + key + "]. Happened [" + mwEnrolmentCasesList.size() + "] times!");
				out.println("\tCases:");
				Iterator iterator2 = mwEnrolmentCasesList.iterator();
				while (iterator2.hasNext()) {
					MWEnrolment mwEnrolment = (MWEnrolment) iterator2.next();
					out.println("\t" + mwEnrolment.toFlatString());
				}
			}
			out.println("TOTAL: " + ReportAllPastEnrollmentMigration.unknownCurricularCoursesTimes);
		}
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printNotFoundCurricularCourseScopes(PrintWriter out)
	{
		if(!ReportAllPastEnrollmentMigration.unknownTeachersAndEmployees.entrySet().isEmpty()) {
			out.println("\nERROR TYPE 2 - UNKNOWN TEACHERS AND EMPLOYEES");
			Iterator iterator = ReportAllPastEnrollmentMigration.unknownTeachersAndEmployees.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				String key = (String) mapEntry.getKey();
				List mwEnrolmentCasesList = (List) mapEntry.getValue();
				out.println("\tNumber: [" + key + "]. Happened [" + mwEnrolmentCasesList.size() + "] times!");
				out.println("\tCases:");
				Iterator iterator2 = mwEnrolmentCasesList.iterator();
				while (iterator2.hasNext()) {
					MWEnrolment mwEnrolment = (MWEnrolment) iterator2.next();
					out.println("\t" + mwEnrolment.toFlatString());
				}
			}
			out.println("TOTAL: " + ReportAllPastEnrollmentMigration.unknownTeachersAndEmployeesTimes);
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

		ReportAllPastEnrollmentMigration.printUnknownCurricularCourses(out);
		ReportAllPastEnrollmentMigration.printNotFoundCurricularCourseScopes(out);

		if(ReportAllPastEnrollmentMigration.totalEnrollmentsCreated > 0) {
			out.println("\nTOTAL MIGRATED ENROLMENTS: " + ReportAllPastEnrollmentMigration.totalEnrollmentsCreated);
		}

		int totalEnrolmentsNotMigrated = ReportAllPastEnrollmentMigration.unknownCurricularCoursesTimes + ReportAllPastEnrollmentMigration.unknownTeachersAndEmployeesTimes;
		if(totalEnrolmentsNotMigrated > 0) {
			out.println("\nTOTAL ENROLMENTS NOT MIGRADTED: " + totalEnrolmentsNotMigrated);
		}

		out.println("\n---------------------------------------------------------------------------");
		out.println("\n----------------------------- INFORMATIONS --------------------------------");
		out.println("\n---------------------------------------------------------------------------");
		
		out.println("[INFO] DONE!");
		out.println("[INFO] Total StudentCurricularPlans created: [" + ReportAllPastEnrollmentMigration.totalStudentCurricularPlansCreated + "].");
		out.println("[INFO] Total Enrolments created: [" + ReportAllPastEnrollmentMigration.totalEnrollmentsCreated + "].");
		out.println("[INFO] Total EnrolmentEvaluations created: [" + ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsCreated + "].");
		out.println("[INFO] Total CurricularCourses created: [" + ReportAllPastEnrollmentMigration.totalCurricularCoursesCreated + "].");
		out.println("[INFO] Total CurricularCourseScopes created: [" + ReportAllPastEnrollmentMigration.totalCurricularCourseScopesCreated + "].");

		out.close();
	}

	/**
	 * @param courseCode
	 */
//	private static String getCurricularCourseName(String courseCode)
//	{
//		String courseName = null;
//		try
//		{
//			courseName = CreateAndUpdateAllPastCurriculums.getCurricularCourseName(courseCode);
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return courseName;
//	}

	/**
	 * @param degreeCode
	 */
//	private static String getDegreeName(String degreeCode)
//	{
//		String degreeName = ReportEnrolment.findDegreeName(degreeCode);
//		return degreeName;
//	}
}