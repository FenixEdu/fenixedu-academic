package ServidorAplicacao.Servico;

/**
 * ObterSeccao service. It is used to get all information about a
 * seccao. A seccao is represented by its name, the sitio it is
 * associated with and its parent seccao.
 *
 * @author Joao Pereira
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.IItem;
import Dominio.ISeccao;
import Dominio.ISitio;
import ServidorAplicacao.IServico;
import ServidorAplicacao.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ObterSeccao implements IServico {

  private static ObterSeccao _servico = new ObterSeccao();

  /**
   * The singleton access method of this class.
   **/
  public static ObterSeccao getService() {
    return _servico;
  }

  /**
   * The ctor of this class.
   **/
  private ObterSeccao() {
  }

  /**
   * Returns the name of this service.
   **/
  public final String getNome() {
    return "ObterSeccao";
  }

  /**
   * Executes the service. Returns the all information about the
   * desired seccao.
   *
   * @param sitioName is the name of the sitio.
   * @param name is the name of the desired seccao.
   *
   * @throws ExcepcaoInexistente if there is none seccao with the
   * desired name, parent and sitio.
   **/
  public SeccaoView run(String sitioName, String name)
    throws FenixServiceException, ExcepcaoInexistente {
    ISeccao seccao = null;
    ISuportePersistente sp;
    ISitio sitio;

    try {
      sp = SuportePersistenteOJB.getInstance();
      sitio = sp.getISitioPersistente().readByNome(sitioName);
      seccao =
        sp.getISeccaoPersistente().readBySitioAndSeccaoAndNome(
          sitio,
          null,
          name);
    } catch (ExcepcaoPersistencia ex) {
      FenixServiceException newEx =
        new FenixServiceException("Persistence layer error");
      newEx.fillInStackTrace();
      throw newEx;
    }

    if (seccao == null)
      throw new ExcepcaoInexistente("Seccao desconhecido");

    List subSeccoes = new ArrayList();
/*    if (seccao.getSeccoesInferiores() != null
      && !seccao.getSeccoesInferiores().isEmpty()) {
      Iterator iter = seccao.getSeccoesInferiores().iterator();

      while (iter.hasNext())
        subSeccoes.add(((ISeccao) iter.next()).getNome());
    }
*/

    List itens = new ArrayList();
    if (seccao.getItens() != null
      && !seccao.getItens().isEmpty()) {
      Iterator iter = seccao.getItens().iterator();

      while (iter.hasNext()) {
        IItem item = (IItem) iter.next();
        itens.add(new ItemView(item.getNome(), item.getInformacao(), item.getUrgente()));
      }
    }
      
    return new SeccaoView(seccao.getNome(), seccao.getOrdem(),
                           sitio.getNome(), 
                           /*seccao.getSeccaoSuperior() != */null/* ? seccao.getSeccaoSuperior().getNome():null*/,
                           subSeccoes, itens);
  }
}
