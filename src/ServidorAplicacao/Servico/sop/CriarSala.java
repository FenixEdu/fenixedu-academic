/*
 * CriarSala.java
 *
 * Created on 25 de Outubro de 2002, ??:??
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço CriarSala.
 *
 * @author tfc130
 **/
import DataBeans.InfoRoom;
import Dominio.ISala;
import Dominio.Sala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarSala implements IServico {

  private static CriarSala _servico = new CriarSala();
  /**
   * The singleton access method of this class.
   **/
  public static CriarSala getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private CriarSala() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "CriarSala";
  }

  public Object run(InfoRoom infoSala) {
                        
    ISala sala = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      sala = new Sala(infoSala.getNome(), infoSala.getEdificio(), infoSala.getPiso(), infoSala.getTipo(),
                      infoSala.getCapacidadeNormal(), infoSala.getCapacidadeExame());
      sp.getISalaPersistente().lockWrite(sala);
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }
  
}