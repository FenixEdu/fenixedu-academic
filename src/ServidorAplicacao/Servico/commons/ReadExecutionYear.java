/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ReadExecutionYear implements IServico {

	private static ReadExecutionYear service = new ReadExecutionYear();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionYear getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadExecutionYear";
	}
	
	public InfoExecutionYear run(String year) throws FenixServiceException {
                        
	  InfoExecutionYear result =null;
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
		
		IExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);
		if (executionYear != null) {
			result = (InfoExecutionYear) Cloner.get(executionYear);				
		}

	  } catch (ExcepcaoPersistencia ex) {
	  	throw new FenixServiceException(ex);
	  }
    
	  return result;
	}

}
