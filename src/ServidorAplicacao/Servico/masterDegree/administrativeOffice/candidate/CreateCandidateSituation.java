package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;
import java.util.Iterator;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoPerson;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationName;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This Service Changes the Candidate Situation from Pre-Candidate to Pending
 * 
 */

public class CreateCandidateSituation implements IServico {


	private static CreateCandidateSituation servico = new CreateCandidateSituation();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateCandidateSituation getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateCandidateSituation() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "CreateCandidateSituation";
	}

	public void run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson) throws Exception{

		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		
		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			
			
			ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
			IPessoa person = Cloner.copyInfoPerson2IPerson(infoPerson);
			
			
			masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readByExecutionDegreeAndPerson(executionDegree, person);
			if (masterDegreeCandidate == null)
				throw new ExcepcaoInexistente("Unknown Master Degree Candidate !!");	
			
			Iterator iterator = masterDegreeCandidate.getSituations().iterator();
			while (iterator.hasNext()){
				ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next();
				if (candidateSituation.getValidation().equals(new State(State.ACTIVE))){
					candidateSituation.setValidation(new State(State.INACTIVE));
					sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);
				}
					
			}
		
			// Create the New Candidate Situation			
			ICandidateSituation candidateSituation = new CandidateSituation();
			Calendar calendar = Calendar.getInstance();
			candidateSituation.setDate(calendar.getTime());
			candidateSituation.setSituation(new SituationName(SituationName.PENDENTE_STRING));
			candidateSituation.setValidation(new State(State.ACTIVE));
			candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
			sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);

		} catch (ExistingPersistentException ex) {
			// The situation Already Exists ... Something wrong ?
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
	}
		
}
