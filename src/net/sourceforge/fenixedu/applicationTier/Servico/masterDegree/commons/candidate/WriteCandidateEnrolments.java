package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class WriteCandidateEnrolments implements IService {

    public void run(Set<Integer> selectedCurricularCoursesIDs, Integer candidateID, Double credits,
            String givenCreditsRemarks) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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

        List<ICandidateEnrolment> candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();
        List<Integer> candidateEnrolmentsCurricularCoursesIDs = (List<Integer>) CollectionUtils.collect(
                candidateEnrolments, new Transformer() {
                    public Object transform(Object arg0) {
                        ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) arg0;
                        return candidateEnrolment.getCurricularCourse().getIdInternal();
                    }
                });

        Collection<Integer> curricularCoursesToEnroll = CollectionUtils.subtract(
                selectedCurricularCoursesIDs, candidateEnrolmentsCurricularCoursesIDs);

        final Collection<Integer> curricularCoursesToDelete = CollectionUtils.subtract(
                candidateEnrolmentsCurricularCoursesIDs, selectedCurricularCoursesIDs);

        Collection<ICandidateEnrolment> candidateEnrollmentsToDelete = CollectionUtils
                .select(candidateEnrolments, new Predicate() {
                    public boolean evaluate(Object arg0) {
                        ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) arg0;
                        return (curricularCoursesToDelete.contains(candidateEnrolment
                                .getCurricularCourse().getIdInternal()));
                    }
                });

        writeFilteredEnrollments(sp, masterDegreeCandidate, curricularCoursesToEnroll);

        deleteRemainingEnrollments(persistentCandidateEnrolment, candidateEnrollmentsToDelete);

    }

    /**
     * @param persistentCandidateEnrolment
     * @param candidateEnrollmentsToDelete
     * @throws ExcepcaoPersistencia
     */
    private void deleteRemainingEnrollments(IPersistentCandidateEnrolment persistentCandidateEnrolment,
            Collection<ICandidateEnrolment> candidateEnrollmentsToDelete) throws ExcepcaoPersistencia {
        Iterator iterCandidateEnrollmentsToDelete = candidateEnrollmentsToDelete.iterator();
        while (iterCandidateEnrollmentsToDelete.hasNext()) {
            ICandidateEnrolment candidateEnrolmentToDelete = (ICandidateEnrolment) iterCandidateEnrollmentsToDelete
                    .next();
            candidateEnrolmentToDelete.getCurricularCourse().getCandidateEnrolments().remove(
                    candidateEnrolmentToDelete);
            candidateEnrolmentToDelete.getMasterDegreeCandidate().getCandidateEnrolments().remove(
                    candidateEnrolmentToDelete);
            candidateEnrolmentToDelete.setCurricularCourse(null);
            candidateEnrolmentToDelete.setMasterDegreeCandidate(null);
            persistentCandidateEnrolment.deleteByOID(CandidateEnrolment.class,
                    candidateEnrolmentToDelete.getIdInternal());
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
            IMasterDegreeCandidate masterDegreeCandidate, Collection<Integer> curricularCoursesToEnroll)
            throws NonExistingServiceException, ExcepcaoPersistencia {
        Iterator iterCurricularCourseIds = curricularCoursesToEnroll.iterator();
        while (iterCurricularCourseIds.hasNext()) {

            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(CurricularCourse.class, (Integer) iterCurricularCourseIds.next());

            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }

            ICandidateEnrolment candidateEnrolment = DomainFactory.makeCandidateEnrolment();

            masterDegreeCandidate.addCandidateEnrolments(candidateEnrolment);
            candidateEnrolment.setCurricularCourse(curricularCourse);
        }
    }

 
}