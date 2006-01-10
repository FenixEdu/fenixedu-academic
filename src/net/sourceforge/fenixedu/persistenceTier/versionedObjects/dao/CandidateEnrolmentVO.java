package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateEnrolment;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/*
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolmentVO extends VersionedObjectsBase implements
		IPersistentCandidateEnrolment {

	public List readByMDCandidate(Integer masterDegreeCandidateID)
			throws ExcepcaoPersistencia {

		final MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) readByOID(
				MasterDegreeCandidate.class, masterDegreeCandidateID);

		return masterDegreeCandidate.getCandidateEnrolments();
	}

}