/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.Evaluation;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author João Mota
 *
 * 
 */
public class EvaluationOJB
	extends ObjectFenixOJB
	implements IPersistentEvaluation {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluation#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
	public IEvaluation readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
			try {
				IEvaluation evaluation= null;
				String oqlQuery = "select all from " + Evaluation.class.getName();
				oqlQuery += " where executionCourse.sigla = $1 ";
				oqlQuery += " and executionCourse.executionPeriod.name = $2 ";
				oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $3 ";
				query.create(oqlQuery);
				query.bind(executionCourse.getSigla());
				query.bind(executionCourse.getExecutionPeriod().getName());
				query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
				List result = (List) query.execute();
				
				lockRead(result);
				if (result.size() != 0){
					evaluation = (IEvaluation) result.get(0);
				}
				return evaluation;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluation#readAll()
	 */
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

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluation#delete(Dominio.IEvaluation)
	 */
	public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia {
		super.delete(evaluation);
	//TODO: add to the execution Course delete methods to delete all evaluations, all curriculums and all sites
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluation#deleteAll()
	 */
	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Evaluation.class.getName();
		super.deleteAll(oqlQuery);

	}
	
	public void lockWrite(IEvaluation evaluation) throws ExcepcaoPersistencia {
		IEvaluation evaluationFromDB = null;

					// If there is nothing to write, simply return.
					if (evaluation == null)
						return;

					// Read professorship from database.
				evaluationFromDB =
						this.readByExecutionCourse(
						evaluation.getExecutionCourse());

					// If professorship is not in database, then write it.
					if (evaluationFromDB == null)
						super.lockWrite(evaluation);
				
					// else If the professorship is mapped to the database, then write any existing changes.
					else if (
						(evaluation instanceof Evaluation)
							&& ((Evaluation) evaluationFromDB).getIdInternal().equals(
								((Evaluation) evaluation).getIdInternal())) {
						super.lockWrite(evaluation);
						// else Throw an already existing exception
					} else
						throw new ExistingPersistentException();
					}
	
	

}
