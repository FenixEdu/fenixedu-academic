package ServidorAplicacao.Servico.enrolment.degree;

import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.IEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ShowAvailableDegreesForOption implements IServico {

	private static ShowAvailableDegreesForOption _servico = new ShowAvailableDegreesForOption();
	/**
	 * The singleton access method of this class.
	 **/
	public static ShowAvailableDegreesForOption getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ShowAvailableDegreesForOption() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ShowAvailableDegreesForOption";
	}

	/**
	 * @return InfoEnrolmentContext
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {

		try{
			IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
			IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
			return EnrolmentContextManager.getInfoEnrolmentContext(strategy.getDegreesForOptionalCurricularCourses());
		} catch(IllegalStateException ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		}
	}
}