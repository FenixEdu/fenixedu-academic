package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.List;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.ReadAllExecutionPeriods;

/**
 * @author David Santos
 * 10/Jun/2003
 */

public class ReadAllExecutionPeriodsForEnrollment implements IServico {

	private static ReadAllExecutionPeriodsForEnrollment _servico = new ReadAllExecutionPeriodsForEnrollment();

	public static ReadAllExecutionPeriodsForEnrollment getService() {
		return _servico;
	}

	private ReadAllExecutionPeriodsForEnrollment() {
	}

	public final String getNome() {
		return "ReadAllExecutionPeriodsForEnrollment";
	}

	public List run() throws FenixServiceException {
		return ReadAllExecutionPeriods.getService().run();
	}
}