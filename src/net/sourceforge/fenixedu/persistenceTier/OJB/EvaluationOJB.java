package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Fernanda Quitério 25/06/2003
 *  
 */
public class EvaluationOJB extends PersistentObjectOJB implements IPersistentEvaluation {

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Evaluation.class, criteria);
    }

}