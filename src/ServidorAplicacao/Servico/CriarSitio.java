package ServidorAplicacao.Servico;

/**
 * Serviço CriarSitio.
 *
 * @author jorge
 * @version
 **/

import Dominio.ISitio;
import Dominio.Sitio;
import ServidorAplicacao.IServico;
import ServidorAplicacao.NotExecutedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarSitio implements IServico {

  private static CriarSitio _servico = new CriarSitio();

  /**
   * The singleton access method of this class.
   **/
  public static CriarSitio getService() {
    return _servico;
  }

  /**
   * The ctor of this class.
   **/
  private CriarSitio() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "CriarSitio";
  }


  public void run(String nome, Integer anoCurricular, Integer semestre,
                     String curso, String departamento) throws NotExecutedException {
                        
    ISitio sitio = new Sitio(nome, anoCurricular.intValue(), 
                             semestre.intValue(), curso, departamento);

    try {
      SuportePersistenteOJB.getInstance().getISitioPersistente().lockWrite(sitio);
    } catch (ExcepcaoPersistencia ex) {
      NotExecutedException newEx = new NotExecutedException("Persistence layer error");
      newEx.fillInStackTrace();
      throw newEx;
    }
  }
}