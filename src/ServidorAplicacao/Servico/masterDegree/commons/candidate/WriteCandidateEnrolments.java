package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.CandidateEnrolment;
import Dominio.CurricularCourse;
import Dominio.ICandidateEnrolment;
import Dominio.ICurricularCourse;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateEnrolment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class WriteCandidateEnrolments implements IServico {

    private static WriteCandidateEnrolments servico = new WriteCandidateEnrolments();

    /**
     * The singleton access method of this class.
     */
    public static WriteCandidateEnrolments getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private WriteCandidateEnrolments() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "WriteCandidateEnrolments";
    }

    public void run(Integer[] selection, Integer candidateID, Double credits,
            String givenCreditsRemarks) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCandidateEnrolment persistentCandidateEnrolment = sp
                    .getIPersistentCandidateEnrolment();

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(
                            MasterDegreeCandidate.class, candidateID, true);

            if (masterDegreeCandidate == null) {
                throw new NonExistingServiceException();
            }

            masterDegreeCandidate.setGivenCredits(credits);

            if (credits.floatValue() != 0) {
                masterDegreeCandidate
                        .setGivenCreditsRemarks(givenCreditsRemarks);
            }

            List candidateEnrollmentsToDelete = new ArrayList();
            List curricularCoursesToEnroll = new ArrayList();

            for (int i = 0; i < selection.length; i++) {
                curricularCoursesToEnroll.add(selection[i]);
            }

            filterEnrollments(persistentCandidateEnrolment,
                    masterDegreeCandidate, candidateEnrollmentsToDelete,
                    curricularCoursesToEnroll);

            writeFilteredEnrollments(sp, masterDegreeCandidate,
                    curricularCoursesToEnroll);

            deleteRemainingEnrollments(persistentCandidateEnrolment,
                    candidateEnrollmentsToDelete);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

    /**
     * @param persistentCandidateEnrolment
     * @param candidateEnrollmentsToDelete
     * @throws ExcepcaoPersistencia
     */
    private void deleteRemainingEnrollments(
            IPersistentCandidateEnrolment persistentCandidateEnrolment,
            List candidateEnrollmentsToDelete) throws ExcepcaoPersistencia {
        Iterator iterCandidateEnrollmentsToDelete = candidateEnrollmentsToDelete
                .iterator();
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
            IMasterDegreeCandidate masterDegreeCandidate,
            List curricularCoursesToEnroll) throws NonExistingServiceException,
            ExcepcaoPersistencia {
        Iterator iterCurricularCourseIds = curricularCoursesToEnroll.iterator();
        while (iterCurricularCourseIds.hasNext()) {

            ICurricularCourse curricularCourse = (ICurricularCourse) sp
                    .getIPersistentCurricularCourse().readByOID(
                            CurricularCourse.class,
                            (Integer) iterCurricularCourseIds.next());

            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }

            ICandidateEnrolment candidateEnrolment = new CandidateEnrolment();
            sp.getIPersistentCandidateEnrolment().simpleLockWrite(
                    candidateEnrolment);

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
    private void filterEnrollments(
            IPersistentCandidateEnrolment persistentCandidateEnrolment,
            IMasterDegreeCandidate masterDegreeCandidate,
            List candidateEnrollmentsToDelete, List curricularCoursesToEnroll)
            throws ExcepcaoPersistencia {
        List existentCandidateEnrollments = persistentCandidateEnrolment
                .readByMDCandidate(masterDegreeCandidate);
        Iterator iterCandidateEnrollment = existentCandidateEnrollments
                .iterator();
        while (iterCandidateEnrollment.hasNext()) {
            ICandidateEnrolment existentCandidateEnrolment = (ICandidateEnrolment) iterCandidateEnrollment
                    .next();
            if (curricularCoursesToEnroll.contains(existentCandidateEnrolment
                    .getCurricularCourse().getIdInternal())) {
                removeElement(curricularCoursesToEnroll,
                        existentCandidateEnrolment);
            } else {
                candidateEnrollmentsToDelete.add(existentCandidateEnrolment);
            }
        }
    }

    /**
     * @param curricularCoursesToEnroll
     * @param existentCandidateEnrolment
     */
    private void removeElement(List curricularCoursesToEnroll,
            ICandidateEnrolment existentCandidateEnrolment) {
        final Integer idToRemove = existentCandidateEnrolment
                .getCurricularCourse().getIdInternal();
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