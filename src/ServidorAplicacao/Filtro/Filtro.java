package ServidorAplicacao.Filtro;

/**
 * This class is the superclass of all Filter. A Filter class is
 * responsible for ensuring that a given service can be invoked by a
 * certain user or by changing the arguments that should be processed
 * by the service.
 * Filter classes must also define a public static method, called
 * getInstance, that returns an instance of the filter class. This 
 * method is called through reflection. If the Filter class does not
 * maintain any state, then this method should return a singleton
 * instance.
 *
 * @author Joao Pereira
 * @version
 **/

import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;

abstract public class Filtro {
	private static Filtro instance;

	/**
	 * The preFilter method is called before the service is
	 * invoked. It should throw an exception if the service should
	 * not be called
	 *
	 * @throws FenixServiceException
	 * @throws NotAuthorizedException
	 **/
	abstract public void preFiltragem(
		IUserView id,
		IServico servico,
		Object argumentos[])
		throws Exception;
}
