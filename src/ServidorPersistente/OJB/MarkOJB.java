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

/**
 * @author Angela
 */

public class MarkOJB extends PersistentObjectOJB implements IPersistentMark {

    public MarkOJB() {
        super();
    }

    public void delete(IMark mark) throws ExcepcaoPersistencia {
        try {
            super.delete(mark);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public List readBy(IFrequenta attend) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("attend.idInternal", attend.getIdInternal());
        return queryList(Mark.class, criteria);
    }

    public List readBy(IEvaluation evaluation) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("evaluation.idInternal", evaluation.getIdInternal());
        return queryList(Mark.class, criteria);
    }

    public IMark readBy(IEvaluation evaluation, IFrequenta attend) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("evaluation.idInternal", evaluation.getIdInternal());
        criteria.addEqualTo("attend.idInternal", attend.getIdInternal());
        return (IMark) queryObject(Mark.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Mark.class, criteria);
    }

    public List readBy(IEvaluation evaluation, boolean published) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyEvaluation", evaluation.getIdInternal());
        if (published) {
            criteria.addNotNull("publishedMark");
        }
        return queryList(Mark.class, criteria);
    }

    public void deleteByEvaluation(IEvaluation evaluation) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyEvaluation", evaluation.getIdInternal());
        List marks = queryList(Mark.class, criteria);
        Iterator it = marks.iterator();
        while (it.hasNext()) {
            delete((Mark) it.next());
        }
    }
}