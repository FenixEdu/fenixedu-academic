package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CandidateEnrolment;
import Dominio.ICandidateEnrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IMasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateEnrolment;

/*
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolmentOJB extends ObjectFenixOJB implements IPersistentCandidateEnrolment
{

    public void delete(ICandidateEnrolment candidateEnrolment) throws ExcepcaoPersistencia
    {
        super.delete(candidateEnrolment);
    }

  
  
    public List readByMDCandidate(IMasterDegreeCandidate masterDegreeCandidate)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("masterDegreeCandidate.idInternal", masterDegreeCandidate.getIdInternal());
        return queryList(CandidateEnrolment.class, crit);


     
        }

   public ICandidateEnrolment readByMDCandidateAndCurricularCourseScope(
        IMasterDegreeCandidate masterDegreeCandidate,
        ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("masterDegreeCandidate.idInternal", masterDegreeCandidate.getIdInternal());
        crit.addEqualTo("curricularCourseScope.idInternal", curricularCourseScope.getIdInternal());
        return (ICandidateEnrolment) queryObject(CandidateEnrolment.class, crit);

      
            }

      

    public ICandidateEnrolment readByMDCandidateAndCurricularCourse(
        IMasterDegreeCandidate masterDegreeCandidate,
        ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("masterDegreeCandidate.idInternal", masterDegreeCandidate.getIdInternal());
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return (ICandidateEnrolment) queryObject(CandidateEnrolment.class, criteria);
    }

	
      

    public void deleteAllByCandidateID(IMasterDegreeCandidate masterDegreeCandidate)
        throws ExcepcaoPersistencia
    {
        try
        {
            List result = this.readByMDCandidate(masterDegreeCandidate);
            Iterator iterator = result.iterator();
            while (iterator.hasNext())
            {
                delete((ICandidateEnrolment) iterator.next());
            }

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new ExcepcaoPersistencia();
        }
    }
}