import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoCurricularCourseScope;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author David Santos
 */

public class InscTeste2 {

	private static GestorServicos gestor = GestorServicos.manager();
	private static IUserView userView = new UserView("user", null);

	public static void main(String[] args) {

		InfoEnrolmentContext infoEnrolmentContext = null;
		autentication();
		generateEnrolmentProssess(infoEnrolmentContext, true, true);
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

	private static void generateEnrolmentProssess(InfoEnrolmentContext infoEnrolmentContext, boolean withValidation, boolean withConfirm) {
		Object serviceArgs1[] = {userView};
		infoEnrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs1);
		InscTesteIO.showFinalSpan(infoEnrolmentContext);
		
		InscTesteIO.selectNormalCurricularCoursesToEnroll(infoEnrolmentContext);

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
				InscTesteIO.showAvailableDegreesForOption(infoEnrolmentContext);

				InscTesteIO.selectDegreeForOptionalCurricularCourseToEnroll(infoEnrolmentContext);

				Object serviceArgs3[] = { infoEnrolmentContext };
				infoEnrolmentContext = executeService("ShowAvailableCurricularCoursesForOption", serviceArgs3);
				InscTesteIO.showAvailableCurricularCoursesForOption(infoEnrolmentContext);

				Object serviceArgs4[] = { infoEnrolmentContext, InscTesteIO.selectOptionalCurricularCourseToEnroll(infoEnrolmentContext) };
				infoEnrolmentContext = executeService("SelectOptionalCurricularCourse", serviceArgs4);
			}
		}
		InscTesteIO.showActualEnrolments(infoEnrolmentContext);
		InscTesteIO.showChosenCurricularCoursesForOptionalCurricularCourses(infoEnrolmentContext);

		if(withValidation) {
			Object serviceArgs5[] = {infoEnrolmentContext};
			infoEnrolmentContext = executeService("ValidateActualEnrolment", serviceArgs5);
		}
		if(withConfirm) {
			Object serviceArgs6[] = {infoEnrolmentContext};
			infoEnrolmentContext = executeService("ConfirmActualEnrolment", serviceArgs6);
		}
		InscTesteIO.showEnrolmentValidationResultMessages(infoEnrolmentContext);
	}

	private static void secureEnrolmentProssess() {
		Object serviceArgs[] = {userView};
		executeService("ChangeEnrolmentStateFromTemporarilyToEnroled", serviceArgs);
	}
}