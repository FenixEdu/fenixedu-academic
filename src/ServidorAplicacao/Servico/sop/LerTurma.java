package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurma
 *
 * @author tfc130
 * @version
 **/

import DataBeans.ClassKey;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurma implements IServico {

  private static LerTurma _servico = new LerTurma();
  /**
   * The singleton access method of this class.
   **/
  public static LerTurma getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerTurma() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerTurma";
  }

  public Object run(ClassKey keyTurma) {
                        
    InfoClass infoTurma = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      ITurma turma = sp.getITurmaPersistente().readByNome(keyTurma.getNomeTurma());
	  if (turma != null) {
	  	InfoDegree infoLicenciatura = new InfoDegree(turma.getLicenciatura().getSigla(),
	  	                                                         turma.getLicenciatura().getNome());
	  	infoTurma= new InfoClass(turma.getNome(), turma.getSemestre(),turma.getAnoCurricular(), infoLicenciatura);
	  }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return infoTurma;
  }

}