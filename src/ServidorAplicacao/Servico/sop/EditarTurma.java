/*
 * EditarTurma.java
 *
 * Created on 27 de Outubro de 2002, 20:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurma.
 *
 * @author tfc130
 **/
import DataBeans.ClassKey;
import DataBeans.InfoClass;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarTurma implements IServico {

  private static EditarTurma _servico = new EditarTurma();
  /**
   * The singleton access method of this class.
   **/
  public static EditarTurma getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private EditarTurma() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "EditarTurma";
  }

  public Object run(ClassKey turmaAntiga, InfoClass turmaNova) {

    ITurma turma = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();

      turma = sp.getITurmaPersistente().readByNome(turmaAntiga.getNomeTurma());
      if (turma != null) {
          turma.setNome(turmaNova.getNome());
          turma.setSemestre(turmaNova.getSemestre());
          sp.getITurmaPersistente().lockWrite(turma);
          result = true;
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }

}