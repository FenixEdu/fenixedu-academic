package ServidorAplicacao.Servico.sop;

/**
 * Serviço RemoverTurno
 *
 * @author tfc130
 * @version
 **/

import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import Dominio.ITurno;
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

  public Object run(InfoShift infoShift, InfoClass infoClass) {

    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      ITurno shift = Cloner.copyInfoShift2IShift(infoShift);
      ITurma classTemp = Cloner.copyInfoClass2Class(infoClass);
      
      sp.getITurmaTurnoPersistente().delete(shift, classTemp);
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }

}