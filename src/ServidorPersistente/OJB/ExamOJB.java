/*
 * ExamOJB.java
 *
 * Created on 2003/03/19
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;
import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.Exam;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;

public class ExamOJB extends ObjectFenixOJB implements IPersistentExam {

	public List readBy(Calendar day, Calendar beginning)
		throws ExcepcaoPersistencia {

			PersistenceBroker broker = ((HasBroker) odmg.currentTransaction()).getBroker(); 

			Criteria criteria = new Criteria();
			criteria.addEqualTo("day",day);
			criteria.addEqualTo("beginning",beginning);				
			Query queryPB = new QueryByCriteria(Exam.class, criteria);
			return (List) broker.getCollectionByQuery(queryPB);			
			
/* 		try {
 			String oqlQuery = "select exams from " + Exam.class.getName();
			oqlQuery += " where day = $1";
			oqlQuery += " and beginning = $2";
			query.create(oqlQuery);
			query.bind(day);
			query.bind(beginning);
			List result = (List) query.execute();
			lockRead(result);

			return result;
			
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}*/
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Exam.class.getName();
			oqlQuery += " order by season asc";
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void delete(IExam exam) throws ExcepcaoPersistencia {

		List associatedExecutionCourses = exam.getAssociatedExecutionCourses();

		if (associatedExecutionCourses != null) {
			for (int i = 0; i < associatedExecutionCourses.size(); i++) {
				IDisciplinaExecucao executionCourse =
					(IDisciplinaExecucao) associatedExecutionCourses.get(i);
				executionCourse.getAssociatedExams().remove(exam);

				IExamExecutionCourse examExecutionCourseToDelete =
					SuportePersistenteOJB
						.getInstance()
						.getIPersistentExamExecutionCourse()
						.readBy(
						exam,
						executionCourse);

				SuportePersistenteOJB
					.getInstance()
					.getIPersistentExamExecutionCourse()
					.delete(
					examExecutionCourseToDelete);
			}
		}

		exam.setAssociatedExecutionCourses(null);

		super.delete(exam);

		//		PersistenceBroker broker = ((HasBroker) tx).getBroker();
		//		broker.clearCache();
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Exam.class.getName();
		super.deleteAll(oqlQuery);
	}

}
