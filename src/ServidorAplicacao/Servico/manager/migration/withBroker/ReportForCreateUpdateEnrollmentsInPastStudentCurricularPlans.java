package ServidorAplicacao.Servico.manager.migration.withBroker;

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

public class ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans
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
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsDeleted++;
	}
	
	public static void addEnrollmentEvaluationsDeleted()
	{
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentEvaluationsDeleted++;
	}

	/**
	 * @param mwStudent
	 */
	public static void addClassCastExceptions(MWStudent mwStudent)
	{
		String value = "Number: [" + mwStudent.getNumber() + "] Degree: [" + mwStudent.getDegreecode() + "] Branch: [" + mwStudent.getBranchcode() + "] Document ID number: [" + mwStudent.getDocumentidnumber() + "]";
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.classCastExceptions.add(value);
	}

	/**
	 * @param mwEnrolment
	 */
	public static void addUnknownCurricularCourse(MWEnrolment mwEnrolment)
	{
		String key = mwEnrolment.getCoursecode();

		List mwEnrolmentCasesList = (List) ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCourses.get(key);
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
		
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCourses.put(key, mwEnrolmentCasesList);
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCoursesTimes++;
	}

	/**
	 * @param mwEnrolment
	 */
	public static void addUnknownTeachersAndEmployees(MWEnrolment mwEnrolment)
	{
		String key = mwEnrolment.getTeachernumber().toString();

		List mwEnrolmentCasesList = (List) ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployees.get(key);
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
		
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployees.put(key, mwEnrolmentCasesList);
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployeesTimes++;
	}

	/**
	 * @param mwEnrolment
	 */
	public static void addNotCreatedEnrolmentEvaluation(String key, MWEnrolment mwEnrolment)
	{
		List mwEnrolmentCasesList = (List) ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.notCreatedEnrolmentEvaluations.get(key);
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
		
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.notCreatedEnrolmentEvaluations.put(key, mwEnrolmentCasesList);
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.notCreatedEnrolmentEvaluationsTimes++;
	}

	/**
	 * @param value
	 */
	public static void addEnrolmentsMigrated(int value)
	{
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addStudentCurricularPlansMigrated(int value)
	{
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalStudentCurricularPlansCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addEnrollmentEvaluationsMigrated(int value)
	{
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentEvaluationsCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addCurricularCoursesMigrated(int value)
	{
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalCurricularCoursesCreated += value;
	}

	/**
	 * @param value
	 */
	public static void addCurricularCourseScopesMigrated(int value)
	{
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalCurricularCourseScopesCreated += value;
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printUnknownCurricularCourses(PrintWriter out)
	{
		if(!ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCourses.entrySet().isEmpty()) {
			out.println("");
			out.println("ERROR TYPE 1 - UNKNOWN CURRICULAR COURSES");
			Iterator iterator = ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCourses.entrySet().iterator();
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
			out.println("TOTAL: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCoursesTimes);
		}
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printNotFoundCurricularCourseScopes(PrintWriter out)
	{
		if(!ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployees.entrySet().isEmpty()) {
			out.println("");
			out.println("ERROR TYPE 2 - UNKNOWN TEACHERS AND EMPLOYEES");
			Iterator iterator = ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployees.entrySet().iterator();
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
			out.println("TOTAL: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployeesTimes);
		}
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printClassCastExceptions(PrintWriter out)
	{
		if(!ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.classCastExceptions.isEmpty()) {
			out.println("");
			out.println("ERROR TYPE 3 - CLASS CAST EXCEPTIONS");
			Iterator iterator = ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.classCastExceptions.iterator();
			while (iterator.hasNext())
			{
				String value = (String) iterator.next();
				out.println("\t" + value);
			}
			out.println("TOTAL: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.classCastExceptions.size());
		}
	}

	/**
	 * @param out
	 * @return
	 */
	private static void printNotCreatedEnrolmentEvaluations(PrintWriter out)
	{
		if(!ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.notCreatedEnrolmentEvaluations.entrySet().isEmpty()) {
			out.println("");
			out.println("WARNING TYPE 1 - ENROLLMENT EVALUATIONS NOT CREATED");
			out.println("\tCases:");
			Iterator iterator = ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.notCreatedEnrolmentEvaluations.entrySet().iterator();
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
			out.println("TOTAL: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.notCreatedEnrolmentEvaluationsTimes);
		}
	}

//	/**
//	 * @param out
//	 * @return
//	 */
//	private static void printEnrollmentsAndEvaluationsDeleted(PrintWriter out)
//	{
//		if(ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsDeleted != 0)
//		{
//			out.println("\n[INFO] ENROLLMENTS DELETED: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsDeleted);
//		}
//		if(ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentEvaluationsDeleted != 0)
//		{
//			out.println("\n[INFO] ENROLLMENT EVALUATIONS DELETED: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentEvaluationsDeleted);
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

		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.printUnknownCurricularCourses(out);
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.printNotFoundCurricularCourseScopes(out);
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.printClassCastExceptions(out);

		if(ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsCreated > 0) {
			out.println("[INFO] TOTAL MIGRATED ENROLMENTS: " + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsCreated);
		}

		int totalEnrolmentsNotMigrated = ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownCurricularCoursesTimes + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.unknownTeachersAndEmployeesTimes;
		if(totalEnrolmentsNotMigrated > 0) {
			out.println("[INFO] TOTAL ENROLMENTS NOT MIGRADTED: " + totalEnrolmentsNotMigrated);
		}

		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("----------------------------- WARNING CASES -------------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");

		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.printNotCreatedEnrolmentEvaluations(out);

		out.println("");
		out.println("---------------------------------------------------------------------------");
		out.println("");
		out.println("----------------------------- INFORMATIONS --------------------------------");
		out.println("");
		out.println("---------------------------------------------------------------------------");
		
		out.println("[INFO] DONE!");
		out.println("[INFO] Total StudentCurricularPlans created: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalStudentCurricularPlansCreated + "].");
		out.println("[INFO] Total Enrolments created: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsCreated + "].");
		out.println("[INFO] Total EnrolmentEvaluations created: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentEvaluationsCreated + "].");
		out.println("[INFO] Total CurricularCourses created: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalCurricularCoursesCreated + "].");
//		out.println("[INFO] Total CurricularCourseScopes created: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalCurricularCourseScopesCreated + "].");
		out.println("[INFO] Total Enrolments deleted: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentsDeleted + "]");
		out.println("[INFO] Total EnrolmentEvaluations deleted: [" + ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.totalEnrollmentEvaluationsDeleted + "]");
		
		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.reset();
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