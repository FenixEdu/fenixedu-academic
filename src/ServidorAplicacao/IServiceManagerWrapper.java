package ServidorAplicacao;

import java.rmi.RemoteException;
import java.util.HashMap;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.logging.SystemInfo;

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
  public HashMap getMapUsersToWatch(IUserView id);
  public Boolean serviceLoggingIsOn(IUserView id);
  public Boolean userLoggingIsOn(IUserView id);
  public void turnServiceLoggingOn(IUserView id);
  public void turnServiceLoggingOff(IUserView id);
  public void turnUserLoggingOn(IUserView id);
  public void turnUserLoggingOff(IUserView id);
  public void clearServiceLogHistory(IUserView id);
  public void clearUserLogHistory(IUserView id);
  public SystemInfo getSystemInfo(IUserView id);
}
