package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentCandidateEnrolment extends IPersistentObject {

    /**
     * @param candidateEnrolment
     * @throws ExcepcaoPersistencia
     */
    public void delete(ICandidateEnrolment candidateEnrolment) throws ExcepcaoPersistencia;

    /**
     * @param masterDegreeCandidate
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByMDCandidate(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia;

    /**
     * @param masterDegreeCandidate
     * @param curricularCourseScope
     * @return @throws
     *         ExcepcaoPersistencia
     * @deprecated
     */
    public ICandidateEnrolment readByMDCandidateAndCurricularCourseScope(
            IMasterDegreeCandidate masterDegreeCandidate, ICurricularCourseScope curricularCourseScope)
            throws ExcepcaoPersistencia;

    /**
     * @param masterDegreeCandidate
     * @param curricularCourse
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public ICandidateEnrolment readByMDCandidateAndCurricularCourse(
            IMasterDegreeCandidate masterDegreeCandidate, ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    /**
     * @param masterDegreeCandidate
     * @throws ExcepcaoPersistencia
     */
    public void deleteAllByCandidateID(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia;
}