package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ICandidateEnrolment;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID implements IServico {

	private static ReadCandidateEnrolmentsByCandidateID servico = new ReadCandidateEnrolmentsByCandidateID();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCandidateEnrolmentsByCandidateID getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadCandidateEnrolmentsByCandidateID() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadCandidateEnrolmentsByCandidateID";
	}

	public List run(Integer candidateID) throws FenixServiceException {
		List result = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IMasterDegreeCandidate mdcTemp = new MasterDegreeCandidate();
			mdcTemp.setIdInternal(candidateID);
			IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(mdcTemp, false);

			if (masterDegreeCandidate == null){
				throw new NonExistingServiceException();
			}

			List candidateEnrolments = sp.getIPersistentCandidateEnrolment().readByMDCandidate(masterDegreeCandidate);

			if (candidateEnrolments == null){
				throw new NonExistingServiceException();
			}

			Iterator candidateEnrolmentIterator = candidateEnrolments.iterator();
			
			while (candidateEnrolmentIterator.hasNext()){ 
				ICandidateEnrolment candidateEnrolmentTemp = (ICandidateEnrolment) candidateEnrolmentIterator.next();
				result.add(Cloner.copyICandidateEnrolment2InfoCandidateEnrolment(candidateEnrolmentTemp));
			}			
			
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		return result;
	}
}
