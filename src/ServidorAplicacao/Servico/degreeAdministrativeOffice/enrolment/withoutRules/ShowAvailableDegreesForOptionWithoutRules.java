package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalDegreesRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */

public class ShowAvailableDegreesForOptionWithoutRules implements IServico {

	private static ShowAvailableDegreesForOptionWithoutRules _servico = new ShowAvailableDegreesForOptionWithoutRules();

	public static ShowAvailableDegreesForOptionWithoutRules getService() {
		return _servico;
	}

	private ShowAvailableDegreesForOptionWithoutRules() {
	}

	public final String getNome() {
		return "ShowAvailableDegreesForOptionWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {

		IEnrolmentRule enrolmentRule = new EnrolmentFilterAllOptionalDegreesRule();
		EnrolmentContext enrolmentContext = enrolmentRule.apply(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
		InfoEnrolmentContext infoEnrolmentContext2 = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
		// TODO DAVID-RICARDO: Falta filtrar a lista de cursos pelos cursos execução.
		return infoEnrolmentContext2;
	}
}