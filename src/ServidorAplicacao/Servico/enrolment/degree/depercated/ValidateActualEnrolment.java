package ServidorAplicacao.Servico.enrolment.degree.depercated;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;

/**
 * @author dcs-rjao
 * 
 * 9/Abr/2003
 */

public class ValidateActualEnrolment implements IService
{

    /**
	 * The actor of this class.
	 */
    public ValidateActualEnrolment()
    {
    }

    /**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
    public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext)
    {

        IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
        EnrolmentContext enrolmentContext =
            EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);
        enrolmentContext.getEnrolmentValidationResult().reset();
        //		IEnrolmentStrategy strategy =
		// enrolmentStrategyFactory.getEnrolmentStrategyInstance(enrolmentContext);
        //		
        //		enrolmentContext = strategy.validateEnrolment();

        return EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
    }
}