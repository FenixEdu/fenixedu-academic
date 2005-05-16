package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentCandidateEnrolment extends IPersistentObject {

	/**
	 * @param masterDegreeCandidate
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByMDCandidate(Integer masterDegreeCandidateID)
			throws ExcepcaoPersistencia;

}