package ServidorAplicacao.Servico;

/**
 * Serviço ObterSitio. Obtem a informacao sobre um sitio.
 *
 * @author Joao Pereira
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ISeccao;
import Dominio.ISitio;
import ServidorAplicacao.IServico;
import ServidorAplicacao.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ObterSitio implements IServico {

  private static ObterSitio _servico = new ObterSitio();

  /**
   * The singleton access method of this class.
   **/
  public static ObterSitio getService() {
    return _servico;
  }

  /**
   * The ctor of this class.
   **/
  private ObterSitio() {
  }

  /**
   * Returns the name of this service.
   **/
  public final String getNome() {
    return "ObterSitio";
  }

  /**
   * Executes the service. Returns the all information about the
   * desired sitio.
   *
   * @param name is the name of the desired sitio.
   *
   * @throws ExcepcaoInexistente if there is none sitio with the
   * desired name.
   **/
  public SitioView run(String name)
    throws FenixServiceException, ExcepcaoInexistente {
    ISitio sitio = null;
    ISuportePersistente sp;

    try {
      sp = SuportePersistenteOJB.getInstance();
      sitio = sp.getISitioPersistente().readByNome(name);
    } catch (ExcepcaoPersistencia ex) {
      FenixServiceException newEx =
        new FenixServiceException("Persistence layer error");
      newEx.fillInStackTrace();
      throw newEx;
    }

    if (sitio == null)
      throw new ExcepcaoInexistente("Sitio desconhecido");

    // the list that contains the names of all seccoes of the sitio
    List names = new ArrayList();
    
    if (sitio.getSeccoes() != null && !sitio.getSeccoes().isEmpty()) {
      Iterator iter = sitio.getSeccoes().iterator();

      
      while (iter.hasNext())
        names.add(((ISeccao) iter.next()).getNome());
    }

    String seccaoInicial = null;
    if (sitio.getSeccaoInicial() != null)
      seccaoInicial = sitio.getSeccaoInicial().getNome();

    return new SitioView(sitio.getNome(), sitio.getAnoCurricular(),
                          sitio.getSemestre(), sitio.getCurso(),
                          sitio.getDepartamento(), names, seccaoInicial);
  }
}
