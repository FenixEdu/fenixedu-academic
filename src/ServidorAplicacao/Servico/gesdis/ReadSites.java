package ServidorAplicacao.Servico.gesdis;

/**
 * Serviço ObterSitios.
 *
 * @author Joao Pereira
 * @author Ivo Brandão
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadSites implements IServico {

  private static ReadSites service = new ReadSites();

  /**
   * The singleton access method of this class.
   */
  public static ReadSites getService() {
    return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadSites() { }

  /**
   * Service name
   */
  public final String getNome() {
    return "ReadSites";
  }

  /**
   * Executes the service. Returns the current collection of infosites .
   */
  public List run() throws FenixServiceException {
    ISuportePersistente sp;
    List allSites = null;

	try {
    	sp = SuportePersistenteOJB.getInstance();
    	allSites = sp.getIPersistentSite().readAll();
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

    if (allSites == null || allSites.isEmpty()) throw new InvalidArgumentsServiceException();

    // build the result of this service
    Iterator iterator = allSites.iterator();
    List result = new ArrayList(allSites.size());
    
    while (iterator.hasNext())
		result.add( Cloner.copyISite2InfoSite((ISite) iterator.next()) );

    return result;
  }
}
