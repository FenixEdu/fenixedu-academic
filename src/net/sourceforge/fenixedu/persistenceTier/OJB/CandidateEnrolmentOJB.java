package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateEnrolment;

import org.apache.ojb.broker.query.Criteria;

/*
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolmentOJB extends PersistentObjectOJB implements IPersistentCandidateEnrolment {

    public void delete(ICandidateEnrolment candidateEnrolment) throws ExcepcaoPersistencia {
        super.delete(candidateEnrolment);
    }

    public List readByMDCandidate(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("masterDegreeCandidate.idInternal", masterDegreeCandidate.getIdInternal());
        return queryList(CandidateEnrolment.class, crit);

    }

    public ICandidateEnrolment readByMDCandidateAndCurricularCourseScope(
            IMasterDegreeCandidate masterDegreeCandidate, ICurricularCourseScope curricularCourseScope)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("masterDegreeCandidate.idInternal", masterDegreeCandidate.getIdInternal());
        crit.addEqualTo("curricularCourseScope.idInternal", curricularCourseScope.getIdInternal());
        return (ICandidateEnrolment) queryObject(CandidateEnrolment.class, crit);

    }

    public ICandidateEnrolment readByMDCandidateAndCurricularCourse(
            IMasterDegreeCandidate masterDegreeCandidate, ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("masterDegreeCandidate.idInternal", masterDegreeCandidate.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return (ICandidateEnrolment) queryObject(CandidateEnrolment.class, criteria);
    }

    public void deleteAllByCandidateID(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia {
        try {
            List result = this.readByMDCandidate(masterDegreeCandidate);
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                delete((ICandidateEnrolment) iterator.next());
            }

        } catch (ExcepcaoPersistencia e) {
            throw new ExcepcaoPersistencia();
        }
    }
}