/*
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeTurno
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
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeTurno implements IServico {

  private static LerAulasDeTurno _servico = new LerAulasDeTurno();
  /**
   * The singleton access method of this class.
   **/
  public static LerAulasDeTurno getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerAulasDeTurno() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerAulasDeTurno";
  }

  public Object run(ShiftKey shiftKey) {
    ArrayList infoAulas = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      ITurno shift = Cloner.copyInfoShift2Shift(new InfoShift(shiftKey.getShiftName(),null, null, shiftKey.getInfoExecutionCourse()));
      
      List aulas = sp.getITurnoAulaPersistente().readAulasDeTurno(shift);

      Iterator iterator = aulas.iterator();
      infoAulas = new ArrayList();
      while(iterator.hasNext()) {
      	IAula elem = (IAula)iterator.next();
		if (elem != null) {
			InfoRoom infoSala = null;
			if (elem.getSala() != null)
				infoSala =
					new InfoRoom(
						elem.getSala().getNome(),
						elem.getSala().getEdificio(),
						elem.getSala().getPiso(),
						elem.getSala().getTipo(),
						elem.getSala().getCapacidadeNormal(),
						elem.getSala().getCapacidadeExame());
			InfoDegree infoLicenciatura =
				new InfoDegree(
					elem
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getCurso()
						.getSigla(),
					elem
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getCurso()
						.getNome());
			InfoExecutionDegree infoLicenciaturaExecucao =
				new InfoExecutionDegree(
					elem
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getAnoLectivo(),
					infoLicenciatura);
			InfoExecutionCourse infoDisciplinaExecucao =
				new InfoExecutionCourse(
					elem.getDisciplinaExecucao().getNome(),
					elem.getDisciplinaExecucao().getSigla(),
					elem.getDisciplinaExecucao().getPrograma(),
					infoLicenciaturaExecucao,
					elem.getDisciplinaExecucao().getTheoreticalHours(),
					elem.getDisciplinaExecucao().getPraticalHours(),
					elem.getDisciplinaExecucao().getTheoPratHours(),
					elem.getDisciplinaExecucao().getLabHours());
			infoAulas.add(
				new InfoLesson(
					elem.getDiaSemana(),
					elem.getInicio(),
					elem.getFim(),
					elem.getTipo(),
					infoSala,
					infoDisciplinaExecucao));
		}
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return infoAulas;
  }

}