package ServidorAplicacao.Servico.commons.degree;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadDegreeByYearAndCode implements IServico {
	private static ReadDegreeByYearAndCode service = new ReadDegreeByYearAndCode();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadDegreeByYearAndCode getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadDegreeByYearAndCode";
	}
	
	public InfoExecutionDegree run(String executionYearString, String degreeCode) throws NonExistingServiceException {
                        
	  
	  ICursoExecucao executionDegree = null;
	   
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();

		IExecutionYear executionYear = new ExecutionYear();
		executionYear.setYear(executionYearString);
		executionDegree = sp.getICursoExecucaoPersistente().readByDegreeCodeAndExecutionYear(degreeCode, executionYear); 
		
	  } catch (ExcepcaoPersistencia ex) {
	  	throw new RuntimeException(ex);
	  }
      
      if (executionDegree == null){
      	throw new NonExistingServiceException();
      }
    
      return (InfoExecutionDegree) Cloner.get(executionDegree);
	}

}
