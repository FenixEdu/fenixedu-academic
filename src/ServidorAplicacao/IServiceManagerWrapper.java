package ServidorAplicacao;

import java.rmi.RemoteException;
import java.util.HashMap;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * The business interface for the service manager seassion bean.
 * *
 * @author Luis Cruz
 * @version
 **/

public interface IServiceManagerWrapper {

  /**
   * Executes a given service.
   *
   * @param id represents the identification of the entity that
   *        desires to run the service
   *
   * @param service is a string containing the name of the service to execute
   *
   * @param argumentos is a vector with the arguments of the service
   * to execute.
   *
   * @throws FenixServiceException
   * @throws NotAuthorizedException
   **/
  public Object execute(IUserView id, String service, Object argumentos[])
      throws FenixServiceException, RemoteException;

  public Object execute(IUserView id, String service, String methods, Object argumentos[])
		throws FenixServiceException, RemoteException;

  public HashMap getMapServicesToWatch(IUserView id);
  public Boolean loggingIsOn(IUserView id);
  public void turnLoggingOn(IUserView id);
  public void turnLoggingOff(IUserView id);
  public void clearLogHistory(IUserView id);

}
