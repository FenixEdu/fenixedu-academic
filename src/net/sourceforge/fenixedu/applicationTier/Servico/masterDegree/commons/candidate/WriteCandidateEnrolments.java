package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class WriteCandidateEnrolments implements IService {

    public void run(Integer[] selection, Integer candidateID, Double credits, String givenCreditsRemarks)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCandidateEnrolment persistentCandidateEnrolment = sp
                    .getIPersistentCandidateEnrolment();

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                            candidateID, true);

            if (masterDegreeCandidate == null) {
                throw new NonExistingServiceException();
            }

            masterDegreeCandidate.setGivenCredits(credits);

            if (credits.floatValue() != 0) {
                masterDegreeCandidate.setGivenCreditsRemarks(givenCreditsRemarks);
            }

            List candidateEnrollmentsToDelete = new ArrayList();
            List curricularCoursesToEnroll = new ArrayList();

            for (int i = 0; i < selection.length; i++) {
                curricularCoursesToEnroll.add(selection[i]);
            }

            List curricularCoursesToEnrollFiltered = filterEnrollments(persistentCandidateEnrolment,
                    masterDegreeCandidate, candidateEnrollmentsToDelete, curricularCoursesToEnroll);

            writeFilteredEnrollments(sp, masterDegreeCandidate, curricularCoursesToEnrollFiltered);

            deleteRemainingEnrollments(persistentCandidateEnrolment, candidateEnrollmentsToDelete);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

    /**
     * @param persistentCandidateEnrolment
     * @param candidateEnrollmentsToDelete
     * @throws ExcepcaoPersistencia
     */
    private void deleteRemainingEnrollments(IPersistentCandidateEnrolment persistentCandidateEnrolment,
            List candidateEnrollmentsToDelete) throws ExcepcaoPersistencia {
        Iterator iterCandidateEnrollmentsToDelete = candidateEnrollmentsToDelete.iterator();
        while (iterCandidateEnrollmentsToDelete.hasNext()) {
            ICandidateEnrolment candidateEnrolmentToDelete = (ICandidateEnrolment) iterCandidateEnrollmentsToDelete
                    .next();
            persistentCandidateEnrolment.delete(candidateEnrolmentToDelete);
        }
    }

    /**
     * @param sp
     * @param masterDegreeCandidate
     * @param curricularCoursesToEnroll
     * @throws NonExistingServiceException
     * @throws ExcepcaoPersistencia
     */
    private void writeFilteredEnrollments(ISuportePersistente sp,
            IMasterDegreeCandidate masterDegreeCandidate, List curricularCoursesToEnroll)
            throws NonExistingServiceException, ExcepcaoPersistencia {
        Iterator iterCurricularCourseIds = curricularCoursesToEnroll.iterator();
        while (iterCurricularCourseIds.hasNext()) {

            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(CurricularCourse.class, (Integer) iterCurricularCourseIds.next());

            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }

            ICandidateEnrolment candidateEnrolment = new CandidateEnrolment();
            sp.getIPersistentCandidateEnrolment().simpleLockWrite(candidateEnrolment);

            candidateEnrolment.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateEnrolment.setCurricularCourse(curricularCourse);
        }
    }

    /**
     * @param persistentCandidateEnrolment
     * @param masterDegreeCandidate
     * @param candidateEnrollmentsToDelete
     * @param curricularCoursesToEnroll
     * @throws ExcepcaoPersistencia
     */
    private List filterEnrollments(IPersistentCandidateEnrolment persistentCandidateEnrolment,
            IMasterDegreeCandidate masterDegreeCandidate, List candidateEnrollmentsToDelete,
            List curricularCoursesToEnroll) throws ExcepcaoPersistencia {
        List existentCandidateEnrollments = persistentCandidateEnrolment
                .readByMDCandidate(masterDegreeCandidate);
        Iterator iterCandidateEnrollment = existentCandidateEnrollments.iterator();
        while (iterCandidateEnrollment.hasNext()) {
            ICandidateEnrolment existentCandidateEnrolment = (ICandidateEnrolment) iterCandidateEnrollment
                    .next();
            if (curricularCoursesToEnroll.contains(existentCandidateEnrolment.getCurricularCourse()
                    .getIdInternal())) {
                removeElement(curricularCoursesToEnroll, existentCandidateEnrolment);
            } else {
                candidateEnrollmentsToDelete.add(existentCandidateEnrolment);
            }
        }

        //remove repeated Enrollments to "protect" the service
        // in order to avoid the listing of a course with more than one scientific area
        List result = new ArrayList();
        for (Iterator iter = curricularCoursesToEnroll.iterator(); iter.hasNext();) {
            Integer curricularCourse = (Integer) iter.next();

            if (!result.contains(curricularCourse)) {
                result.add(curricularCourse);
            }
        }

        return result;

    }

    /**
     * @param curricularCoursesToEnroll
     * @param existentCandidateEnrolment
     */
    private void removeElement(List curricularCoursesToEnroll,
            ICandidateEnrolment existentCandidateEnrolment) {
        final Integer idToRemove = existentCandidateEnrolment.getCurricularCourse().getIdInternal();
        CollectionUtils.filter(curricularCoursesToEnroll, new Predicate() {

            public boolean evaluate(Object object) {
                Integer id = (Integer) object;

                if (id.equals(idToRemove)) {
                    return false;
                }
                return true;
            }
        });
    }
}