/*
 * AdicionarTurno.java
 *
 * Created on 27 de Outubro de 2002, 12:31
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o AdicionarTurno.
 *
 * @author tfc130
 **/
import DataBeans.ClassAndShiftKeys;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class AdicionarTurno implements IServico {

  private static AdicionarTurno _servico = new AdicionarTurno();
  /**
   * The singleton access method of this class.
   **/
  public static AdicionarTurno getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private AdicionarTurno() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "AdicionarTurno";
  }

  public Object run(ClassAndShiftKeys keysTurmaAndTurno) {

    ITurmaTurno turmaTurno = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      ITurma turma1 = sp.getITurmaPersistente().readByNome(keysTurmaAndTurno.getNomeTurma());
      ITurno turno1 = sp.getITurnoPersistente().readByNome(keysTurmaAndTurno.getNomeTurno());

	  turmaTurno = new TurmaTurno(turma1, turno1);
      sp.getITurmaTurnoPersistente().lockWrite(turmaTurno);
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }

    return new Boolean (result);
  }

}