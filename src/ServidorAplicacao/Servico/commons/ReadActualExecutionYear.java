package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadActualExecutionYear implements IServico {
	private static ReadActualExecutionYear service = new ReadActualExecutionYear();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadActualExecutionYear getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadActualExecutionYear";
	}
	
	public InfoExecutionYear run() {
                        
	  InfoExecutionYear infoExecutionYear = null;
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
		
		IExecutionYear executionYear= persistentExecutionYear.readActualExecutionYear();
		
		infoExecutionYear = Cloner.copyIExecutionYear2InfoExecutionYear(executionYear);

	  } catch (ExcepcaoPersistencia ex) {
	  	throw new RuntimeException(ex);
	  }
    
	  return infoExecutionYear;
	}

}
