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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;

public class ExamOJB extends ObjectFenixOJB implements IPersistentExam {

	public List readBy(Calendar day, Calendar beginning)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("day", day);
		criteria.addEqualTo("beginning", beginning);
		return queryList(Exam.class, criteria);
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

		
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExam", exam.getIdInternal());
		List examEnrollments = queryList(ExamStudentRoom.class, criteria);
		if (examEnrollments != null && !examEnrollments.isEmpty()) {
			throw new notAuthorizedPersistentDeleteException();
		}
		else{

			List associatedExecutionCourses =
				exam.getAssociatedExecutionCourses();

			if (associatedExecutionCourses != null) {
				for (int i = 0; i < associatedExecutionCourses.size(); i++) {
					IExecutionCourse executionCourse =
						(IExecutionCourse) associatedExecutionCourses.get(i);
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

		}
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Exam.class.getName();
		super.deleteAll(oqlQuery);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentExam#readBy(Dominio.ISala, Dominio.IExecutionPeriod)
	 */
	public List readBy(ISala room, IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("associatedRooms.nome", room.getNome());
		criteria.addEqualTo(
			"associatedExecutionCourses.executionPeriod.name",
			executionPeriod.getName());
		criteria.addEqualTo(
			"associatedExecutionCourses.executionPeriod.executionYear.year",
			executionPeriod.getExecutionYear().getYear());
		List examsWithRepetition = queryList(Exam.class, criteria);
		List examsWithoutRepetition = new ArrayList();
		for (int i = 0; i < examsWithRepetition.size(); i++) {
			IExam exam = (IExam) examsWithRepetition.get(i);
			if (!examsWithoutRepetition.contains(exam)) {
				examsWithoutRepetition.add(exam);
			}
		}
		return examsWithoutRepetition;
	}

}
