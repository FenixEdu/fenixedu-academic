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

import org.odmg.QueryException;

import Dominio.Exam;
import Dominio.IExam;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;

public class ExamOJB extends ObjectFenixOJB implements IPersistentExam {

	public List readBy(Calendar day, Calendar beginning)
		throws ExcepcaoPersistencia {
		try {
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
		}
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
		super.delete(exam);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Exam.class.getName();
		super.deleteAll(oqlQuery);
	}

}
