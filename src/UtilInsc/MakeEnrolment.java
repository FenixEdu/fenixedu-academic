package UtilInsc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author David Santos
 */

public class MakeEnrolment {

	private static GestorServicos gestor = GestorServicos.manager();
	private static IUserView userView = null;

	public static void main(String[] args) {

		InfoEnrolmentContext infoEnrolmentContext = null;
		autentication();
//		userView = MakeEnrolmentIO.interactiveAutentication();
		while(true) {
			generateEnrolmentProssess(infoEnrolmentContext, true, true);
		}
	}

	private static void autentication() {
		String argsAutenticacao[] = {"stdnt", "pass", Autenticacao.EXTRANET};
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

	private static void generateEnrolmentProssess(InfoEnrolmentContext infoEnrolmentContext, boolean withValidation, boolean withConfirm) {
		Object serviceArgs1[] = {userView};
		infoEnrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs1);
		MakeEnrolmentIO.showFinalSpan(infoEnrolmentContext);
		boolean remove = false;
		
		MakeEnrolmentIO.selectNormalCurricularCoursesToEnroll(infoEnrolmentContext);

		final List actualEnrolments = infoEnrolmentContext.getActualEnrolment();

		List optionalCurricularCoursesScopesEnrolled = (List) CollectionUtils.select(actualEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) obj;
				return infoCurricularCourseScope.getInfoCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE));
			}
		});

		if(!optionalCurricularCoursesScopesEnrolled.isEmpty()) {
			Iterator iterator = optionalCurricularCoursesScopesEnrolled.iterator();
			while(iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoCurricularCourseScope);
				infoEnrolmentContext.getActualEnrolment().remove(infoCurricularCourseScope);

				Object serviceArgs2[] = { infoEnrolmentContext };
				infoEnrolmentContext = executeService("ShowAvailableDegreesForOption", serviceArgs2);
				MakeEnrolmentIO.showAvailableDegreesForOption(infoEnrolmentContext);

				MakeEnrolmentIO.selectDegreeForOptionalCurricularCourseToEnroll(infoEnrolmentContext);
				
				if(infoEnrolmentContext.getChosenOptionalInfoDegree() != null) {
					Object serviceArgs3[] = { infoEnrolmentContext };
					infoEnrolmentContext = executeService("ShowAvailableCurricularCoursesForOption", serviceArgs3);
					MakeEnrolmentIO.showAvailableCurricularCoursesForOption(infoEnrolmentContext);

					Object serviceArgs4[] = { infoEnrolmentContext, MakeEnrolmentIO.selectOptionalCurricularCourseToEnroll(infoEnrolmentContext) };
					infoEnrolmentContext = executeService("SelectOptionalCurricularCourse", serviceArgs4);
				} else {
					remove = true;
				}
			}
		}
		if(!optionalCurricularCoursesScopesEnrolled.isEmpty() && remove) {
			Iterator iterator = optionalCurricularCoursesScopesEnrolled.iterator();
			List aux = new ArrayList();
			while(iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				infoEnrolmentContext.getActualEnrolment().remove(infoCurricularCourseScope);
				Iterator iterator2 = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().iterator();
				while(iterator2.hasNext()) {
					InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) iterator2.next();
					if(infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope().getInfoCurricularCourse().equals(infoCurricularCourseScope.getInfoCurricularCourse())) {
						aux.add(infoEnrolmentInOptionalCurricularCourse);
					}
				}
			}
			infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().removeAll(aux);
		}
		MakeEnrolmentIO.showActualEnrolments(infoEnrolmentContext);
		MakeEnrolmentIO.showChosenCurricularCoursesForOptionalCurricularCourses(infoEnrolmentContext);

		if(withValidation) {
			Object serviceArgs5[] = {infoEnrolmentContext};
			infoEnrolmentContext = executeService("ValidateActualEnrolment", serviceArgs5);
		}
		if(withConfirm) {
			Object serviceArgs6[] = {infoEnrolmentContext};
			infoEnrolmentContext = executeService("ConfirmActualEnrolment", serviceArgs6);
		}
		MakeEnrolmentIO.showEnrolmentValidationResultMessages(infoEnrolmentContext);
	}

	private static void secureEnrolmentProssess() {
		Object serviceArgs[] = {userView};
		executeService("ChangeEnrolmentStateFromTemporarilyToEnroled", serviceArgs);
	}
}