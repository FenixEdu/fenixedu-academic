package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public class WriteCandidateEnrolments extends Service {

    public void run(Set<Integer> selectedCurricularCoursesIDs, Integer candidateID, Double credits,
            String givenCreditsRemarks) throws FenixServiceException, ExcepcaoPersistencia {

        MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);
        if (masterDegreeCandidate == null) {
            throw new NonExistingServiceException();
        }

        masterDegreeCandidate.setGivenCredits(credits);

        if (credits.floatValue() != 0) {
            masterDegreeCandidate.setGivenCreditsRemarks(givenCreditsRemarks);
        }

        List<CandidateEnrolment> candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();
        List<Integer> candidateEnrolmentsCurricularCoursesIDs = (List<Integer>) CollectionUtils.collect(
                candidateEnrolments, new Transformer() {
                    public Object transform(Object arg0) {
                        CandidateEnrolment candidateEnrolment = (CandidateEnrolment) arg0;
                        return candidateEnrolment.getCurricularCourse().getIdInternal();
                    }
                });

        Collection<Integer> curricularCoursesToEnroll = CollectionUtils.subtract(
                selectedCurricularCoursesIDs, candidateEnrolmentsCurricularCoursesIDs);

        final Collection<Integer> curricularCoursesToDelete = CollectionUtils.subtract(
                candidateEnrolmentsCurricularCoursesIDs, selectedCurricularCoursesIDs);

        Collection<CandidateEnrolment> candidateEnrollmentsToDelete = CollectionUtils
                .select(candidateEnrolments, new Predicate() {
                    public boolean evaluate(Object arg0) {
                        CandidateEnrolment candidateEnrolment = (CandidateEnrolment) arg0;
                        return (curricularCoursesToDelete.contains(candidateEnrolment
                                .getCurricularCourse().getIdInternal()));
                    }
                });

        writeFilteredEnrollments(masterDegreeCandidate, curricularCoursesToEnroll);

        for (CandidateEnrolment candidateEnrolmentToDelete : candidateEnrollmentsToDelete) {
            candidateEnrolmentToDelete.delete();
        }
    }

    /**
     * @param persistentSupport
     * @param masterDegreeCandidate
     * @param curricularCoursesToEnroll
     * @throws NonExistingServiceException
     * @throws ExcepcaoPersistencia
     */
    private void writeFilteredEnrollments(MasterDegreeCandidate masterDegreeCandidate, Collection<Integer> curricularCoursesToEnroll)
            throws NonExistingServiceException, ExcepcaoPersistencia {
        Iterator iterCurricularCourseIds = curricularCoursesToEnroll.iterator();
        while (iterCurricularCourseIds.hasNext()) {

            CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID((Integer) iterCurricularCourseIds.next());

            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }

            CandidateEnrolment candidateEnrolment = new CandidateEnrolment();

            masterDegreeCandidate.addCandidateEnrolments(candidateEnrolment);
            candidateEnrolment.setCurricularCourse(curricularCourse);
        }
    }

 
}