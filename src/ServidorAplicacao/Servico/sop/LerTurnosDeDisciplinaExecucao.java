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
import DataBeans.InfoShift;
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
  public Object run(InfoExecutionCourse iDE) {

    List turnos = null;
    List infoTurnos = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      turnos = sp.getITurnoPersistente().readByDisciplinaExecucao(iDE.getSigla(),
      															  iDE.getInfoLicenciaturaExecucao().getAnoLectivo(),
      															  iDE.getInfoLicenciaturaExecucao().getInfoLicenciatura().getSigla());
      Iterator iterator = turnos.iterator();
      infoTurnos = new ArrayList();
      while(iterator.hasNext()) {
      	ITurno elem = (ITurno)iterator.next();
        infoTurnos.add(new InfoShift(elem.getNome(), elem.getTipo(), elem.getLotacao(), iDE));
        }
      															  
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return infoTurnos;
  }

}