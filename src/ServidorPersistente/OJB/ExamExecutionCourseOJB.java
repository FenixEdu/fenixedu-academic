/*
 * ExamExecutionCourseOJB.java
 *
 * Created on 2003/03/29
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import org.odmg.QueryException;

import Dominio.Exam;
import Dominio.ExamExecutionCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ExamExecutionCourseOJB
	extends ObjectFenixOJB
	implements IPersistentExamExecutionCourse {

	public IExamExecutionCourse readBy(IExam exam, IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		try {
			IExamExecutionCourse examExecutionCourse = null;

			String oqlQuery = "select examexecutioncourse from " + ExamExecutionCourse.class.getName();
			oqlQuery += " where exam.day = $1";
			oqlQuery += " and exam.season = $2";
			oqlQuery += " and executionCourse.sigla = $3";
			oqlQuery += " and executionCourse.executionPeriod.name = $4";
			oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $5";

			query.create(oqlQuery);
			query.bind(exam.getDay());
			query.bind(exam.getSeason());
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				examExecutionCourse = (IExamExecutionCourse) result.get(0);
			return examExecutionCourse;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + ExamExecutionCourse.class.getName();
			oqlQuery += " order by executionCourse.sigla asc, exam.season asc";
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(IExamExecutionCourse examExecutionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {
		IExamExecutionCourse examExecutionCourseFromDB = null;
		if (examExecutionCourseToWrite == null)
			return;

		// read exam		
		examExecutionCourseFromDB =
			this.readBy(
				examExecutionCourseToWrite.getExam(),
				examExecutionCourseToWrite.getExecutionCourse());

		// if (exam not in database) then write it
		if (examExecutionCourseFromDB == null) {
			super.lockWrite(examExecutionCourseToWrite);
		}

		// else if (exam is mapped to the database then write any existing changes)
		else if (
			(examExecutionCourseToWrite instanceof Exam)
				&& ((Exam) examExecutionCourseFromDB).getIdInternal().equals(
					((Exam) examExecutionCourseToWrite).getIdInternal())) {
			examExecutionCourseFromDB.setExam(examExecutionCourseToWrite.getExam());
			examExecutionCourseFromDB.setExecutionCourse(examExecutionCourseToWrite.getExecutionCourse());
		// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IExamExecutionCourse examExecutionCourse) throws ExcepcaoPersistencia {
		super.delete(examExecutionCourse);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + ExamExecutionCourse.class.getName();
		super.deleteAll(oqlQuery);
	}

}
