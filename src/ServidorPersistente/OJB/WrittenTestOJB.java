/*
 * Created on 20/Out/2003
 *
  */
package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IWrittenTest;
import Dominio.WrittenTest;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWrittenTest;

/**
 * @author Ana e Ricardo
 *
 */
public class WrittenTestOJB extends ObjectFenixOJB implements IPersistentWrittenTest{
	public List readBy(Calendar day, Calendar beginning)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("day", day);
		criteria.addEqualTo("beginning", beginning);
		return queryList(WrittenTest.class, criteria);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + WrittenTest.class.getName();
			//oqlQuery += " order by season asc";
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void delete(IWrittenTest writtenTest) throws ExcepcaoPersistencia {
		// TO DO apagar tb as associações com outras tabelas
		
//		Criteria criteria = new Criteria();
//		criteria.addEqualTo("keyWrittenTest", writtenTest.getIdInternal());
//		List examEnrollments = queryList(ExamStudentRoom.class, criteria);
//		if (examEnrollments != null && !examEnrollments.isEmpty()) {
//			throw new notAuthorizedPersistentDeleteException();
//		}
//		else{
//
//			List associatedExecutionCourses =
//				exam.getAssociatedExecutionCourses();
//
//			if (associatedExecutionCourses != null) {
//				for (int i = 0; i < associatedExecutionCourses.size(); i++) {
//					IDisciplinaExecucao executionCourse =
//						(IDisciplinaExecucao) associatedExecutionCourses.get(i);
//					executionCourse.getAssociatedExams().remove(exam);
//
//					IExamExecutionCourse examExecutionCourseToDelete =
//						SuportePersistenteOJB
//							.getInstance()
//							.getIPersistentExamExecutionCourse()
//							.readBy(
//							exam,
//							executionCourse);
//
//					SuportePersistenteOJB
//						.getInstance()
//						.getIPersistentExamExecutionCourse()
//						.delete(
//						examExecutionCourseToDelete);
//				}
//			}
//
//			exam.setAssociatedExecutionCourses(null);

			super.delete(writtenTest);
//		}
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + WrittenTest.class.getName();
		super.deleteAll(oqlQuery);
	}



}
