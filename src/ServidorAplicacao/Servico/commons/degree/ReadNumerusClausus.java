package ServidorAplicacao.Servico.commons.degree;

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
public class ReadNumerusClausus implements IServico {
	private static ReadNumerusClausus service = new ReadNumerusClausus();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadNumerusClausus getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadNumerusClausus";
	}
	
	public Integer run(String executionYearString, String degreeCode) throws NonExistingServiceException {
                        
	  
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
    
      return executionDegree.getCurricularPlan().getNumerusClausus();
	}

}
