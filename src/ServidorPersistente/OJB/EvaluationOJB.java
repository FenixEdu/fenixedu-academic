package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Evaluation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluation;

/**
 * @author Fernanda Quitério 25/06/2003
 *  
 */
public class EvaluationOJB extends ObjectFenixOJB implements IPersistentEvaluation
{

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(Evaluation.class, criteria);
    }

    
}
