package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.ShowAvailableCurricularCoursesForOption;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author David Santos
 */

public class ShowAvailableCurricularCoursesForOptionWithRules implements IServico {

	private static ShowAvailableCurricularCoursesForOptionWithRules _servico = new ShowAvailableCurricularCoursesForOptionWithRules();

	public static ShowAvailableCurricularCoursesForOptionWithRules getService() {
		return _servico;
	}

	private ShowAvailableCurricularCoursesForOptionWithRules() {
	}

	public final String getNome() {
		return "ShowAvailableCurricularCoursesForOptionWithRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		ShowAvailableCurricularCoursesForOption service = ShowAvailableCurricularCoursesForOption.getService();
		return service.run(infoEnrolmentContext);
	}
}