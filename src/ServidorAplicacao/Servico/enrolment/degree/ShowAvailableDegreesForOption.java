package ServidorAplicacao.Servico.enrolment.degree;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;

/**
 * @author dcs-rjao
 * 
 * 9/Abr/2003
 */
public class ShowAvailableDegreesForOption implements IService
{

    /**
	 * The actor of this class.
	 */
    public ShowAvailableDegreesForOption()
    {
    }

    /**
	 * @return InfoEnrolmentContext
	 * @throws FenixServiceException
	 */
    public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext)
        throws FenixServiceException
    {

        try
        {
            IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
            //			IEnrolmentStrategy strategy =
			// enrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
            //			return
			// EnrolmentContextManager.getInfoEnrolmentContext(strategy.getDegreesForOptionalCurricularCourses());
            return null;
        }
        catch (IllegalStateException ex)
        {
            ex.printStackTrace(System.out);
            throw new FenixServiceException(ex);
        }
    }
}