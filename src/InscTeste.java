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

	public static void main(String[] args) {

		GestorServicos gestor = null;
		IUserView userView = new UserView("user", null);

		gestor = GestorServicos.manager();
		String argsAutenticacao[] = {"user", "pass"};
		Object result = null;

		try {
			userView = (IUserView) gestor.executar(null, "Autenticacao", argsAutenticacao);

			Object serviceArgs1[] = {userView, new Integer(1)};
			result = gestor.executar(userView, "ShowAvailableCurricularCourses", serviceArgs1);
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println(infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());


//			for(int i = 0; i < 6; i++) {
//				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
//				infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			}

//			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(0);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(1);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(2);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(3);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(4);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(5);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);

//			Object serviceArgs2[] = {infoEnrolmentContext};
//			result = gestor.executar(userView, "ValidateActualEnrolment", serviceArgs2);
//			infoEnrolmentContext = (InfoEnrolmentContext) result;
//			System.out.println(infoEnrolmentContext.getEnrolmentValidationResult().getMessage());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}