package ServidorAplicacao.Servico;

/**
 * Serviço CriarSitio.
 *
 * @author jorge
 * @version
 **/

import Dominio.ISitio;
import ServidorAplicacao.IServico;
import ServidorAplicacao.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class AlterarSitio implements IServico {

  private static AlterarSitio _servico = new AlterarSitio();

  /**
   * The singleton access method of this class.
   **/
  public static AlterarSitio getService() {
    return _servico;
  }

  /**
   * The ctor of this class.
   **/
  private AlterarSitio() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "AlterarSitio";
  }


  public void run(String nome, Integer anoCurricular, Integer semestre,
                     String curso, String departamento)
      throws FenixServiceException, ExcepcaoInexistente {
                        
    ISitio sitio = null;

    ISuportePersistente sp;
    
    try {
      sp = SuportePersistenteOJB.getInstance();
      sitio = sp.getISitioPersistente().readByNome(nome);
    } catch (ExcepcaoPersistencia ex) {
      FenixServiceException newEx = new FenixServiceException("Persistence layer error");
      newEx.fillInStackTrace();
      throw newEx;
    }
    
    if (sitio == null)
      throw new ExcepcaoInexistente("Sitio desconhecido");
      
    try {
      sp.getISitioPersistente().lockWrite(sitio);
    } catch (ExcepcaoPersistencia ex) {
      FenixServiceException newEx = new FenixServiceException("Persistence layer error");
      newEx.fillInStackTrace();
      throw newEx;
    }

    sitio.setAnoCurricular(anoCurricular.intValue());
    sitio.setSemestre(semestre.intValue());
    sitio.setCurso(curso);
    sitio.setDepartamento(departamento);
  }
}