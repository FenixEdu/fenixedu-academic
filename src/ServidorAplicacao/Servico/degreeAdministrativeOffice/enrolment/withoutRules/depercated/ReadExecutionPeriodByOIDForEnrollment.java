package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules.depercated;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.manager.ReadExecutionPeriod;

/**
 * @author David Santos
 * 10/Jun/2003
 */

public class ReadExecutionPeriodByOIDForEnrollment implements IServico {

	private static ReadExecutionPeriodByOIDForEnrollment _servico = new ReadExecutionPeriodByOIDForEnrollment();

	public static ReadExecutionPeriodByOIDForEnrollment getService() {
		return _servico;
	}

	private ReadExecutionPeriodByOIDForEnrollment() {
	}

	public final String getNome() {
		return "ReadExecutionPeriodByOIDForEnrollment";
	}

	public InfoExecutionPeriod run(Integer executionPeriodOID) throws FenixServiceException {
		return ReadExecutionPeriod.getService().run(executionPeriodOID);
	}
}