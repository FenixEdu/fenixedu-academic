package ServidorAplicacao.Servico.enrolment.degree;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
public class ShowAvailableCurricularCoursesForOption implements IServico {

	private static ShowAvailableCurricularCoursesForOption _servico = new ShowAvailableCurricularCoursesForOption();
	/**
	 * The singleton access method of this class.
	 **/
	public static ShowAvailableCurricularCoursesForOption getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ShowAvailableCurricularCoursesForOption() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ShowAvailableCurricularCoursesForOption";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return List
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
		IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
		return EnrolmentContextManager.getInfoEnrolmentContext(strategy.getOptionalCurricularCourses());
	}
}