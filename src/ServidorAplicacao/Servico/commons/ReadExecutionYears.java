package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

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
public class ReadExecutionYears implements IServico {
	private static ReadExecutionYears service = new ReadExecutionYears();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionYears getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadExecutionYears";
	}
	
	public ArrayList run() {
                        
	  ArrayList result = new ArrayList();
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
		
		List executionYears = executionYearDAO.readAllExecutionYear();
		
		
		Iterator iterator = executionYears.iterator();
		while(iterator.hasNext()){
			String year = ((IExecutionYear) iterator.next()).getYear();
			result.add(new LabelValueBean(year, year));
		}

	  } catch (ExcepcaoPersistencia ex) {
	  	throw new RuntimeException(ex);
	  }
    
	  return result;
	}

}
