package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.ValidateActualEnrolment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

public class ValidateActualEnrolmentWithRules implements IServico {

	private static ValidateActualEnrolmentWithRules _servico = new ValidateActualEnrolmentWithRules();

	public static ValidateActualEnrolmentWithRules getService() {
		return _servico;
	}

	private ValidateActualEnrolmentWithRules() {
	}

	public final String getNome() {
		return "ValidateActualEnrolmentWithRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException{
		ValidateActualEnrolment service = ValidateActualEnrolment.getService();
		return service.run(infoEnrolmentContext);
	}
}