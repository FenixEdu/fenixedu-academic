import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
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

			//			FIXME: David-Ricardo: ler o semestre do execution Period quando este tiver esta informacao
			Object serviceArgs1[] = {userView, new Integer(2)};
			result = gestor.executar(userView, "ShowAvailableCurricularCourses", serviceArgs1);
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("TO CHOOSE (FINAL SPAN):");
			System.out.println(infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled());
			System.out.println("AUTOMATIC CHOOSEN (ANUALS + ALTERNATIVES):");
			System.out.println(infoEnrolmentContext.getInfoCurricularCoursesScopesEnroledByStudent());

			Object serviceArgs2[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ShowAvailableDegreesForOption", serviceArgs2);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nAVAILABLE DEGREES FOR OPTION1:");
			System.out.println(infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses());
			
			infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(0));
			Object serviceArgs3[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ShowAvailableCurricularCoursesForOption", serviceArgs3);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nAVAILABLE COURSES FOR OPTION1:");
			System.out.println(infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree());
			
			infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope( ((InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(5)));
			Object serviceArgs4[] = {infoEnrolmentContext,(InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(0) };
			result = gestor.executar(userView, "SelectOptionalCurricularCourse", serviceArgs4);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nCHOSEN COURSE AND EQUIVALENCE:");
			System.out.println(infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments());

			Object serviceArgs5[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ShowAvailableDegreesForOption", serviceArgs5);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nAVAILABLE DEGREES FOR OPTION2:");
			System.out.println(infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses());
			
			infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(0));
			Object serviceArgs6[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ShowAvailableCurricularCoursesForOption", serviceArgs6);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nAVAILABLE COURSES FOR OPTION2:");
			System.out.println(infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree());

			infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope( ((InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(5)));
			Object serviceArgs7[] = {infoEnrolmentContext,(InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(1)};
			result = gestor.executar(userView, "SelectOptionalCurricularCourse", serviceArgs7);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nCHOSEN COURSE AND EQUIVALENCE:");
			System.out.println(infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments());

			Object serviceArgs8[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ShowAvailableDegreesForOption", serviceArgs8);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nAVAILABLE DEGREES FOR OPTION3:");
			System.out.println(infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses());
			
			infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().get(0));
			Object serviceArgs9[] = {infoEnrolmentContext};
			result = gestor.executar(userView, "ShowAvailableCurricularCoursesForOption", serviceArgs9);
			infoEnrolmentContext = (InfoEnrolmentContext) result;
			System.out.println("\nAVAILABLE COURSES FOR OPTION3:");
			System.out.println(infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree());

//			for(int i = 0; i < 8; i++) {
//				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
//				infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			}

//			infoEnrolmentContext.getActualEnrolment().addAll(infoEnrolmentContext.getInfoCurricularCoursesScopesEnroledByStudent());
//
//
//			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(0);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(1);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(2);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(3);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(4);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
//			infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(5);
//			infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);


			System.out.println("\nACTUAL ENROLMENTS:");
			System.out.println(infoEnrolmentContext.getActualEnrolment());

//			Object serviceArgs100[] = {infoEnrolmentContext};
//			result = gestor.executar(userView, "ValidateActualEnrolment", serviceArgs100);
//			infoEnrolmentContext = (InfoEnrolmentContext) result;
//			System.out.println(infoEnrolmentContext.getEnrolmentValidationResult().getMessage());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}