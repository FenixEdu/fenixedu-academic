package ServidorAplicacao.Servico;

/**
 * Serviço ObterSitios. Obtem a lista de sitios ja' criados.
 *
 * @author Joao Pereira
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ISitio;
import ServidorAplicacao.IServico;
import ServidorAplicacao.NotExecutedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ObterSitios implements IServico {

  private static ObterSitios _servico = new ObterSitios();

  /**
   * The singleton access method of this class.
   **/
  public static ObterSitios getService() {
    return _servico;
  }

  /**
   * The ctor of this class.
   **/
  private ObterSitios() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ObterSitios";
  }


  /**
   * Executes the service. Returns the current collection of 
   * sitios names.
   *
   * @throws ExcepcaoInexistente is there is none sitio.
   **/
  public List run() throws NotExecutedException, ExcepcaoInexistente {
    ISuportePersistente sp;
    List allSitios = null;

    try {
      sp = SuportePersistenteOJB.getInstance();
      allSitios = sp.getISitioPersistente().readAll();
    } catch (ExcepcaoPersistencia ex) {
      NotExecutedException newEx = new NotExecutedException("Persistence layer error");
      newEx.fillInStackTrace();
      throw newEx;
    }
    
    if (allSitios == null || allSitios.isEmpty())
      throw new ExcepcaoInexistente("Nao ha sitios");
    
    // build the result of this service
    Iterator iter = allSitios.iterator();
    List result = new ArrayList(allSitios.size());
    
    while (iter.hasNext())
      result.add(((ISitio)iter.next()).getNome());

    return result;
  }
}
