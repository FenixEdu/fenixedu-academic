/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateMasterDegreeCandidate implements IServico {


	private static CreateMasterDegreeCandidate servico = new CreateMasterDegreeCandidate();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateMasterDegreeCandidate getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateMasterDegreeCandidate() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "CreateMasterDegreeCandidate";
	}

	public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate)
		throws ExcepcaoInexistente, FenixServiceException , ExistingServiceException {

		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		
		ISuportePersistente sp = null;

		// Get the Actual Execution Year
		IExecutionYear executionYear = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			executionYear = sp.getIPersistentExecutionYear().readActualExecutionYear();		
  		    
  		    InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
  		    infoExecutionYear = Cloner.copyIExecutionYear2InfoExecutionYear(executionYear);
  		    newMasterDegreeCandidate.setInfoExecutionYear(infoExecutionYear);

			System.out.println("Novo" + newMasterDegreeCandidate);

			masterDegreeCandidate = Cloner.copyInfoMasterDegreeCandidate2IMasterDegreCandidate(newMasterDegreeCandidate);

			System.out.println("Clone" + masterDegreeCandidate);

			// Generate the Candidate's number	
			Integer number = sp.getIPersistentMasterDegreeCandidate().generateCandidateNumber(executionYear.getYear(), masterDegreeCandidate.getDegree().getNome());
			
			masterDegreeCandidate.setCandidateNumber(number);
			
			// Write the new Candidate
			sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);		
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
		
		return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
		}
}
