/*
 * LerTurnosDeTurma.java
 *
 * Created on 28 de Outubro de 2002, 20:26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurnosDeTurma
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
import DataBeans.InfoShift;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurnosDeTurma implements IServico {

  private static LerTurnosDeTurma _servico = new LerTurnosDeTurma();
  /**
   * The singleton access method of this class.
   **/
  public static LerTurnosDeTurma getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerTurnosDeTurma() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerTurnosDeTurma";
  }

  public Object run(ClassKey keyTurma) {
    ArrayList infoTurnos = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      List turnos = sp.getITurmaTurnoPersistente().readTurnosDeTurma(keyTurma.getNomeTurma());
      
      Iterator iterator = turnos.iterator();
      infoTurnos = new ArrayList();
      while(iterator.hasNext()) {
      	ITurno elem = (ITurno)iterator.next();
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
        infoTurnos.add(new InfoShift(elem.getNome(), elem.getTipo(), elem.getLotacao(),
                                  infoDisciplinaExecucao));
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return infoTurnos;
  }

}