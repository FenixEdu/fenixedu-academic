package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import DataBeans.InfoStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.enrolment.degree.ShowAvailableCurricularCourses;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author David Santos
 */

public class ShowAvailableCurricularCoursesWithRules implements IServico {

	private static ShowAvailableCurricularCoursesWithRules _servico = new ShowAvailableCurricularCoursesWithRules();

	public static ShowAvailableCurricularCoursesWithRules getService() {
		return _servico;
	}

	private ShowAvailableCurricularCoursesWithRules() {
	}

	public final String getNome() {
		return "ShowAvailableCurricularCoursesWithRules";
	}

	public InfoEnrolmentContext run(InfoStudent infoStudent) throws FenixServiceException {
		IUserView userView = new UserView(infoStudent.getInfoPerson().getUsername(), null);
		ShowAvailableCurricularCourses service = ShowAvailableCurricularCourses.getService();
		return service.run(userView);
	}
}