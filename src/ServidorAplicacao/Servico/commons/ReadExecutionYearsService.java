/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *
 */
public class ReadExecutionYearsService implements IService
{

	public ReadExecutionYearsService()
	{
	  super();
	}

	public List run() throws FenixServiceException {
                        
	  List result = new ArrayList();
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
		
		List executionYears = executionYearDAO.readAllExecutionYear();
		if (executionYears != null && !executionYears.isEmpty())
		{
			for (int i = 0; i < executionYears.size(); i++)
			{
				result.add(Cloner.get((IExecutionYear) executionYears.get(i)));
			}
		}
	  } catch (ExcepcaoPersistencia ex) {
	  	throw new FenixServiceException(ex);
	  }
    
	  return result;
	}

}