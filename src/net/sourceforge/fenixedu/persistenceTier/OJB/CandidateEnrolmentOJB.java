package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateEnrolment;

import org.apache.ojb.broker.query.Criteria;

/*
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolmentOJB extends PersistentObjectOJB implements
		IPersistentCandidateEnrolment {

	public List readByMDCandidate(Integer masterDegreeCandidateID)
			throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("masterDegreeCandidate.idInternal",
				masterDegreeCandidateID);
		return queryList(CandidateEnrolment.class, crit);

	}

}