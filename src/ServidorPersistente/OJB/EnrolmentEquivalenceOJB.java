package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EnrolmentEquivalence;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentEquivalence;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEquivalenceOJB extends ObjectFenixOJB implements IPersistentEnrolmentEquivalence
{

    public void delete(IEnrolmentEquivalence enrolment) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(enrolment);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public List readAll() throws ExcepcaoPersistencia
    {

        return queryList(EnrolmentEquivalence.class, new Criteria());
    }

    public IEnrolmentEquivalence readByEnrolment(IEnrollment enrolment) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (IEnrolmentEquivalence) queryObject(EnrolmentEquivalence.class, criteria);
    }
}