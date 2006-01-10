package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Angela
 */

public class MarkOJB extends PersistentObjectOJB implements IPersistentMark {

    public MarkOJB() {
        super();
    }

    public void delete(Mark mark) throws ExcepcaoPersistencia {
	super.delete(mark);
    }

    public List readBy(Attends attend) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("attend.idInternal", attend.getIdInternal());
        return queryList(Mark.class, criteria);
    }

    public List readBy(Evaluation evaluation) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("evaluation.idInternal", evaluation.getIdInternal());
        return queryList(Mark.class, criteria);
    }

    public Mark readBy(Evaluation evaluation, Attends attend) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("evaluation.idInternal", evaluation.getIdInternal());
        criteria.addEqualTo("attend.idInternal", attend.getIdInternal());
        return (Mark) queryObject(Mark.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Mark.class, criteria);
    }

    public List readBy(Evaluation evaluation, boolean published) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyEvaluation", evaluation.getIdInternal());
        if (published) {
            criteria.addNotNull("publishedMark");
        }
        return queryList(Mark.class, criteria);
    }

    public void deleteByEvaluation(Evaluation evaluation) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyEvaluation", evaluation.getIdInternal());
        List marks = queryList(Mark.class, criteria);
        Iterator it = marks.iterator();
        while (it.hasNext()) {
            delete((Mark) it.next());
        }
    }
}
