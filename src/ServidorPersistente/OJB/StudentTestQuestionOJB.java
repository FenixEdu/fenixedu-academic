/*
 * Created on 27/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDistributedTest;
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
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo(
			"keyDistributedTest",
			distributedTest.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}
	
	public List readByDistributedTest(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
			Criteria criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
			return queryList(StudentTestQuestion.class, criteria);
	}
	
	public List readByStudent(IStudent student) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
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
