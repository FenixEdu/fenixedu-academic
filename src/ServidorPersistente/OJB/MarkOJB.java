package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

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
		Criteria criteria = new Criteria();
		criteria.addEqualTo("attend.idInternal", attend.getIdInternal());
		return queryList(Mark.class, criteria);
    }

    public List readBy(IEvaluation evaluation) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("evaluation.idInternal", evaluation.getIdInternal());
        return queryList(Mark.class, criteria);
    }

    public IMark readBy(IEvaluation evaluation, IFrequenta attend) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("evaluation.idInternal", evaluation.getIdInternal());
		criteria.addEqualTo("attend.idInternal", attend.getIdInternal());		
		return (IMark)queryObject(Mark.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(Mark.class, criteria);
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
	public void deleteByEvaluation(IEvaluation evaluation) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyEvaluation", evaluation.getIdInternal());
		List marks = queryList(Mark.class, criteria);
		Iterator it = marks.iterator();
		while (it.hasNext())
			delete((Mark) it.next());
}
}
