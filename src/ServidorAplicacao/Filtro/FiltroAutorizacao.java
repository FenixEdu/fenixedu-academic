package ServidorAplicacao.Filtro;

/**
 * This class is responsible for verifying if a given user has the
 * authorization to run a service with certain attributes.
 *
 * @author Joao Pereira
 * @version
 **/

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

public class FiltroAutorizacao extends Filtro {
  
  // the singleton of this class
  public final static FiltroAutorizacao autorizacao = new FiltroAutorizacao();

  /**
   * The singleton access method of this class.
   *
   * @return Returns the instance of this class responsible for the
   * authorization access to services.
   **/
  public static FiltroAutorizacao getInstance() {
    return autorizacao;
  }

  /**
   * Checks if the user has the priviligies to execute the service
   * with the given attributes.
   *
   * @see ServidorAplicacao.Filtro.Filtro#prefilter
   **/
  public void preFiltragem(IUserView id, Object argumentos[])
    throws FenixServiceException, NotAuthorizedException {
   
    
    System.out.println("FiltroAutorizacao::preFiltragem() invocado");
    if (id == null || !(id instanceof UserView)){
      throw new FenixServiceException("Invalid user ID");
    }
    
//    UserView userView = (UserView) id;
//    Set privilegios = userView.getPrivilegios();
//    if (privilegios != null) {
//      result = privilegios.contains(servico.getNome());
//    }
////      System.out.println(result);
//    if (!result){
//      throw new NotAuthorizedException("User " + userView.getUtilizador() +
//				       " does not have privileges to run service " +
//				       servico.getNome());
//    }
  }
}