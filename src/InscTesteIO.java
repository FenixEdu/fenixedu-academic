import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;

/**
 * @author David Santos
 */

public abstract class InscTesteIO {

	public static void showFinalSpan(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();

		System.out.println("TO CHOOSE (FINAL SPAN):");
		Iterator iterator = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
			System.out.println(i + " - YEAR: " + infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear() + "; SEMESTER: " + infoCurricularCourseScope.getInfoCurricularSemester().getSemester() + "; CURRICULAR COURSE: " + infoCurricularCourseScope.getInfoCurricularCourse().getName());
			i++;
		}

		System.out.println();

		System.out.println("AUTOMATIC CHOOSEN (ANUALS + ALTERNATIVES):");
		Iterator iterator2 = infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled().iterator();
		int j = 0;
		while (iterator2.hasNext()) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator2.next();
			System.out.println(j + " - YEAR: " + infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear() + "; SEMESTER: " + infoCurricularCourseScope.getInfoCurricularSemester().getSemester() + "; CURRICULAR COURSE: " + infoCurricularCourseScope.getInfoCurricularCourse().getName());
			j++;
		}
	}

	public static void showAvailableDegreesForOption(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("AVAILABLE DEGREES FOR OPTION:");
		Iterator iterator = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			InfoDegree infoDegree = (InfoDegree) iterator.next();
			System.out.println(i + " - NAME: " + infoDegree.getNome() + "; CODE: " + infoDegree.getSigla());
			i++;
		}
	}

	public static void showAvailableCurricularCoursesForOption(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("AVAILABLE COURSES FOR OPTION:");
		Iterator iterator = infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
			System.out.println(i + " - NAME: " + infoCurricularCourse.getName() + "; CODE: " + infoCurricularCourse.getCode());
			i++;
		}
	}

	public static void showChosenCurricularCoursesForOptionalCurricularCourses(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("CHOSEN COURSE AND OPTIONAL COURSE:");
		Iterator iterator = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().iterator();
		while (iterator.hasNext()) {
			InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
			System.out.println("OPTIONAL CURRICULAR COURSE NAME: " + infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourse().getName() + "; CHOSEN CURRICULAR COURSE : " + infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseForOption().getName());
		}
	}

	public static void showActualEnrolments(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("ACTUAL ENROLMENTS:");
		Iterator iterator = infoEnrolmentContext.getActualEnrolment().iterator();
		while (iterator.hasNext()) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
			System.out.println("YEAR: " + infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear() + "; SEMESTER: " + infoCurricularCourseScope.getInfoCurricularSemester().getSemester() + "; CURRICULAR COURSE: " + infoCurricularCourseScope.getInfoCurricularCourse().getName());
		}
	}

	public static void showEnrolmentValidationResultMessages(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("ENROLMENT VALIDATION MESSAGES:");
		Set keySet = infoEnrolmentContext.getEnrolmentValidationResult().getMessage().keySet();

		Iterator iterator = keySet.iterator();
		int i = 1;
		while (iterator.hasNext()) {
			String msg = (String) iterator.next();
			System.out.println(i + " - MESSAGE: " + msg);
			i++;
		}
	}

	public static void selectNormalCurricularCoursesToEnroll(InfoEnrolmentContext infoEnrolmentContext) {
		try {
			List indexes = new ArrayList();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String str = new String(" ");
			String endStr = new String("");
			while(!str.equals(endStr)) {
				System.out.print("> ");
				str = in.readLine();
				if(!str.equals(endStr)) {
					Integer i = new Integer(str);
					indexes.add(i);
				}
			}

			Iterator iterator = indexes.iterator();
			while(iterator.hasNext()) {
				int i = ((Integer) iterator.next()).intValue();
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
				infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public static void selectDegreeForOptionalCurricularCourseToEnroll(InfoEnrolmentContext infoEnrolmentContext) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String str = new String(" ");
			String endStr = new String("");
			System.out.print("> ");
			str = in.readLine();
			if(!str.equals(endStr)) {
				Integer i = new Integer(str);
				infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(i.intValue()));
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public static InfoCurricularCourse selectOptionalCurricularCourseToEnroll(InfoEnrolmentContext infoEnrolmentContext) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String str = new String(" ");
			String endStr = new String("");
			System.out.print("> ");
			str = in.readLine();
			if(!str.equals(endStr)) {
				Integer i = new Integer(str);
				return (InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(i.intValue());
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}


}