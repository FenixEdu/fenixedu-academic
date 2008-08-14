package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID extends Service {

    public List run(Integer candidateID) throws FenixServiceException {
	List result = new ArrayList();

	MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);

	if (masterDegreeCandidate == null) {
	    throw new NonExistingServiceException();
	}

	List candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();

	if (candidateEnrolments == null) {
	    throw new NonExistingServiceException();
	}

	for (final Iterator candidateEnrolmentIterator = candidateEnrolments.iterator(); candidateEnrolmentIterator.hasNext();) {
	    CandidateEnrolment candidateEnrolmentTemp = (CandidateEnrolment) candidateEnrolmentIterator.next();
	    InfoCandidateEnrolment infoCandidateEnrolment = InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree
		    .newInfoFromDomain(candidateEnrolmentTemp);
	    result.add(infoCandidateEnrolment);
	}

	return result;
    }
}