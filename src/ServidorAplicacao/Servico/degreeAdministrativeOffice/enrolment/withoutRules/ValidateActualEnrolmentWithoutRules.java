package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.List;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author David Santos
 * 16/Jun/2003
 */

public class ValidateActualEnrolmentWithoutRules implements IServico {

	private static ValidateActualEnrolmentWithoutRules _servico = new ValidateActualEnrolmentWithoutRules();

	public static ValidateActualEnrolmentWithoutRules getService() {
		return _servico;
	}

	private ValidateActualEnrolmentWithoutRules() {
	}

	public final String getNome() {
		return "ValidateActualEnrolmentWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException{
		List currentEnroloments = infoEnrolmentContext.getActualEnrolment();
		if( (currentEnroloments == null) || (currentEnroloments.isEmpty()) ) {
			infoEnrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.NO_CURRICULAR_COURSES_TO_ENROLL);
		}
		return infoEnrolmentContext;
	}
}