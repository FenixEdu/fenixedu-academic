package ServidorAplicacao.Filtro;

/**
 * This class is responsible for verifying if a given user has the
 * authorization to run a service with certain attributes.
 *
 * @author Nuno Nunes & Joana Mota
 * @version
 **/

import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.NotAuthorizedException;
import ServidorAplicacao.NotExecutedException;
import ServidorAplicacao.Servico.UserView;

public class CandidateAuthorizationFilter extends Filtro {
  
  // the singleton of this class
  public final static CandidateAuthorizationFilter autorizacao = new CandidateAuthorizationFilter();

  /**
   * The singleton access method of this class.
   *
   * @return Returns the instance of this class responsible for the
   * authorization access to services.
   **/
  public static CandidateAuthorizationFilter getInstance() {
    return autorizacao;
  }

  /**
   * Checks if the user has the priviligies to execute the service
   * with the given attributes.
   *
   * @see ServidorAplicacao.Filtro.Filtro#prefilter
   **/
  public void preFiltragem(IUserView id, IServico servico, Object argumentos[])
    throws NotExecutedException, NotAuthorizedException {
    
    System.out.println("CandidateAuthorizationFilter::preFiltering() invoked");
    if (id == null || !(id instanceof UserView))
      throw new NotExecutedException("Invalid user ID");
    

  }
}