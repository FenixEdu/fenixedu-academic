package ServidorAplicacao.Servico.sop;

/**
 * Serviï¿½o RemoverAula
 *
 * @author tfc130
 * @version
 **/

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ISala;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoverAula implements IServico {

// FIXME : O serviço nao devolve False quando a aula nao existe!...

  private static RemoverAula _servico = new RemoverAula();
  /**
   * The singleton access method of this class.
   **/
  public static RemoverAula getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private RemoverAula() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "RemoverAula";
  }

  public Object run(InfoLesson infoLesson, InfoShift infoShift) {
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      ISala room = sp.getISalaPersistente().readByName(infoLesson.getInfoSala().getNome());
      		      
	  ITurno shift = Cloner.copyInfoShift2Shift(infoShift);		      
  
      sp.getITurnoAulaPersistente().delete(shift, infoLesson.getDiaSemana(),
                                           infoLesson.getInicio(), infoLesson.getFim(), room);
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }

}