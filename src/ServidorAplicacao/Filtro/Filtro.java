package ServidorAplicacao.Filtro;

/**
 * This class is the superclass of all Filter. A Filter class is responsible for ensuring that a given
 * service can be invoked by a certain user or by changing the arguments that should be processed by the
 * service. Filter classes must also define a public static method, called getInstance, that returns an
 * instance of the filter class. This method is called through reflection. If the Filter class does not
 * maintain any state, then this method should return a singleton instance.
 * 
 * @author Joao Pereira
 * @version
 */

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import ServidorAplicacao.IUserView;

abstract public class Filtro extends AccessControlFilter /* implements IFilter */
{
	/**
	 * The preFilter method is called before the service is invoked. It should throw an exception if the
	 * service should not be called
	 * 
	 * @throws FenixServiceException
	 * @throws NotAuthorizedException
	 */
	abstract public void preFiltragem(IUserView requester, Object arguments[]) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(java.lang.Object, java.lang.Object[])
	 */
	//	public void execute(Object requester, Object[] arguments) throws FilterException, Exception
	//	{
	//		preFiltragem((IUserView) requester, arguments);
	//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
	 *      pt.utl.ist.berserk.ServiceResponse)
	 */
	public void execute(ServiceRequest arg0, ServiceResponse arg1) throws FilterException, Exception
	{
		preFiltragem((IUserView) arg0.getRequester(), arg0.getArguments());
	}

}
