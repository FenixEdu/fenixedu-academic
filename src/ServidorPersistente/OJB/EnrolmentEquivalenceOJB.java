package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EnrolmentEquivalence;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalenceOJB extends ObjectFenixOJB implements IPersistentEnrolmentEquivalence
{

    public void lockWrite(IEnrolmentEquivalence equivalenceToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IEnrolmentEquivalence equivalenceFromDB = null;

        // If there is nothing to write, simply return.
        if (equivalenceToWrite == null)
        {
            return;
        }

        // Read EnrolmentEquivalence from database.
        try
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("enrolmentKey", equivalenceToWrite.getEnrolment().getIdInternal());
            criteria.addEqualTo("idInternal", equivalenceToWrite.getIdInternal());
            equivalenceFromDB =
                (IEnrolmentEquivalence) queryObject(EnrolmentEquivalence.class, criteria);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw e;
        }

        // If EnrolmentEquivalence is not in database, then write it.
        if (equivalenceFromDB == null)
        {
            super.lockWrite(equivalenceToWrite);
            // else If the EnrolmentEquivalence is mapped to the database, then
			// write any existing changes.
        }
        else if (
            (equivalenceToWrite instanceof EnrolmentEquivalence)
                && ((EnrolmentEquivalence) equivalenceFromDB).getIdInternal().equals(
                    ((EnrolmentEquivalence) equivalenceToWrite).getIdInternal()))
        {
            super.lockWrite(equivalenceToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

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

    public IEnrolmentEquivalence readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (IEnrolmentEquivalence) queryObject(EnrolmentEquivalence.class, criteria);
    }
}