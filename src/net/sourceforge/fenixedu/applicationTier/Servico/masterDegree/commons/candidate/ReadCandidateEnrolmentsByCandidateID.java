package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID implements IService {

    public List run(Integer candidateID) throws FenixServiceException, ExcepcaoPersistencia {
        List result = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                        candidateID);

        if (masterDegreeCandidate == null) {
            throw new NonExistingServiceException();
        }

        List candidateEnrolments = sp.getIPersistentCandidateEnrolment().readByMDCandidate(
                masterDegreeCandidate);

        if (candidateEnrolments == null) {
            throw new NonExistingServiceException();
        }

        for (final Iterator candidateEnrolmentIterator = candidateEnrolments.iterator(); candidateEnrolmentIterator
                .hasNext();) {
            ICandidateEnrolment candidateEnrolmentTemp = (ICandidateEnrolment) candidateEnrolmentIterator
                    .next();
            InfoCandidateEnrolment infoCandidateEnrolment = InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree
                    .newInfoFromDomain(candidateEnrolmentTemp);
            result.add(infoCandidateEnrolment);
        }

        return result;
    }
}