/*
 * LerAula.java
 *
 * Created on 27 de Outubro de 2002, 21:18
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAula.
 *
 * @author tfc130
 **/
import DataBeans.InfoLesson;
import DataBeans.KeyLesson;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAula implements IServico {

  private static LerAula _servico = new LerAula();
  /**
   * The singleton access method of this class.
   **/
  public static LerAula getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerAula() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerAula";
  }

  public Object run(KeyLesson keyAula) {

    InfoLesson infoAula = null;


    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      ISala sala = sp.getISalaPersistente().readByName(keyAula.getKeySala().getNomeSala());
      IAula aula1 = sp.getIAulaPersistente().readByDiaSemanaAndInicioAndFimAndSala(keyAula.getDiaSemana(),
                    keyAula.getInicio(), keyAula.getFim(), sala);
      if (aula1 != null) {
      	infoAula = Cloner.copyILesson2InfoLesson(aula1);
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return infoAula;
  }

}