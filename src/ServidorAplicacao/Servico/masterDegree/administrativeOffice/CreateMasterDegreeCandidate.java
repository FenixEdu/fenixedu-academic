/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
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
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.CandidateSituationValidation;
import Util.SituationName;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

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

	public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate, String degreeName)
		throws ExcepcaoInexistente, FenixServiceException , ExistingServiceException{

		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		
		ISuportePersistente sp = null;

		// Get the Actual Execution Year
		IExecutionYear executionYear = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			executionYear = sp.getIPersistentExecutionYear().readActualExecutionYear();		
  		    
			// Read the Execution of this degree in the current execution Year
			
			ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYear(degreeName, executionYear);
			
			// Create the Candidate
			
			// Set the Candidate's Situation
			Set situations = new HashSet();
			ICandidateSituation candidateSituation = new CandidateSituation();
			candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
			candidateSituation.setRemarks("Pré-Candidatura. Pagamento da candidatura por efectuar.");
			candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
			candidateSituation.setValidation(new CandidateSituationValidation(CandidateSituationValidation.ACTIVE));
			
			Calendar actualDate = Calendar.getInstance();
			candidateSituation.setDate(actualDate.getTime());
			
			situations.add(candidateSituation);
			
			masterDegreeCandidate.setSituations(situations);
//			masterDegreeCandidate.setName(newMasterDegreeCandidate.getName());
//			masterDegreeCandidate.setIdentificationDocumentNumber(newMasterDegreeCandidate.getIdentificationDocumentNumber());
//			masterDegreeCandidate.setIdentificationDocumentType(new TipoDocumentoIdentificacao(newMasterDegreeCandidate.getInfoIdentificationDocumentType()));			
			masterDegreeCandidate.setSpecialization(new Specialization(newMasterDegreeCandidate.getSpecialization()));
			masterDegreeCandidate.setExecutionDegree(executionDegree);

			// Generate the Candidate's number	
			Integer number = sp.getIPersistentMasterDegreeCandidate().generateCandidateNumber(
								executionYear.getYear(),
								masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getCurso().getNome());

			masterDegreeCandidate.setCandidateNumber(number);
			
			// Write the new Candidate
			sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);		
			
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
		
		// Return the new Candidate
		return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
		}
}
