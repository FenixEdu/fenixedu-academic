package ServidorAplicacao.Servico.sop;

/**
 * Serviço RemoverTurno
 *
 * @author tfc130
 * @version
 **/

import DataBeans.ClassAndShiftKeys;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoverTurno implements IServico {

  private static RemoverTurno _servico = new RemoverTurno();
  /**
   * The singleton access method of this class.
   **/
  public static RemoverTurno getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private RemoverTurno() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "RemoverTurno";
  }

  public Object run(ClassAndShiftKeys keysTurmaAndTurno) {

    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      sp.getITurmaTurnoPersistente().delete(keysTurmaAndTurno.getNomeTurma(), keysTurmaAndTurno.getNomeTurno());
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }

}