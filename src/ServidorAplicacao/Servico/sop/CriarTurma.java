/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurma
 *
 * @author tfc130
 **/
import DataBeans.InfoClass;
import Dominio.ICurso;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarTurma implements IServico {

  private static CriarTurma _servico = new CriarTurma();
  /**
   * The singleton access method of this class.
   **/
  public static CriarTurma getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private CriarTurma() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "CriarTurma";
  }

  public Object run(InfoClass infoTurma) {
                        
    ITurma turma = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      ICurso licenciatura = sp.getICursoPersistente().readBySigla(infoTurma.getInfoLicenciatura().getSigla());
      turma = new Turma(infoTurma.getNome(), infoTurma.getSemestre(),infoTurma.getAnoCurricular(), licenciatura);
      sp.getITurmaPersistente().lockWrite(turma);
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }
  
}