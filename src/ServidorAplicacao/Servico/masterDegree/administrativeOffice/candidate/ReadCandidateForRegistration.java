/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateForRegistration implements IServico {

	private static ReadCandidateForRegistration servico = new ReadCandidateForRegistration();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCandidateForRegistration getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadCandidateForRegistration() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadCandidateForRegistration";
	}

	public List run(String executionYearString, String degreeCode) throws FenixServiceException {

		ISuportePersistente sp = null;
		List result = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Get the Actual Execution Year
			IExecutionYear executionYear = null;

			executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(executionYearString);
			
			ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeCodeAndExecutionYear(degreeCode, executionYear);		
			result = sp.getIPersistentCandidateSituation().readCandidateListforRegistration(executionDegree);
		} catch (ExcepcaoPersistencia ex) {

			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 


		if (result == null){
			throw new NonExistingServiceException();
		}


		List candidateList = new ArrayList();
		Iterator iterator = result.iterator();
		while(iterator.hasNext()){
			ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next(); 
			InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateSituation.getMasterDegreeCandidate());
			infoMasterDegreeCandidate.setInfoCandidateSituation(Cloner.copyICandidateSituation2InfoCandidateSituation(candidateSituation));
			candidateList.add(infoMasterDegreeCandidate);
		}
		
		return candidateList;
		
	}
	
	public List run(Integer executionDegreeCode) throws FenixServiceException {

			ISuportePersistente sp = null;
			List result = null;
		
			try {
				sp = SuportePersistenteOJB.getInstance();
			
				// Get the Actual Execution Year
				
				ICursoExecucao executionDegree = new CursoExecucao();
				executionDegree.setIdInternal(executionDegreeCode);
						
				result = sp.getIPersistentCandidateSituation().readCandidateListforRegistration(executionDegree);
			} catch (ExcepcaoPersistencia ex) {

				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			} 


			if (result == null){
				throw new NonExistingServiceException();
			}


			List candidateList = new ArrayList();
			Iterator iterator = result.iterator();
			while(iterator.hasNext()){
				ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next(); 
				InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateSituation.getMasterDegreeCandidate());
				infoMasterDegreeCandidate.setInfoCandidateSituation(Cloner.copyICandidateSituation2InfoCandidateSituation(candidateSituation));
				candidateList.add(infoMasterDegreeCandidate);
			}
		
			return candidateList;
		
		}
}
