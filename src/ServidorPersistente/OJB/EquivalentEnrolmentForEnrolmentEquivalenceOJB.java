package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EquivalentEnrolmentForEnrolmentEquivalenceOJB
    extends ObjectFenixOJB
    implements IPersistentEquivalentEnrolmentForEnrolmentEquivalence
{

   

    

    public void delete(IEquivalentEnrolmentForEnrolmentEquivalence enrolmentEquivalenceRestriction)
        throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(enrolmentEquivalenceRestriction);
        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
        IEnrolmentEquivalence enrolmentEquivalence,
        IEnrollment equivalentEnrolment)
        throws ExcepcaoPersistencia
    {
        try
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("enrolmentEquivalence.idInternal", enrolmentEquivalence.getIdInternal());
            criteria.addEqualTo("equivalentEnrolment.idInternal", equivalentEnrolment.getIdInternal());
            return (IEquivalentEnrolmentForEnrolmentEquivalence) queryObject(
                EquivalentEnrolmentForEnrolmentEquivalence.class,
                criteria);
        } catch (ExcepcaoPersistencia e)
        {
            throw e;
        }
    }

    public List readByEquivalentEnrolment(IEnrollment equivalentEnrolment) throws ExcepcaoPersistencia
    {
        try
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("equivalentEnrolment.idInternal", equivalentEnrolment.getIdInternal());
            return queryList(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
        } catch (ExcepcaoPersistencia e)
        {
            throw e;
        }
    }

    public List readAll() throws ExcepcaoPersistencia
    {

       return queryList(EquivalentEnrolmentForEnrolmentEquivalence.class,new Criteria());
    }

    public List readByEnrolmentEquivalence(IEnrolmentEquivalence enrolmentEquivalence)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolmentEquivalence.idInternal", enrolmentEquivalence.getIdInternal());
		return queryList(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
	}

}