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
import java.util.ArrayList;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Exam;
import Dominio.ExamExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ExamExecutionCourseOJB
	extends ObjectFenixOJB
	implements IPersistentExamExecutionCourse {

	public IExamExecutionCourse readBy(
		IExam exam,
		IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		try {
			IExamExecutionCourse examExecutionCourse = null;

			String oqlQuery =
				"select examexecutioncourse from "
					+ ExamExecutionCourse.class.getName();
			//oqlQuery += " where exam.day = $1";
			oqlQuery += " where exam.season = $1";
			oqlQuery += " and executionCourse.sigla = $2";
			oqlQuery += " and executionCourse.executionPeriod.name = $3";
			oqlQuery
				+= " and executionCourse.executionPeriod.executionYear.year = $4";

			query.create(oqlQuery);
			//query.bind(exam.getDay());
			query.bind(exam.getSeason());
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				examExecutionCourse = (IExamExecutionCourse) result.get(0);
			return examExecutionCourse;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		try {

			String oqlQuery =
				"select examexecutioncourse from "
					+ ExamExecutionCourse.class.getName();

			oqlQuery += " where executionCourse.sigla = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery
				+= " and executionCourse.executionPeriod.executionYear.year = $3";

			query.create(oqlQuery);

			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + ExamExecutionCourse.class.getName();
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
			tx.commit();
			tx.begin();

			IExam exam = examExecutionCourseToWrite.getExam();
			IExecutionCourse executionCourse =
				examExecutionCourseToWrite.getExecutionCourse();

			List associatedExecutionCourses =
				examExecutionCourseToWrite
					.getExam()
					.getAssociatedExecutionCourses();

			List associatedExams =
				examExecutionCourseToWrite
					.getExecutionCourse()
					.getAssociatedExams();

			if (associatedExams == null) {
				associatedExams = new ArrayList();
				executionCourse.setAssociatedExams(associatedExams);
			}
			associatedExams.add(exam);

			if (associatedExecutionCourses == null) {
				associatedExecutionCourses = new ArrayList();
				exam.setAssociatedExecutionCourses(associatedExecutionCourses);
			}
			associatedExecutionCourses.add(executionCourse);
			//			PersistenceBroker pb = ((HasBroker)tx).getBroker();
			//			pb.clearCache();
			//			pb.removeFromCache(examExecutionCourseToWrite.getExam());
			//			pb.removeFromCache(examExecutionCourseToWrite.getExecutionCourse());
		}

		// else if (exam is mapped to the database then write any existing changes)
		else if (
			(examExecutionCourseToWrite instanceof Exam)
				&& ((Exam) examExecutionCourseFromDB).getIdInternal().equals(
					((Exam) examExecutionCourseToWrite).getIdInternal())) {
			examExecutionCourseFromDB.setExam(
				examExecutionCourseToWrite.getExam());
			examExecutionCourseFromDB.setExecutionCourse(
				examExecutionCourseToWrite.getExecutionCourse());
			// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IExam exam) throws ExcepcaoPersistencia {
		List examsExecutionCourses = readByCriteria(exam);
		for (int i = 0; i < examsExecutionCourses.size(); i++) {
			IExamExecutionCourse examExecutionCourse =
				(IExamExecutionCourse) examsExecutionCourses.get(i);
			super.delete(examExecutionCourse);
		}
	}

	public void delete(IExamExecutionCourse examExecutionCourse)
		throws ExcepcaoPersistencia {
		super.delete(examExecutionCourse);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery =
			"select all from " + ExamExecutionCourse.class.getName();
		super.deleteAll(oqlQuery);
	}

}
