package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.ConfirmActualEnrolment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;


/**
 * @author David Santos
 */

public class ConfirmActualEnrolmentWithRules implements IServico {

	private static ConfirmActualEnrolmentWithRules _servico = new ConfirmActualEnrolmentWithRules();

	public static ConfirmActualEnrolmentWithRules getService() {
		return _servico;
	}

	private ConfirmActualEnrolmentWithRules() {
	}

	public final String getNome() {
		return "ConfirmActualEnrolmentWithRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		ConfirmActualEnrolment service = ConfirmActualEnrolment.getService();
		return service.run(infoEnrolmentContext);
	}
}