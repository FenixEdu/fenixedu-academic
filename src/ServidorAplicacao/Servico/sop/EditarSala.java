/*
 * EditarSala.java
 *
 * Created on 27 de Outubro de 2002, 19:43
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarSala
 *
 * @author tfc130
 **/
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarSala implements IServico {

  private static EditarSala _servico = new EditarSala();
  /**
   * The singleton access method of this class.
   **/
  public static EditarSala getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private EditarSala() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "EditarSala";
  }

  public Object run(RoomKey salaAntiga, InfoRoom salaNova) {

    ISala sala = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();

      sala = sp.getISalaPersistente().readByName(salaAntiga.getNomeSala());
      if (sala != null) {
          sala.setNome(salaNova.getNome());
          sala.setEdificio(salaNova.getEdificio());
          sala.setPiso(salaNova.getPiso());
          sala.setCapacidadeNormal(salaNova.getCapacidadeNormal());
          sala.setCapacidadeExame(salaNova.getCapacidadeExame());
          sala.setTipo(salaNova.getTipo());
          sp.getISalaPersistente().lockWrite(sala);
          result = true;
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }
  
}