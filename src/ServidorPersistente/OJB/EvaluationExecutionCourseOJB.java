package ServidorPersistente.OJB;

/**
 *
 * @author  Tânia Pousão
 */
import java.util.ArrayList;
import java.util.List;

import org.odmg.QueryException;

import Dominio.EvaluationExecutionCourse;
import Dominio.ExamExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IEvalutionExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class EvaluationExecutionCourseOJB extends ObjectFenixOJB implements IPersistentEvaluationExecutionCourse {

	public IEvalutionExecutionCourse readBy(IEvaluation evaluation, IExecutionCourse executionCourse) throws ExcepcaoPersistencia {

		IEvalutionExecutionCourse evalutionExecutionCourse = null;

		try {
			if (evaluation instanceof IExam) {
				String oqlQuery = "select examexecutioncourse from " + ExamExecutionCourse.class.getName();
				//oqlQuery += " where exam.day = $1";
				oqlQuery += " where exam.season = $1";
				oqlQuery += " and executionCourse.sigla = $2";
				oqlQuery += " and executionCourse.executionPeriod.name = $3";
				oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $4";

				query.create(oqlQuery);
				//query.bind(exam.getDay());
				query.bind(((IExam) evaluation).getSeason());
				query.bind(executionCourse.getSigla());
				query.bind(executionCourse.getExecutionPeriod().getName());
				query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

				List result = (List) query.execute();
				lockRead(result);
				if (result.size() != 0)
					evalutionExecutionCourse = (IExamExecutionCourse) result.get(0);
			}

			return evalutionExecutionCourse;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
		try {

			String oqlQuery = "select examexecutioncourse from " + ExamExecutionCourse.class.getName();

			oqlQuery += " where executionCourse.sigla = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $3";

			query.create(oqlQuery);

			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + EvaluationExecutionCourse.class.getName();
			oqlQuery += " order by executionCourse.sigla asc";
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(IEvalutionExecutionCourse evalutionExecutionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IEvalutionExecutionCourse evalutionExecutionCourseFromDB = null;

		if (evalutionExecutionCourseToWrite == null)
			return;

		//read evaluation
		evalutionExecutionCourseFromDB =
			this.readBy(evalutionExecutionCourseToWrite.getEvaluation(), evalutionExecutionCourseToWrite.getExecutionCourse());

		//if (evaluation not in database) then write it
		if (evalutionExecutionCourseFromDB == null) {

			super.lockWrite(evalutionExecutionCourseToWrite);
			tx.commit();
			tx.begin();

			IEvaluation evaluation = evalutionExecutionCourseToWrite.getEvaluation();
			IExecutionCourse executionCourse = evalutionExecutionCourseToWrite.getExecutionCourse();

			List associatedExecutionCourses = evalutionExecutionCourseToWrite.getEvaluation().getAssociatedExecutionCourses();
			List associatedEvaluations = evalutionExecutionCourseToWrite.getExecutionCourse().getAssociatedExams();

			if (associatedEvaluations == null) {
				associatedEvaluations = new ArrayList();
				executionCourse.setAssociatedExams(associatedEvaluations);
			}
			associatedEvaluations.add(evaluation);

			if (associatedExecutionCourses == null) {
				associatedExecutionCourses = new ArrayList();
				evaluation.setAssociatedExecutionCourses(associatedExecutionCourses);
			}
			associatedExecutionCourses.add(executionCourse);
		}

		// else if (evaluation is mapped to the database then write any existing changes)
		else if (
			(evalutionExecutionCourseToWrite instanceof EvaluationExecutionCourse)
				&& ((EvaluationExecutionCourse) evalutionExecutionCourseFromDB).getIdInternal().equals(
					((EvaluationExecutionCourse) evalutionExecutionCourseToWrite).getIdInternal())) {
			evalutionExecutionCourseFromDB.setEvaluation(evalutionExecutionCourseToWrite.getEvaluation());
			evalutionExecutionCourseFromDB.setExecutionCourse(evalutionExecutionCourseToWrite.getExecutionCourse());
			// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia {
		List evaluationsExecutionCourses = readByCriteria(evaluation);
		for (int i = 0; i < evaluationsExecutionCourses.size(); i++) {
			IEvalutionExecutionCourse evalutionExecutionCourse = (IEvalutionExecutionCourse) evaluationsExecutionCourses.get(i);
			super.delete(evalutionExecutionCourse);
		}
	}

	public void delete(IEvalutionExecutionCourse evalutionExecutionCourse) throws ExcepcaoPersistencia {
		super.delete(evalutionExecutionCourse);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + EvaluationExecutionCourse.class.getName();
		super.deleteAll(oqlQuery);
	}

}
