package middleware.studentMigration.enrollments;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;


/**
 * @author David Santos
 * 14/Out/2003
 */

public class ReportAllPastEnrollmentMigration
{
	// For errors:
	private static HashMap unknownCurricularCourses = new HashMap();
	private static HashMap unknownTeachersAndEmployees = new HashMap();
	private static HashMap notCreatedEnrolmentEvaluations = new HashMap();
	
	// For information:
	private static int totalStudentCurricularPlansCreated = 0;
	private static int totalEnrollmentsCreated = 0;
	private static int totalEnrollmentEvaluationsCreated = 0;
	private static int totalCurricularCoursesCreated = 0;
	private static int totalCurricularCourseScopesCreated = 0;
	private static int totalEnrollmentsDeleted = 0;
	private static int totalEnrollmentEvaluationsDeleted = 0;
	
	private static int unknownCurricularCoursesTimes = 0;
	private static int unknownTeachersAndEmployeesTimes = 0;
	private static int notCreatedEnrolmentEvaluationsTimes = 0;

	// For debuging:
	private static List classCastExceptions = new ArrayList();

	
	public static void addEnrollmentsDeleted()
	{
		ReportAllPastEnrollmentMigration.totalEnrollmentsDeleted++;
	}
	
	public static void addEnrollmentEvaluationsDeleted()
	{
		ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsDeleted++;
	}

	/**
	 * @param mwStudent
	 */
	public static void addClassCastExceptions(MWStudent mwStudent)
	{
		String value = "Number: [" + mwStudent.getNumber() + "] Degree: [" + mwStudent.getDegreecode() + "] Branch: [" + mwStudent.getBranchcode() + "] Document ID number: [" + mwStudent.getDocumentidnumber() + "]";
		ReportAllPastEnrollmentMigration.classCastExceptions.add(value);
	}

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
	 * @param mwEnrolment
	 */
	public static void addNotCreatedEnrolmentEvaluation(String key, MWEnrolment mwEnrolment)
	{
		List mwEnrolmentCasesList = (List) ReportAllPastEnrollmentMigration.notCreatedEnrolmentEvaluations.get(key);
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
		
		ReportAllPastEnrollmentMigration.notCreatedEnrolmentEvaluations.put(key, mwEnrolmentCasesList);
		ReportAllPastEnrollmentMigration.notCreatedEnrolmentEvaluationsTimes++;
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
			out.println("");
			out.println("ERROR TYPE 1 - UNKNOWN CURRICULAR COURSES");
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
					out.println("\t\t" + mwEnrolment.toFlatString());
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
			out.println("");
			out.println("ERROR TYPE 2 - UNKNOWN TEACHERS AND EMPLOYEES");
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
					out.println("\t\t" + mwEnrolment.toFlatString());
				}
			}
			out.println("TOTAL: " + ReportAllPastEnrollmentMigration.unknownTeachersAndEmployeesTimes);
		}
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printClassCastExceptions(PrintWriter out)
	{
		if(!ReportAllPastEnrollmentMigration.classCastExceptions.isEmpty()) {
			out.println("");
			out.println("ERROR TYPE 3 - CLASS CAST EXCEPTIONS");
			Iterator iterator = ReportAllPastEnrollmentMigration.classCastExceptions.iterator();
			while (iterator.hasNext())
			{
				String value = (String) iterator.next();
				out.println("\t" + value);
			}
			out.println("TOTAL: " + ReportAllPastEnrollmentMigration.classCastExceptions.size());
		}
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printNotCreatedEnrolmentEvaluations(PrintWriter out)
	{
		if(!ReportAllPastEnrollmentMigration.notCreatedEnrolmentEvaluations.entrySet().isEmpty()) {
			out.println("");
			out.println("WARNING TYPE 1 - ENROLLMENT EVALUATIONS NOT CREATED");
			out.println("\tCases:");
			Iterator iterator = ReportAllPastEnrollmentMigration.notCreatedEnrolmentEvaluations.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				List mwEnrolmentCasesList = (List) mapEntry.getValue();
				Iterator iterator2 = mwEnrolmentCasesList.iterator();
				while (iterator2.hasNext()) {
					MWEnrolment mwEnrolment = (MWEnrolment) iterator2.next();
					out.println("\t" + mwEnrolment.toFlatString());
				}
			}
			out.println("TOTAL: " + ReportAllPastEnrollmentMigration.notCreatedEnrolmentEvaluationsTimes);
		}
	}

//	/**
//	 * @param out
//	 * @return
//	 */
//	private static void printEnrollmentsAndEvaluationsDeleted(PrintWriter out)
//	{
//		if(ReportAllPastEnrollmentMigration.totalEnrollmentsDeleted != 0)
//		{
//			out.println("\n[INFO] ENROLLMENTS DELETED: " + ReportAllPastEnrollmentMigration.totalEnrollmentsDeleted);
//		}
//		if(ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsDeleted != 0)
//		{
//			out.println("\n[INFO] ENROLLMENT EVALUATIONS DELETED: " + ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsDeleted);
//		}
//	}

	/**
	 * @param out
	 */
	public static void report(PrintWriter out)
	{
		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("------------------------------ ERROR CASES --------------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");

		ReportAllPastEnrollmentMigration.printUnknownCurricularCourses(out);
		ReportAllPastEnrollmentMigration.printNotFoundCurricularCourseScopes(out);
		ReportAllPastEnrollmentMigration.printClassCastExceptions(out);

		if(ReportAllPastEnrollmentMigration.totalEnrollmentsCreated > 0) {
			out.println("[INFO] TOTAL MIGRATED ENROLMENTS: " + ReportAllPastEnrollmentMigration.totalEnrollmentsCreated);
		}

		int totalEnrolmentsNotMigrated = ReportAllPastEnrollmentMigration.unknownCurricularCoursesTimes + ReportAllPastEnrollmentMigration.unknownTeachersAndEmployeesTimes;
		if(totalEnrolmentsNotMigrated > 0) {
			out.println("[INFO] TOTAL ENROLMENTS NOT MIGRADTED: " + totalEnrolmentsNotMigrated);
		}

		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("----------------------------- WARNING CASES -------------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");

		ReportAllPastEnrollmentMigration.printNotCreatedEnrolmentEvaluations(out);

		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("----------------------------- INFORMATIONS --------------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");
		
		out.println("[INFO] DONE!");
		out.println("[INFO] Total StudentCurricularPlans created: [" + ReportAllPastEnrollmentMigration.totalStudentCurricularPlansCreated + "].");
		out.println("[INFO] Total Enrolments created: [" + ReportAllPastEnrollmentMigration.totalEnrollmentsCreated + "].");
		out.println("[INFO] Total EnrolmentEvaluations created: [" + ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsCreated + "].");
		out.println("[INFO] Total CurricularCourses created: [" + ReportAllPastEnrollmentMigration.totalCurricularCoursesCreated + "].");
//		out.println("[INFO] Total CurricularCourseScopes created: [" + ReportAllPastEnrollmentMigration.totalCurricularCourseScopesCreated + "].");
		out.println("[INFO] Total Enrolments deleted: [" + ReportAllPastEnrollmentMigration.totalEnrollmentsDeleted + "]");
		out.println("[INFO] Total EnrolmentEvaluations deleted: [" + ReportAllPastEnrollmentMigration.totalEnrollmentEvaluationsDeleted + "]");
		
		ReportAllPastEnrollmentMigration.reset();
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


	private static void reset()
	{
		unknownCurricularCourses.clear();
		unknownTeachersAndEmployees.clear();
		notCreatedEnrolmentEvaluations.clear();
		totalStudentCurricularPlansCreated = 0;
		totalEnrollmentsCreated = 0;
		totalEnrollmentEvaluationsCreated = 0;
		totalCurricularCoursesCreated = 0;
		totalCurricularCourseScopesCreated = 0;
		totalEnrollmentsDeleted = 0;
		totalEnrollmentEvaluationsDeleted = 0;
		unknownCurricularCoursesTimes = 0;
		unknownTeachersAndEmployeesTimes = 0;
		notCreatedEnrolmentEvaluationsTimes = 0;
		classCastExceptions.clear();
	}
}