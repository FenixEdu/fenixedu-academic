/*
 * LerAulasDeSalaEmSemestre.java
 *
 * Created on 29 de Outubro de 2002, 15:44
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeSalaEmSemestre.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.RoomKeyAndSemester;
import Dominio.Aula;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ISala;
import Dominio.Sala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeSalaEmSemestre implements IServico {

  private static LerAulasDeSalaEmSemestre _servico = new LerAulasDeSalaEmSemestre();
  /**
   * The singleton access method of this class.
   **/
  public static LerAulasDeSalaEmSemestre getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerAulasDeSalaEmSemestre() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerAulasDeSalaEmSemestre";
  }

  public Object run(RoomKeyAndSemester semestreAndKeySala) {
    List infoAulas = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//      List aulas = sp.getIAulaPersistente().readBySalaEmSemestre(semestreAndKeySala.getNomeSala(),
//                                                            semestreAndKeySala.getSemestre());


	  IAula aulaCriteria = new Aula();
	  IDisciplinaExecucao executionCourseCriteria = new DisciplinaExecucao();
	  executionCourseCriteria.setSemester(semestreAndKeySala.getSemestre());
	  
	  ISala roomCriteria = new Sala();
	  roomCriteria.setNome(semestreAndKeySala.getNomeSala());
	  
	  aulaCriteria.setDisciplinaExecucao(executionCourseCriteria);
	  aulaCriteria.setSala(roomCriteria);
	  
	  List aulas = sp.getIAulaPersistente().readByCriteria(aulaCriteria);

      Iterator iterator = aulas.iterator();
      infoAulas = new ArrayList();
      while(iterator.hasNext()) {
      	IAula elem = (IAula)iterator.next();
      	InfoRoom infoSala = new InfoRoom(elem.getSala().getNome(), elem.getSala().getEdificio(),
      	                                 elem.getSala().getPiso(), elem.getSala().getTipo(),
      	                                 elem.getSala().getCapacidadeNormal(),
      	                                 elem.getSala().getCapacidadeExame());
        InfoDegree infoLicenciatura = new InfoDegree(elem.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla(),
                                                                 elem.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome());
        InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(elem.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo(),
                                                                                         infoLicenciatura);
		InfoExecutionCourse infoDisciplinaExecucao = new InfoExecutionCourse(elem.getDisciplinaExecucao().getNome(),
		                                                                           elem.getDisciplinaExecucao().getSigla(),
		                                                                           elem.getDisciplinaExecucao().getPrograma(),
		                                                                           infoLicenciaturaExecucao,elem.getDisciplinaExecucao().getTheoreticalHours(),
		                                                                           elem.getDisciplinaExecucao().getPraticalHours(),elem.getDisciplinaExecucao().getTheoPratHours(),
		                                                                           elem.getDisciplinaExecucao().getLabHours());
        infoAulas.add(new InfoLesson(elem.getDiaSemana(), elem.getInicio(), elem.getFim(),
                                   elem.getTipo(), infoSala, infoDisciplinaExecucao));
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return infoAulas;
  }

}