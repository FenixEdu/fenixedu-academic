package ServidorAplicacao.Servico.degreeAdministrativeOffice;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.ShowAvailableDegreesForOption;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;

public class ShowAvailableDegreesForOptionWithRules implements IServico {

	private static ShowAvailableDegreesForOptionWithRules _servico = new ShowAvailableDegreesForOptionWithRules();

	public static ShowAvailableDegreesForOptionWithRules getService() {
		return _servico;
	}

	private ShowAvailableDegreesForOptionWithRules() {
	}

	public final String getNome() {
		return "ShowAvailableDegreesForOptionWithRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		ShowAvailableDegreesForOption service = ShowAvailableDegreesForOption.getService();
		return service.run(infoEnrolmentContext);
	}
}