/*
 * ApagarAula.java
 *
 * Created on 27 de Outubro de 2002, 14:30
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o ApagarAula.
 *
 * @author tfc130
 **/
import DataBeans.KeyLesson;
import Dominio.IAula;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ApagarAula implements IServico {

  private static ApagarAula _servico = new ApagarAula();
  /**
   * The singleton access method of this class.
   **/
  public static ApagarAula getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ApagarAula() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ApagarAula";
  }

  public Object run(KeyLesson keyAula) {

    IAula aula1 = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();

	  ISala sala = sp.getISalaPersistente().readByNome(keyAula.getKeySala().getNomeSala());
      aula1 = sp.getIAulaPersistente().readByDiaSemanaAndInicioAndFimAndSala(keyAula.getDiaSemana(),
                    keyAula.getInicio(), keyAula.getFim(), sala);
      
      if (aula1 != null) {
          sp.getIAulaPersistente().delete(aula1);
          result = true;
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }

}