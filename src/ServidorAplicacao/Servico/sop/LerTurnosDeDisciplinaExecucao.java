/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço LerTurnosDeDisciplinaExecucao.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
public class LerTurnosDeDisciplinaExecucao implements IServico {
  private static LerTurnosDeDisciplinaExecucao _servico = new LerTurnosDeDisciplinaExecucao();
  /**
   * The singleton access method of this class.
   **/
  public static LerTurnosDeDisciplinaExecucao getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerTurnosDeDisciplinaExecucao() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerTurnosDeDisciplinaExecucao";
  }
  public List run(InfoExecutionCourse infoExecutionCourse) {

    List shiftList = null;
    List infoShiftList = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      
      IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
      
      shiftList = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
      
      Iterator iterator = shiftList.iterator();
      infoShiftList = new ArrayList();
      while(iterator.hasNext()) {
      	ITurno shift = (ITurno)iterator.next();
        infoShiftList.add(Cloner.copyIShift2InfoShift(shift));
        }
      															  
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return infoShiftList;
  }

}