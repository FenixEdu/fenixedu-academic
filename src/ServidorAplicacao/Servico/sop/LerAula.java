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
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.KeyLesson;
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
      ISala sala = sp.getISalaPersistente().readByNome(keyAula.getKeySala().getNomeSala());
      IAula aula1 = sp.getIAulaPersistente().readByDiaSemanaAndInicioAndFimAndSala(keyAula.getDiaSemana(),
                    keyAula.getInicio(), keyAula.getFim(), sala);
      if (aula1 != null) {
      	InfoDegree infoLicenciatura = new InfoDegree(aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla(),
      	                                                         aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome());
      	InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo(),
      	                                                                                 infoLicenciatura);
      	InfoExecutionCourse infoDisciplinaExecucao = new InfoExecutionCourse(aula1.getDisciplinaExecucao().getNome(),
      	                                                                           aula1.getDisciplinaExecucao().getSigla(),
      	                                                                           aula1.getDisciplinaExecucao().getPrograma(),
     	                                                                           infoLicenciaturaExecucao,aula1.getDisciplinaExecucao().getTheoreticalHours(),
     	                                                                           aula1.getDisciplinaExecucao().getPraticalHours(),aula1.getDisciplinaExecucao().getTheoPratHours(),
     	                                                                           aula1.getDisciplinaExecucao().getLabHours());
      	InfoRoom infoSala = new InfoRoom(aula1.getSala().getNome(), aula1.getSala().getEdificio(),
      	                                 aula1.getSala().getPiso(), aula1.getSala().getTipo(),
      	                                 aula1.getSala().getCapacidadeNormal(),
      	                                 aula1.getSala().getCapacidadeExame());
      	infoAula = new InfoLesson(aula1.getDiaSemana(), aula1.getInicio(), aula1.getFim(),
      	                        aula1.getTipo(), infoSala, infoDisciplinaExecucao);
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return infoAula;
  }

}