package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
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

	public InfoEnrolmentContext run(IUserView userView) throws FenixServiceException {
		ShowAvailableCurricularCourses service = ShowAvailableCurricularCourses.getService();
		return service.run(userView);
	}
}