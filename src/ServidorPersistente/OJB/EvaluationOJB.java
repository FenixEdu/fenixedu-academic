package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.Evaluation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluation;

/**
 * @author Fernanda Quitério
 * 25/06/2003
 *
 */
public class EvaluationOJB extends ObjectFenixOJB implements IPersistentEvaluation {

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Evaluation.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Evaluation.class.getName();
		super.deleteAll(oqlQuery);
	}
}
