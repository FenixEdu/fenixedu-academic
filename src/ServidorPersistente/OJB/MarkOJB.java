package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.Mark;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Angela
 *
 * 
 */

public class MarkOJB extends ObjectFenixOJB implements IPersistentMark
{

    public MarkOJB()
    {
        super();
    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Mark.class.getName();
            super.deleteAll(oqlQuery);
        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public void lockWrite(IMark markToWrite) throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IMark markFromDB = null;
        // If there is nothing to write, simply return.
        if (markToWrite == null)
        {
            return;
        }

        // Read mark from database.
        markFromDB = this.readBy(markToWrite.getEvaluation(), markToWrite.getAttend());
        // If mark is not in database, then write it.
        if (markFromDB == null)
        {
            super.lockWrite(markToWrite);
            // else If the mark is mapped to the database, then write any existing changes.
        } else if (
            (markToWrite instanceof Mark)
                && ((Mark) markFromDB).getIdInternal().equals(((Mark) markToWrite).getIdInternal()))
        {
            super.lockWrite(markToWrite);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

    public void delete(IMark mark) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(mark);
        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public List readBy(IFrequenta attend) throws ExcepcaoPersistencia
    {
        try
        {

            String oqlQuery = "select all from " + Mark.class.getName();
            oqlQuery += " where attend = $1";
            query.create(oqlQuery);
            query.bind(attend.getIdInternal());
            List result = (List) query.execute();
            lockRead(result);
            return result;

        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readBy(IEvaluation evaluation) throws ExcepcaoPersistencia
    {

        try
        {

            String oqlQuery = "select all from " + Mark.class.getName();
            oqlQuery += " where evaluation = $1";
            query.create(oqlQuery);
            query.bind(evaluation.getIdInternal());
            List result = (List) query.execute();
            lockRead(result);
            return result;

        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IMark readBy(IEvaluation evaluation, IFrequenta attend) throws ExcepcaoPersistencia
    {

        try
        {
            IMark mark = null;
            String oqlQuery = "select all from " + Mark.class.getName();
            oqlQuery += " where evaluation = $1";
            oqlQuery += " and attend = $2";

            query.create(oqlQuery);
            query.bind(evaluation.getIdInternal());
            query.bind(attend.getIdInternal());
            List result = (List) query.execute();
            try
            {
                lockRead(result);
            } catch (ExcepcaoPersistencia ex)
            {
                throw ex;
            }

            if ((result != null) && (result.size() != 0))
            {
                mark = (IMark) result.get(0);
            }
            return mark;

        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAll() throws ExcepcaoPersistencia
    {

        try
        {
            ArrayList list = new ArrayList();
            String oqlQuery = "select all from " + Mark.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();

            try
            {
                lockRead(result);
            } catch (ExcepcaoPersistencia ex)
            {
                throw ex;
            }

            if ((result != null) && (result.size() != 0))
            {
                ListIterator iterator = result.listIterator();
                while (iterator.hasNext())
                    list.add(iterator.next());
            }
            return list;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readBy(IEvaluation evaluation, boolean published) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyEvaluation", evaluation.getIdInternal());
        if (published)
        {
            criteria.addNotNull("publishedMark");
        }
        return queryList(Mark.class, criteria);
    }
}
