package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID implements IService {

    public List run(Integer candidateID) throws FenixServiceException {
        List result = new ArrayList();

        try {
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

            Iterator candidateEnrolmentIterator = candidateEnrolments.iterator();

            while (candidateEnrolmentIterator.hasNext()) {
                ICandidateEnrolment candidateEnrolmentTemp = (ICandidateEnrolment) candidateEnrolmentIterator
                        .next();
                result
                        .add(Cloner
                                .copyICandidateEnrolment2InfoCandidateEnrolment(candidateEnrolmentTemp));
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return result;
    }
}