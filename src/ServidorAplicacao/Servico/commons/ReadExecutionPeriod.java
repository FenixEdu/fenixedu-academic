/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ReadExecutionPeriod implements IServico {

		private static ReadExecutionPeriod service = new ReadExecutionPeriod();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionPeriod getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadExecutionPeriod";
	}
	
	public InfoExecutionPeriod run(String name, InfoExecutionYear infoExecutionYear) throws FenixServiceException {
                        
	  InfoExecutionPeriod result =null;
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
		IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
		IExecutionPeriod executionPeriod =executionPeriodDAO.readByNameAndExecutionYear(name,executionYear);
		if ( executionPeriod != null) {
			result = (InfoExecutionPeriod) Cloner.get(executionPeriod);
		}
	  } catch (ExcepcaoPersistencia ex) {
	  	throw new FenixServiceException(ex);
	  }
    
	  return result;
	}
}
