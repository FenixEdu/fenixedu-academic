import java.util.Iterator;
import java.util.Set;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 31/Mar/2003
 */

public class InscTeste {

	private static GestorServicos gestor = GestorServicos.manager();
	private static IUserView userView = new UserView("user", null);

	public static void main(String[] args) {

		InfoEnrolmentContext infoEnrolmentContext = null;
		
		autentication();

		Object serviceArgs1[] = {userView};
		infoEnrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs1);
		showFinalSpan(infoEnrolmentContext);
		showActualEnrolments(infoEnrolmentContext);
		showChosenCurricularCoursesForOptionalCurricularCourses(infoEnrolmentContext);
		
//		Object serviceArgs2[] = { infoEnrolmentContext };
//		infoEnrolmentContext = executeService("ShowAvailableDegreesForOption", serviceArgs2);
//		showAvailableDegreesForOption(infoEnrolmentContext);
//
//		infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(0));
//
//		Object serviceArgs3[] = { infoEnrolmentContext };
//		infoEnrolmentContext = executeService("ShowAvailableCurricularCoursesForOption", serviceArgs3);
//		showAvailableCurricularCoursesForOption(infoEnrolmentContext);
//
//		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(((InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(3)));
//
//		Object serviceArgs4[] = { infoEnrolmentContext, (InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(0) };
//		infoEnrolmentContext = executeService("SelectOptionalCurricularCourse", serviceArgs4);
//		showCosenCurricularCoursesForOptionalCurricularCourse(infoEnrolmentContext);

//		Object serviceArgs5[] = { infoEnrolmentContext };
//		infoEnrolmentContext = executeService("ShowAvailableDegreesForOption", serviceArgs5);
//		showAvailableDegreesForOption(infoEnrolmentContext);
//
//		infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(0));
//
//		Object serviceArgs6[] = { infoEnrolmentContext };
//		infoEnrolmentContext = executeService("ShowAvailableCurricularCoursesForOption", serviceArgs6);
//		showAvailableCurricularCoursesForOption(infoEnrolmentContext);
//
//		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(((InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(5)));
//
//		Object serviceArgs7[] = { infoEnrolmentContext, (InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(1) };
//		infoEnrolmentContext = executeService("SelectOptionalCurricularCourse", serviceArgs7);
//		showCosenCurricularCoursesForOptionalCurricularCourse(infoEnrolmentContext);
//
//		Object serviceArgs8[] = { infoEnrolmentContext };
//		infoEnrolmentContext = executeService("ShowAvailableDegreesForOption", serviceArgs8);
//		showAvailableDegreesForOption(infoEnrolmentContext);
//
//		infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(0));
//
//		Object serviceArgs9[] = { infoEnrolmentContext };
//		infoEnrolmentContext = executeService("ShowAvailableCurricularCoursesForOption", serviceArgs9);
//		showAvailableCurricularCoursesForOption(infoEnrolmentContext);

//		for(int i = 0; i < 1; i++) {
//			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//		}

//		InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(0);
//		infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//		infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(1);
//		infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//		infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(2);
//		infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//		infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(3);
//		infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//		infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(4);
//		infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//		infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(5);
//		infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);


//		Object serviceArgs10[] = {infoEnrolmentContext};
//		infoEnrolmentContext = executeService("ValidateActualEnrolment", serviceArgs10);
//		showActualEnrolments(infoEnrolmentContext);
//		showEnrolmentValidationResultMessages(infoEnrolmentContext);

//		Object serviceArgs11[] = {userView};
//		executeService("ChangeEnrolmentStateFromTemporarilyToEnroled", serviceArgs11);		
	}

	private static void autentication() {
		String argsAutenticacao[] = { "user", "pass" };
		try {
			userView = (IUserView) gestor.executar(null, "Autenticacao", argsAutenticacao);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static InfoEnrolmentContext executeService(String serviceName, Object[] serviceArgs) {
		try {
			Object result = null;
			result = gestor.executar(userView, serviceName, serviceArgs);
			return (InfoEnrolmentContext) result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static void showFinalSpan(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();

		System.out.println("TO CHOOSE (FINAL SPAN):");
		Iterator iterator = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (iterator.hasNext()) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
			System.out.println("YEAR: " + infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear() + "; SEMESTER: " + infoCurricularCourseScope.getInfoCurricularSemester().getSemester() + "; CURRICULAR COURSE: " + infoCurricularCourseScope.getInfoCurricularCourse().getName());
		}

		System.out.println();

		System.out.println("AUTOMATIC CHOOSEN (ANUALS + ALTERNATIVES):");
		Iterator iterator2 = infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled().iterator();
		while (iterator2.hasNext()) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator2.next();
			System.out.println("YEAR: " + infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear() + "; SEMESTER: " + infoCurricularCourseScope.getInfoCurricularSemester().getSemester() + "; CURRICULAR COURSE: " + infoCurricularCourseScope.getInfoCurricularCourse().getName());
		}
	}

	private static void showAvailableDegreesForOption(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("AVAILABLE DEGREES FOR OPTION:");
		Iterator iterator = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().iterator();
		while (iterator.hasNext()) {
			InfoDegree infoDegree = (InfoDegree) iterator.next();
			System.out.println("NAME: " + infoDegree.getNome() + "; CODE: " + infoDegree.getSigla());
		}
	}

	private static void showAvailableCurricularCoursesForOption(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("AVAILABLE COURSES FOR OPTION:");
		Iterator iterator = infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().iterator();
		while (iterator.hasNext()) {
			InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
			System.out.println("NAME: " + infoCurricularCourse.getName() + "; CODE: " + infoCurricularCourse.getCode());
		}
	}

	private static void showChosenCurricularCoursesForOptionalCurricularCourses(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("CHOSEN COURSE AND OPTIONAL COURSE:");
		Iterator iterator = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().iterator();
		while (iterator.hasNext()) {
			InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
			System.out.println("OPTIONAL CURRICULAR COURSE NAME: " + infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourse().getName() + "; CHOSEN CURRICULAR COURSE : " + infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseForOption().getName());
		}
	}

	private static void showActualEnrolments(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("ACTUAL ENROLMENTS:");
		Iterator iterator = infoEnrolmentContext.getActualEnrolment().iterator();
		while (iterator.hasNext()) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
			System.out.println("YEAR: " + infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear() + "; SEMESTER: " + infoCurricularCourseScope.getInfoCurricularSemester().getSemester() + "; CURRICULAR COURSE: " + infoCurricularCourseScope.getInfoCurricularCourse().getName());
		}
	}


	private static void showEnrolmentValidationResultMessages(InfoEnrolmentContext infoEnrolmentContext) {
		System.out.println();
		System.out.println("ENROLMENT VALIDATION MESSAGES:");
		Set keySet = infoEnrolmentContext.getEnrolmentValidationResult().getMessage().keySet();

		Iterator iterator = keySet.iterator();
		int i = 1;
		while (iterator.hasNext()) {
			String msg = (String) iterator.next();
			System.out.println("NUMBER: " + i + "; MESSAGE: " + msg);
			i++;
		}
	}
}