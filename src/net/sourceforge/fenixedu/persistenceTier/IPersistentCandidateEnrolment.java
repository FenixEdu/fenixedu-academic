package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICandidateEnrolment;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentCandidateEnrolment extends IPersistentObject {

	/**
	 * @param candidateEnrolment
	 * @throws ExcepcaoPersistencia
	 */
	public void delete(ICandidateEnrolment candidateEnrolment)
			throws ExcepcaoPersistencia;

	/**
	 * @param masterDegreeCandidate
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByMDCandidate(Integer masterDegreeCandidateID)
			throws ExcepcaoPersistencia;


	/**
	 * @param masterDegreeCandidate
	 * @throws ExcepcaoPersistencia
	 */
	public void deleteAllByCandidateID(Integer masterDegreeCandidateID)
			throws ExcepcaoPersistencia;
}