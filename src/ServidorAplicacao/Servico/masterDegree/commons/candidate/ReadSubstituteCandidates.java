
package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadSubstituteCandidates implements IServico {

	private static ReadSubstituteCandidates servico = new ReadSubstituteCandidates();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadSubstituteCandidates getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadSubstituteCandidates() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadSubstituteCandidates";
	}

	
	public List run(String[] candidateList, String[] ids) throws FenixServiceException {
		
		ISuportePersistente sp = null;
		List result = new ArrayList();
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Read the substitute candidates
			int size = candidateList.length;
			
			for (int i = 0; i<size; i++){ 
				if(candidateList[i].equals(SituationName.SUPLENTE_STRING)
					|| candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)
					|| candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)
					|| candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING)) {

					Integer idInternal = new Integer (ids[i]);
					IMasterDegreeCandidate masterDegreeCandidateTemp = new MasterDegreeCandidate();
					masterDegreeCandidateTemp.setIdInternal(idInternal);
					IMasterDegreeCandidate masterDegreeCandidateToWrite = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(masterDegreeCandidateTemp, false);
					result.add(Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidateToWrite));
				}
			}
			
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
				
		return result;

	}
	
}
