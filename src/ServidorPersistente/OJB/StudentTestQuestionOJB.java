/*
 * Created on 27/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IDistributedTest;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestionOJB
	extends ObjectFenixOJB
	implements IPersistentStudentTestQuestion {

	public StudentTestQuestionOJB() {
	}

	public List readByStudentAndDistributedTest(
		IStudent student,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo(
			"distributedTest.idInternal",
			distributedTest.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyDistributedTest",
			distributedTest.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByStudent(IStudent student) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByQuestion(IQuestion question)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByQuestionAndDistributedTest(
		IQuestion question,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		criteria.addEqualTo(
			"keyDistributedTest",
			distributedTest.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public StudentTestQuestion readByQuestionAndStudentAndDistributedTest(
		IQuestion question,
		IStudent student,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo(
			"keyDistributedTest",
			distributedTest.getIdInternal());
		return (StudentTestQuestion) queryObject(
			StudentTestQuestion.class,
			criteria);
	}

	public List readStudentsByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyDistributedTest",
			distributedTest.getIdInternal());

		PersistenceBroker pb =
			((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria =
			new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		queryCriteria.addGroupBy("keyStudent");
		List result = (List) pb.getCollectionByQuery(queryCriteria);

		lockRead(result);

		List studentList = new ArrayList();

		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			IStudentTestQuestion studentTestQuestion =
				(IStudentTestQuestion) iterator.next();
			studentList.add(studentTestQuestion.getStudent());
		}
		return studentList;

	}

	public List readStudentTestQuestionsByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		List result = null;
		IStudentTestQuestion studentTestQuestion = null;

		List studentTestQuestions = readByDistributedTest(distributedTest);
		if (studentTestQuestions != null && studentTestQuestions.size() != 0) {
			studentTestQuestion =
				(StudentTestQuestion) readByDistributedTest(
					distributedTest).get(
					0);
			result =
				readByStudentAndDistributedTest(
					studentTestQuestion.getStudent(),
					distributedTest);
		}
		return result;
	}

	public void deleteByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyDistributedTest",
			distributedTest.getIdInternal());
		List studentTestQuestions =
			queryList(StudentTestQuestion.class, criteria);
		Iterator it = studentTestQuestions.iterator();
		while (it.hasNext()) {
			delete((IStudentTestQuestion) it.next());
		}
	}

	public void delete(IStudentTestQuestion studentTestQuestion)
		throws ExcepcaoPersistencia {
		super.delete(studentTestQuestion);
	}

}
