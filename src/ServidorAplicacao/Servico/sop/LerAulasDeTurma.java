/*
 * LerAulasDeTurma.java
 *
 * Created on 29 de Outubro de 2002, 22:18
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeTurma
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ClassKey;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import Dominio.IAula;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeTurma implements IServico {

  private static LerAulasDeTurma _servico = new LerAulasDeTurma();
  /**
   * The singleton access method of this class.
   **/
  public static LerAulasDeTurma getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerAulasDeTurma() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerAulasDeTurma";
  }

  public Object run(ClassKey keyTurma) {
    ArrayList infoAulas = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      List aulas = sp.getITurmaTurnoPersistente().readAulasByTurma(keyTurma.getNomeTurma());

      Iterator iterator = aulas.iterator();
      infoAulas = new ArrayList();
      	System.out.println(aulas.size());
      int i =1;
      while(iterator.hasNext()) {
      		System.out.println(i++);
      
      	IAula elem = (IAula)iterator.next();
      	System.out.println(elem.getSala());
      	System.out.println(elem.getDiaSemana());
      	
      	
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