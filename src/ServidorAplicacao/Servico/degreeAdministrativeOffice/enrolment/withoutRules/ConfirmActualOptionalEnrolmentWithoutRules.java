package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.ConfirmActualEnrolment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;


/**
 * @author David Santos
 */

public class ConfirmActualOptionalEnrolmentWithoutRules implements IServico {

	private static ConfirmActualOptionalEnrolmentWithoutRules _servico = new ConfirmActualOptionalEnrolmentWithoutRules();

	public static ConfirmActualOptionalEnrolmentWithoutRules getService() {
		return _servico;
	}

	private ConfirmActualOptionalEnrolmentWithoutRules() {
	}

	public final String getNome() {
		return "ConfirmActualOptionalEnrolmentWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		ConfirmActualEnrolment service = ConfirmActualEnrolment.getService();
		return service.run(infoEnrolmentContext);
	}
}