package ServidorAplicacao.Servico.enrolment.degree;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ShowAvailableCurricularCoursesForOption implements IService {

	

	/**
	 * The actor of this class.
	 **/
	public ShowAvailableCurricularCoursesForOption() {
	}

	

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return List
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) {
		IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
//		IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
//		return EnrolmentContextManager.getInfoEnrolmentContext(strategy.getOptionalCurricularCourses());
		return null;
	}
}