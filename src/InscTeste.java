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

			Object serviceArgs2[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ValidateActualEnrolment", serviceArgs2);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println(infoEnrolmentContext.getValidateMessage());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}