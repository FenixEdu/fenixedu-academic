package ServidorAplicacao.Servico.enrolment.degree;

import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */

public class ValidateActualEnrolment implements IServico {

	private static ValidateActualEnrolment _servico = new ValidateActualEnrolment();
	/**
	 * The singleton access method of this class.
	 **/
	public static ValidateActualEnrolment getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ValidateActualEnrolment() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ValidateActualEnrolment";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext){

		IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
		EnrolmentContext enrolmentContext = EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);
		enrolmentContext.getEnrolmentValidationResult().reset();
		IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(enrolmentContext);
		
		enrolmentContext = strategy.validateEnrolment();

		return EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
	}
}