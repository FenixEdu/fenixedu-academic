package ServidorAplicacao.Servico.sop;

/**
 * @author João Mota
 * 
 */

import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadSchoolClass implements IServico {

  private static ReadSchoolClass _servico = new ReadSchoolClass();
  /**
   * The singleton access method of this class.
   **/
  public static ReadSchoolClass getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadSchoolClass() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadSchoolClass";
  }

  public InfoClass run(InfoClass infoSchoolClass) {
                        
    
	InfoClass result= null;   
    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
     	ITurma schoolClass = new Turma(infoSchoolClass.getIdInternal());
    	ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();
    	schoolClass = (ITurma) persistentSchoolClass.readByOId(schoolClass,false);
    	if (schoolClass!=null){
    		result=Cloner.copyClass2InfoClass(schoolClass);
    	}
	
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return result;
  }

}